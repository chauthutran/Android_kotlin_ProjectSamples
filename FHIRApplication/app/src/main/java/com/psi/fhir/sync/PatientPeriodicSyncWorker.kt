package com.psi.fhir.sync


import android.content.Context
import androidx.work.WorkerParameters
import com.google.android.fhir.sync.AcceptLocalConflictResolver
import com.google.android.fhir.sync.FhirSyncWorker
import com.google.android.fhir.sync.upload.UploadStrategy
import com.psi.fhir.FhirApplication

/**
 * defines how the app will sync with the remote FHIR server using a background worker.
 * **/
class PatientPeriodicSyncWorker(appContext: Context, workerParams: WorkerParameters) :
    FhirSyncWorker(appContext, workerParams) {

    override fun getDownloadWorkManager() = DownloadPatientWorkManagerImpl()

    override fun getUploadStrategy(): UploadStrategy {
        TODO("Not yet implemented")
    }

    override fun getConflictResolver() = AcceptLocalConflictResolver

    override fun getFhirEngine() = FhirApplication.fhirEngine(applicationContext)
}