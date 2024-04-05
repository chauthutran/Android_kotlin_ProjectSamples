package com.psi.fhir.di

import android.app.Application
import android.content.Context
import com.google.android.fhir.DatabaseErrorStrategy
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineConfiguration
import com.google.android.fhir.FhirEngineProvider
import com.google.android.fhir.ServerConfiguration
import com.google.android.fhir.datacapture.DataCaptureConfig
import com.google.android.fhir.datacapture.XFhirQueryResolver
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.remote.HttpLogger
import com.psi.fhir.BuildConfig
import com.psi.fhir.helper.AppConfigurationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.utilities.npm.NpmPackage
import timber.log.Timber
import java.io.File


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
//    private lateinit var contextR4: ComplexWorkerContext
    private lateinit var contextR4: ComplexWorkerContext


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
                        Timber.tag("App-HttpLog").d(it)
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


        constructR4Context()
//        Sync.oneTimeSync<PatientPeriodicSyncWorker>(this)

        // Read and load the config-file,we will use to draw the UI later
        AppConfigurationHelper.readConfiguration(this)

        dataCaptureConfig =
            DataCaptureConfig().apply {
                urlResolver = ReferenceUrlResolver(this@FhirApplication as Context)
                valueSetResolverExternal = object : ValueSetResolver() {}
                xFhirQueryResolver = XFhirQueryResolver { fhirEngine.search(it).map { it.resource } }
            }
    }

//    private fun constructR4Context() {
    private fun constructR4Context() =
        CoroutineScope(Dispatchers.IO).launch {
            println("**** creating contextR4")




            val measlesIg = async {
                NpmPackage.fromPackage(assets.open("packages.fhir.org-hl7.fhir.dk.core-1.1.0.tgz"))
            }


            val baseIg = async { NpmPackage.fromPackage(assets.open("packages.tgz")) }
//            val baseIg = async { NpmPackage.fromFolder("contextR4/") }

//            val packages = arrayListOf<NpmPackage>(measlesIg.await())
            val packages = arrayListOf<NpmPackage>(measlesIg.await(), baseIg.await())

            println("**** read assets contextR4")
            contextR4 = ComplexWorkerContext()
            contextR4?.apply {
                setExpansionProfile(Parameters())
                isCanRunWithoutTerminology = true


                loadFromMultiplePackages(packages, true)
                println("**** created contextR4")
                ValueSetResolver.init(this@FhirApplication, this)
            }

//         val packageCacheManager = FilesystemPackageCacheManager(true)
//         contextR4 = SimpleWorkerContext.fromPackage(packageCacheManager.loadPackage("hl7.fhir.r4.core", "4.0.1"))
//                .apply {
//                    setExpansionProfile(Parameters())
//                    isCanRunWithoutTerminology = true
////                    ValueSetResolver.init(this@FhirApplication, this)
//                }
        }


//    private fun constructR4Context () {
//        val packageCacheManager = FilesystemPackageCacheManager(false)
//
//        contextR4 = SimpleWorkerContext.fromPackage(
//            packageCacheManager.loadPackage(
//                "hl7.fhir.r4.core",
//                "4.0.1"
//            )
//        ).apply {
//            setExpansionProfile(Parameters())
//            isCanRunWithoutTerminology = true
////            ValueSetResolver.init(this@FhirApplication, this)
//        }
//    }

    override fun getDataCaptureConfig(): DataCaptureConfig = dataCaptureConfig ?: DataCaptureConfig()

    private fun constructFhirEngine(): FhirEngine {
        return FhirEngineProvider.getInstance(this)
    }


    companion object {
        fun fhirEngine(context: Context): FhirEngine {
           return (context.applicationContext as FhirApplication).fhirEngine
        }

        fun contextR4(context: Context) = (context.applicationContext as FhirApplication).contextR4

    }
}