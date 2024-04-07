package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.PatientDetailsUiState
import com.psi.fhir.helper.AppConfigurationHelper
import org.hl7.fhir.r4.model.Patient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PatientDetailsViewModel (application: Application): AndroidViewModel(application) {

    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    suspend fun findPatientById(id: String): PatientDetailsUiState {
       val patient = fhirEngine.get<Patient>(id)
       return patient.toPatientDetailsUiState()
    }


}


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