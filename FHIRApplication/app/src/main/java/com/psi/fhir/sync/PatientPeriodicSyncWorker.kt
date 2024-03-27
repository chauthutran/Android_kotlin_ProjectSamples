package com.psi.fhir.sync


import android.content.Context
import androidx.compose.ui.text.style.LineBreak
import androidx.work.WorkerParameters
import com.google.android.fhir.sync.AcceptLocalConflictResolver
import com.google.android.fhir.sync.FhirSyncWorker
//import com.google.android.fhir.sync.Strategy
import com.psi.fhir.FhirApplication

/**
 * defines how the app will sync with the remote FHIR server using a background worker.
 * **/
class PatientPeriodicSyncWorker(appContext: Context, workerParams: WorkerParameters) :
    FhirSyncWorker(appContext, workerParams) {

    override fun getDownloadWorkManager() = DownloadPatientWorkManagerImpl()

//    override fun getUploadStrategy(): Strategy {
//        return if (isUploadEnabled()) {
//            LineBreak.Strategy.FORCE_UPLOAD
//        } else {
//            LineBreak.Strategy.SKIP
//        }
//    }
//
//    private fun isUploadEnabled(): Boolean {
//        // Your logic to determine if upload is enabled goes here
//        // For example, you can check a user preference or some application setting
//        return true
//    }

    override fun getConflictResolver() = AcceptLocalConflictResolver

    override fun getFhirEngine() = FhirApplication.fhirEngine(applicationContext)
}