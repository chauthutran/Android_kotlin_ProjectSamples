package com.psi.fhir.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
//import com.google.android.fhir.sync.CurrentSyncJobStatus
import com.google.android.fhir.sync.FhirSyncWorker
import com.google.android.fhir.sync.PeriodicSyncConfiguration
import com.google.android.fhir.sync.RepeatInterval
import com.google.android.fhir.sync.Sync
import com.google.android.fhir.sync.SyncJobStatus
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.sync.PatientPeriodicSyncWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PatientListViewModel( private val application: Application ): AndroidViewModel(application) {

    private val _pollState = MutableSharedFlow<SyncJobStatus>()
    val pollState = _pollState.asSharedFlow()

    private val _uiState = MutableStateFlow<List<PatientUiState>>(mutableListOf())
    val uiState: StateFlow<List<PatientUiState>> = _uiState.asStateFlow()


    var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    fun performOneTimeSync() {
        viewModelScope.launch {
            Sync.oneTimeSync<PatientPeriodicSyncWorker>(application)
                .shareIn(this, SharingStarted.Eagerly, 10)
                .collect {
                    _pollState.emit(it)
                    searchPatients()
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
    val dob =
        if (hasBirthDateElement()) {
//            LocalDate.parse(birthDateElement.valueAsString, DateTimeFormatter.ISO_DATE)
//            OffsetDateTime.parse(
//                birthDateElement.valueAsString,
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX")
//            )
            LocalDate.parse(birthDateElement.valueAsString.substring(0,10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        } else {
            null
        }
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