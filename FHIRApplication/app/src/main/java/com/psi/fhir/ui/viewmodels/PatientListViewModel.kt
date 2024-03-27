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

class PatientListViewModel( val application: Application, val fhirEngine: FhirEngine ): ViewModel() {

//    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

//    private val _pollState = MutableSharedFlow<SyncJobStatus>()
//    val pollState: Flow<SyncJobStatus> = _pollState.asSharedFlow()

//    private val _pollState = MutableSharedFlow<CurrentSyncJobStatus>()
//    val pollState: Flow<CurrentSyncJobStatus> = _pollState.asSharedFlow()

    private val _pollState = MutableSharedFlow<SyncJobStatus>()

    private val _uiState = MutableStateFlow<List<PatientUiState>>(mutableListOf())
    val uiState: StateFlow<List<PatientUiState>> = _uiState.asStateFlow()

    init {


        println(" ------ fhirEngine1 : INIT")
//        var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)
        println(" ------ fhirEngine2 : ${fhirEngine}")
//        searchPatients()

//        // oneTimeSync
//        viewModelScope.launch {
////        searchPatients()
//
//            Sync.periodicSync<PatientPeriodicSyncWorker>(
//                application.applicationContext,
//                periodicSyncConfiguration =
//                PeriodicSyncConfiguration(
//                    syncConstraints = Constraints.Builder().build(),
//                    repeat = RepeatInterval(interval = 15, timeUnit = TimeUnit.MINUTES),
//                ),
//            )
//                .shareIn(this, SharingStarted.Eagerly, 10)
////                .collect { _pollState.emit(it) }
//        }
    }

//     suspend fun syncList() {
    fun performOneTimeSync() {
    viewModelScope.launch {
//        searchPatients()
        Sync.oneTimeSync<PatientPeriodicSyncWorker>(application)
//            .shareIn(this, SharingStarted.Eagerly, 10)
            .shareIn(this, SharingStarted.Eagerly, 10)
                .collect {
//                    _pollState.emit(it)
                    searchPatients()
                }
    }



//        return suspendCoroutine { continuation ->
//            fhirEngine.syncDownload(
//                conflictResolver = null,
//                download: suspend () -> Flow<List<Patient>>
//            )

//            // Emit loading state
//            emit(CurrentSyncJobStatus.Enqueued)

//        viewModelScope.launch {
//
//        }
//
//            viewModelScope.launch {
////                try {
//                    // Perform the background task using Sync.oneTimeSync
////                    val result =
//                        Sync.oneTimeSync<PatientPeriodicSyncWorker>(getApplication())
//                        .shareIn(this, SharingStarted.Eagerly, 10)
//                        .collect { _pollState.emit(it) }
//                    // Return the result to the caller
////                    continuation.resume(result)
////                    emit(CurrentSyncJobStatus.Succeeded(data = )
////                } catch (e: Exception) {
////                    // Handle any errors and return an error message
////                    emit(CurrentSyncJobStatus.Error(message = "Error occurred: ${e.message}"))
////                }
////
////                Sync.oneTimeSync<PatientPeriodicSyncWorker>(getApplication())
//////                .shareIn(this, SharingStarted.Eagerly, 10)
////                    // Emits a value to this shared flow, suspending/stopping on buffer overflow.
////                    .collect { _pollState.emit(it) }
////            }
//        }
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