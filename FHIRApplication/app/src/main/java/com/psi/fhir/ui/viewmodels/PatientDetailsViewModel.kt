package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.logicalId
import com.google.android.fhir.search.search
import com.psi.fhir.FhirApplication
import com.psi.fhir.R
import com.psi.fhir.data.PatientDetailsUiState
import com.psi.fhir.helper.AppConfigurationHelper
import org.hl7.fhir.r4.model.MedicationRequest
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PatientDetailsViewModel (application: Application): AndroidViewModel(application) {

    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    suspend fun getPatientDetails(patientId: String): PatientDetailData {
        return PatientDetailData( patient = getPatient(patientId), observations = getObservations(patientId))
    }

    private suspend fun getPatient(patientId: String): PatientDetailsUiState {
        val patient = fhirEngine.get<Patient>(patientId)
        return patient.toPatientDetailsUiState()
    }


    private suspend fun getObservations(patientId: String): List<ObservationListItem> {
        val observations: MutableList<ObservationListItem> = mutableListOf()
        fhirEngine
            .search<Observation> {
                filter(Observation.SUBJECT, { value = "Patient/$patientId" })
            }
            .map { createObservationItem(it.resource, getApplication<Application>().resources) }
            .let { observations.addAll(it) }

        return observations
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
                observation.valueCodeableConcept.coding.firstOrNull()?.display ?: ""
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
            observationCode,
            dateTimeString,
            valueString,
        )
    }

}

data class PatientDetailData (
    val patient: PatientDetailsUiState,
    val observations: List<ObservationListItem>
)
data class ObservationListItem (
    val id: String,
    val code: String,
    val effective: String,
    val value: String,
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