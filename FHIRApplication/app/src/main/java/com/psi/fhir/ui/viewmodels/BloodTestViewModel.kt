package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import com.google.android.fhir.get
import com.psi.fhir.FhirApplication
import com.psi.fhir.di.TransformSupportServices
import com.psi.fhir.utils.DispatcherStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.Meta
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.StructureMap
import org.hl7.fhir.r4.model.Task
import org.hl7.fhir.r4.utils.StructureMapUtilities
import org.hl7.fhir.utilities.npm.FilesystemPackageCacheManager
import java.util.Date
import java.util.UUID

class BloodTestViewModel( application: Application) : AndroidViewModel(application){

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)


    private var questionnaire: Questionnaire? = null
    lateinit var questionnaireJson: String

    var isResourceSaved = MutableLiveData<Boolean>()

    private var _isQuestionnaireLoaded =
        MutableStateFlow<DispatcherStatus<Boolean>>(DispatcherStatus.UnSpecified())
    val questionnaireLoaded = _isQuestionnaireLoaded.asStateFlow()

//    private val questionnaireResource: Questionnaire
//        get() =
//            FhirContext.forCached(FhirVersionEnum.R4).newJsonParser().parseResource(questionnaire)
//                    as Questionnaire


    init {
        viewModelScope.launch {
            fetchQuestionnaireJson()
        }
    }

    private suspend fun fetchQuestionnaireJson() {
        questionnaire = fhirEngine.get<Questionnaire>("3")
        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        questionnaireJson = jsonParser.encodeResourceToString(questionnaire)

        viewModelScope.launch { _isQuestionnaireLoaded.emit(DispatcherStatus.Success(true)) }
    }


    fun createObservation(questionnaireResponse: QuestionnaireResponse, task: Task) {
        viewModelScope.launch {

            val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
            println(
                "------------- questionnaireResponse : ${
                    iParser.encodeResourceToString(
                        questionnaireResponse
                    )
                }"
            )

//            val bundle = ResourceMapper.extract(questionnaire!!, questionnaireResponse)
//            println("------------- Task : ${iParser.encodeResourceToString(bundle)}")
//
//            val entry = bundle.entryFirstRep
            val targetResource = transformResource(questionnaireResponse)
            if (targetResource is Bundle) {
                targetResource.entry.forEach { bundleEntryComponent ->
                    val resource = bundleEntryComponent.resource
                    if ( resource is Observation ) {
                        resource.subject.reference = task.`for`.reference
                        setLastUpdate(resource)
            println("============ Observation: ${iParser.encodeResourceToString(resource)}")
                        fhirEngine.create(resource)

                        task.status = Task.TaskStatus.COMPLETED
                        fhirEngine.update(task)
        println("============ task: ${iParser.encodeResourceToString(task)}")
                    }
                }

//                {
//                    var obs: Observation = entry.resource as Observation
//                    obs.id = UUID.randomUUID().toString()
//                    fhirEngine.create(obs)
//
//                    task.status = Task.TaskStatus.COMPLETED
//                    fhirEngine.update(task)
//
//                    println("------------- Task : ${iParser.encodeResourceToString(task)}")
//
//                    isResourceSaved.value = true
//                }
            }
        }
    }


    // ---------------------------------------------------------------------------------------------
    // Supportive methods

    private fun setLastUpdate( resource: Resource){
        if( resource.hasMeta() ) {
            resource.meta.lastUpdated = Date()
        }
        else {
            var meta = Meta()
            meta.lastUpdated = Date()
            resource.setMeta(meta)
        }
    }

    private suspend fun transformResource (questionnaireResponse: QuestionnaireResponse): Bundle {
        val contextR4 =
            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
//        contextR4.isCanRunWithoutTerminology = true

        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities = StructureMapUtilities(contextR4, transformSupportServices)

        var structureMap = fhirEngine.get<StructureMap>("176")

//        val targetResource = Bundle()
//        structureMapUtilities.transform(
//            contextR4,
//            questionnaireResponse,
//            structureMap,
//            targetResource
//        )
        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val targetResource = Bundle()
        val baseElement =
            iParser.parseResource(
                QuestionnaireResponse::class.java,
                iParser.encodeResourceToString(questionnaireResponse),
            )

        structureMapUtilities.transform(contextR4, baseElement, structureMap, targetResource)

        return targetResource


//
////        val pcm = FilesystemPackageCacheManager(false)
////
////        // Create R4 context
////        val contextR4 =
////            SimpleWorkerContext.fromPackage(pcm.loadPackage("hl7.fhir.r4.core", "4.0.1"))
////        contextR4.isCanRunWithoutTerminology = true
//        val contextR4 =
//            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
//        var structureMap = fhirEngine.get<StructureMap>("176")
//
//        // Create an instance of StructureMapUtilities to use R4 context
//        val structureMapUtilities = StructureMapUtilities(contextR4)
//
//        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
//
//
//        val targetResource = Bundle()
//        val baseElement =
//            iParser.parseResource(
//                QuestionnaireResponse::class.java,
//                iParser.encodeResourceToString(questionnaireResponse),
//            )
//
//        structureMapUtilities.transform(contextR4, baseElement, structureMap, targetResource)
//println(iParser.encodeResourceToString( targetResource))
//        return targetResource
    }

}