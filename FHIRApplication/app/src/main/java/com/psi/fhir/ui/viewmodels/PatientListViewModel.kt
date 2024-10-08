package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.PeriodicSyncJobStatus
import com.google.android.fhir.sync.Sync
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.PatientListItemUiState
import com.psi.fhir.data.RequestResult
import com.psi.fhir.sync.FhirPeriodicSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire

enum class SyncDataStatus {
    UNDEFINED,
    LOADING,
    SUCCESS,
    ERROR
}

class PatientListViewModel( private val application: Application ): AndroidViewModel(application) {

    private val _pollState = MutableStateFlow(SyncDataStatus.UNDEFINED)
    val pollState = _pollState.asStateFlow()

    private val _uiState = MutableStateFlow<List<PatientListItemUiState>>(mutableListOf())
    val uiState: StateFlow<List<PatientListItemUiState>> = _uiState.asStateFlow()


    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)


    fun performSyncData() {

        viewModelScope.launch {
            _pollState.emit(SyncDataStatus.LOADING)
            try {
                Sync.oneTimeSync<FhirPeriodicSyncWorker>(application)
//                    .shareIn(this, SharingStarted.Eagerly, 10)
                    .shareIn(this, SharingStarted.Eagerly, 1)
                    .collectLatest {
                        searchPatientsByName("")
                        _pollState.emit(SyncDataStatus.SUCCESS)
                    }
            } catch (e: Exception) {
                _pollState.emit(SyncDataStatus.ERROR)
            }
        }

    }

    suspend fun searchPatientsByName(nameQuery: String = ""): List<PatientListItemUiState> {

        val patients: MutableList<PatientListItemUiState> = mutableListOf()
        fhirEngine.search<Patient> {
            if (nameQuery.isNotEmpty()) {
                filter(
                    Patient.NAME,
                    {
                        modifier = StringFilterModifier.CONTAINS
                        value = nameQuery
                    },
                )
            }
            sort(Patient.GIVEN, Order.ASCENDING)
            count = 100
            from = 0
        }
            .mapIndexed { index, fhirPatient -> fhirPatient.resource.toPatientItem() }
            .let { patients.addAll(it) }

        return patients
    }

}

internal fun Patient.toPatientItem(): PatientListItemUiState {
    Log.d("Patient.toPatientItem", "$idElement" )

    // Show nothing if no values available for gender and date of birth.
    val patientId = if (hasIdElement()) idElement.idPart else ""
    val name = if (hasName()) name[0].nameAsSingleString else ""
    val gender = if (hasGenderElement()) genderElement.valueAsString else ""
//    val dob =
//        if (hasBirthDateElement()) {
////            LocalDate.parse(birthDateElement.valueAsString, DateTimeFormatter.ISO_DATE)
////            OffsetDateTime.parse(
////                birthDateElement.valueAsString,
////                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX")
////            )
//            LocalDate.parse(birthDateElement.valueAsString.substring(0,10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//
//        } else {
//            null
//        }
    val dob = birthDateElement.valueAsString.substring(0,10)
    val phone = if (hasTelecom()) telecom[0].value else ""
    val city = if (hasAddress()) address[0].city else ""
    val country = if (hasAddress()) address[0].country else ""
    val isActive = active

    return PatientListItemUiState(
        id = patientId,
        name = name,
        gender = gender ?: "",
        dob = dob,
        phone = phone ?: "",
        city = city ?: "",
        country = country ?: "",
        isActive = isActive
    )
}

