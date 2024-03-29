package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.Sync
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.sync.PatientPeriodicSyncWorker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient

enum class SyncDataStatus {
    UNDEFINED,
    LOADING,
    SUCCESS,
    ERROR
}

class PatientListViewModel( private val application: Application ): AndroidViewModel(application) {

    private val _pollState = MutableStateFlow<SyncDataStatus>(SyncDataStatus.UNDEFINED)
    val pollState = _pollState.asStateFlow()

    private val _uiState = MutableStateFlow<List<PatientUiState>>(mutableListOf())
    val uiState: StateFlow<List<PatientUiState>> = _uiState.asStateFlow()


    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    fun performSyncData() {
        viewModelScope.launch {
            _pollState.emit(SyncDataStatus.LOADING)

            try {
                Sync.oneTimeSync<PatientPeriodicSyncWorker>(application)
                    .shareIn(this, SharingStarted.Eagerly, 10)
                    .collect {
                        _pollState.emit(SyncDataStatus.SUCCESS)
                        searchPatients()
                    }
            }
            catch(e: Exception) {
                _pollState.emit(SyncDataStatus.ERROR)
            }

        }

    }


    fun searchPatients(nameQuery: String = "") {
        updatePatientList({ searchPatientsByName(nameQuery) })
    }

    private fun updatePatientList (
        getList: suspend () -> List<PatientUiState>,
//        getCount: suspend() -> Long
    ) {
        viewModelScope.launch {
            _uiState.value = getList()
//            livePatientCount.value = getCount()
        }
    }

    private suspend fun searchPatientsByName(nameQuery: String = ""): List<PatientUiState> {
        val patients: MutableList<PatientUiState> = mutableListOf()

        var searchResult = fhirEngine.search<Patient> {
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
            count = 20
            from = 0
        }
            .mapIndexed { index, fhirPatient -> fhirPatient.resource.toPatientItem(index + 1) }
            .let { patients.addAll(it) }

        return patients
    }

}

internal fun Patient.toPatientItem(position: Int): PatientUiState {
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
    val html: String = if (hasText()) text.div.valueAsString else ""

    return PatientUiState(
        id = position.toString(),
        resourceId = patientId,
        name = name,
        gender = gender ?: "",
        dob = dob,
        phone = phone ?: "",
        city = city ?: "",
        country = country ?: "",
        isActive = isActive
    )
}