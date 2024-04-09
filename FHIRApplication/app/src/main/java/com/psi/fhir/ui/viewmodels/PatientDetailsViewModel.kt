package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.delete
import com.google.android.fhir.get
import com.google.android.fhir.logicalId
import com.google.android.fhir.search.search
import com.psi.fhir.FhirApplication
import com.psi.fhir.R
import com.psi.fhir.data.PatientDetailsUiState
import com.psi.fhir.data.RequestResult
import com.psi.fhir.helper.AppConfigurationHelper
import org.hl7.fhir.r4.model.Encounter
import org.hl7.fhir.r4.model.MedicationRequest
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PatientDetailsViewModel (application: Application): AndroidViewModel(application) {

    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)
    private var patientDetailData : PatientDetailData? = null

    suspend fun getPatientDetails(patientId: String): PatientDetailData {
        val patient = getPatient(patientId)
        val encounters = getEncounters(patientId)

        val observations: MutableList<ObservationListItem> = mutableListOf()

        for( enc in encounters ) {
            observations.addAll( getObservationsById( enc.id) )
        }

        patientDetailData = PatientDetailData(patient = patient, encounters = encounters, observations = observations )

        return patientDetailData!!
    }

    suspend fun deletePatientDetails() : RequestResult {
        try {
           
            patientDetailData?.let {
                for( item in it.observations ) {
                    fhirEngine.delete<Observation>(item.id)
                }

                for( item in it.encounters ) {
                    fhirEngine.delete<Encounter>(item.id)
                }

                fhirEngine.delete<Patient>(it.patient.id)

                return RequestResult(true)
            }
        }
        catch (ex : Exception ){
            return RequestResult(false, ex.message)
        }

       return RequestResult(false, getApplication<FhirApplication>().getString(R.string.no_data_to_delete))
    }




    private suspend fun getPatient(patientId: String): PatientDetailsUiState {
        val patient = fhirEngine.get<Patient>(patientId)
        return patient.toPatientDetailsUiState()
    }

    private suspend fun getEncounters(patientId: String): List<EncounterListItem> {
        val encounters: MutableList<EncounterListItem> = mutableListOf()

        fhirEngine
            .search<Encounter> {
                filter(Observation.SUBJECT, { value = "Patient/$patientId" })
            }
            .map { createEncounterItem(it.resource, getApplication<Application>().resources) }
            .let { encounters.addAll(it) }

        return encounters
    }

    private suspend fun getObservationsById(encounterId: String): List<ObservationListItem> {
        val observations: MutableList<ObservationListItem> = mutableListOf()
        fhirEngine
            .search<Observation> {
                filter(Observation.ENCOUNTER, { value = "Encounter/$encounterId" })
            }
            .map { createObservationItem(it.resource, getApplication<Application>().resources) }
            .let { observations.addAll(it) }

        return observations
    }

    private fun createEncounterItem( encounter: Encounter, resources: Resources): EncounterListItem {
        println("============== Encounter : ${encounter.id}")
        return EncounterListItem(encounter.idElement.idPart)
    }

    private fun createObservationItem( observation: Observation, resources: Resources): ObservationListItem {
        val observationCode = observation.code.text ?: observation.code.codingFirstRep.display

        // Show nothing if no values available for datetime and value quantity.
        val dateTimeString =
            if (observation.hasEffectiveDateTimeType()) {
                observation.effectiveDateTimeType.asStringValue()
            }
            else {
                resources.getText(R.string.message_no_datetime).toString()
            }
        val value =
            if (observation.hasValueQuantity()) {
                observation.valueQuantity.value.toString()
            } else if (observation.hasValueCodeableConcept()) {
//                observation.valueCodeableConcept.coding.firstOrNull()?.display ?: ""
                observation.valueCodeableConcept.text ?: ""
            } else {
                ""
            }
        val valueUnit =
            if (observation.hasValueQuantity()) {
                observation.valueQuantity.unit ?: observation.valueQuantity.code
            } else {
                ""
            }
        val valueString = "$value $valueUnit"

        return ObservationListItem(
            observation.logicalId,
            dateTimeString,
            valueString,
        )
    }

}

data class PatientDetailData (
    val patient: PatientDetailsUiState,
    val encounters: List<EncounterListItem>,
    val observations: List<ObservationListItem>
)
data class ObservationListItem (
    val id: String,
    val effective: String,
    val value: String,
)

data class EncounterListItem (
    val id: String
)


internal fun Patient.toPatientDetailsUiState(): PatientDetailsUiState {
    // Show nothing if no values available for gender and date of birth.
    val patientId = if (hasIdElement()) idElement.idPart else ""
    val name = if (hasName()) name[0].nameAsSingleString else ""
    val gender = if (hasGenderElement()) genderElement.valueAsString else ""
    val dob =
        if (hasBirthDateElement()) {
            LocalDate.parse(birthDateElement.valueAsString.substring(0,10), DateTimeFormatter.ofPattern(
                AppConfigurationHelper.getFormatDatePattern()))
        } else {
            null
        }
    val phone = if (hasTelecom()) telecom[0].value else ""
    val city = if (hasAddress()) address[0].city else ""
    val country = if (hasAddress()) address[0].country else ""

    return PatientDetailsUiState(
        id = patientId,
        name = name,
        gender = gender ?: "",
        dob = dob,
        phone = phone ?: "",
        city = city ?: "",
        country = country ?: ""
    )
}