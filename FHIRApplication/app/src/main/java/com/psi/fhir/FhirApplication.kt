package com.psi.fhir

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.fhir.DatabaseErrorStrategy
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineConfiguration
import com.google.android.fhir.FhirEngineProvider
//import com.google.android.fhir.datacapture.BuildConfig
import com.google.android.fhir.ServerConfiguration
import com.google.android.fhir.datacapture.DataCaptureConfig
import com.google.android.fhir.datacapture.XFhirQueryResolver
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.Sync
import com.google.android.fhir.sync.remote.HttpLogger
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.sync.PatientPeriodicSyncWorker
import com.psi.fhir.sync.ReferenceUrlResolver

//class FhirApplication :  Application(), DataCaptureConfig.Provider  {

class FhirApplication : Application(), DataCaptureConfig.Provider  {

//    private val BASE_FHIR_URL = "https://hapi.fhir.org/baseR4/"
    private val BASE_FHIR_URL = "http://172.30.1.27:8080/fhir/"
    /**
     * This instantiate of FHIR Engine ensures the FhirEngine instance is only created
     * when it's accessed for the first time, not immediately when the app starts.
     **/
    private val fhirEngine: FhirEngine by lazy { constructFhirEngine() }

    private var dataCaptureConfig: DataCaptureConfig? = null


    override fun onCreate() {
        super.onCreate()

        // Init FHIR engine
        FhirEngineProvider.init(
            FhirEngineConfiguration(
                //Enables data encryption if the device supports it.
                enableEncryptionIfSupported = false,
                // Determines the database error strategy. In this case, it recreates the database if an error occurs upon opening
                DatabaseErrorStrategy.RECREATE_AT_OPEN,
                ServerConfiguration(
                    baseUrl = BASE_FHIR_URL,
                    httpLogger =
                    HttpLogger(
                        HttpLogger.Configuration(
                            if (BuildConfig.DEBUG) HttpLogger.Level.BODY else HttpLogger.Level.BASIC,
                        ),
                    ) {
                        Log.d("App-HttpLog", it)
                    },
//                    networkConfiguration = NetworkConfiguration(uploadWithGzip = false),
//                    authenticator = { HttpAuthenticationMethod.Bearer("mySecureToken") }
//                    authenticator =
//                        object : Authenticator {
//                            override fun getAccessToken(): String = runBlocking {
//                                return@runBlocking dataStore.getUserIdToken() ?: ""
//                            }
//                        }
                ),
            ),
        )

        Sync.oneTimeSync<PatientPeriodicSyncWorker>(this)

        // Read and load the config-file,we will use to draw the UI later
        AppConfigurationHelper.readConfiguration(this)
        
        dataCaptureConfig =
            DataCaptureConfig().apply {
                urlResolver = ReferenceUrlResolver(this@FhirApplication as Context)
                xFhirQueryResolver = XFhirQueryResolver { fhirEngine.search(it).map { it.resource } }
            }
    }


    override fun getDataCaptureConfig(): DataCaptureConfig = dataCaptureConfig ?: DataCaptureConfig()

    private fun constructFhirEngine(): FhirEngine {
        return FhirEngineProvider.getInstance(this)
    }


    companion object {
        fun fhirEngine(context: Context): FhirEngine {
           return (context.applicationContext as FhirApplication).fhirEngine
        }

    }
}