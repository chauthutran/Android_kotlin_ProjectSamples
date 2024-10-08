package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.extensions.targetStructureMap
import com.google.android.fhir.get
import com.google.android.fhir.search.search
import com.psi.fhir.FhirApplication
import com.psi.fhir.careplan.CarePlanManager
import com.psi.fhir.data.RequestResult
import com.psi.fhir.di.TransformSupportServices
import com.psi.fhir.helper.app.AppConfigurationHelper
import com.psi.fhir.utils.DispatcherStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.Meta
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType
import org.hl7.fhir.r4.model.StructureMap
import org.hl7.fhir.r4.utils.StructureMapUtilities
import timber.log.Timber
import java.util.Date
import java.util.UUID


class QuestionnaireViewModel (application: Application) : AndroidViewModel(application) {

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)
    private var carePlanManager: CarePlanManager = FhirApplication.carePlanManager(application.applicationContext)

    lateinit var questionnaireJson: String
    private var questionnaire: Questionnaire? = null

    private var _formFetched =
        MutableStateFlow<DispatcherStatus<Boolean>>(DispatcherStatus.UnSpecified())
    val formFetched = _formFetched.asStateFlow()

    private var _resourceSaved =
        MutableStateFlow<DispatcherStatus<Boolean>>(DispatcherStatus.UnSpecified())
    val resourceSaved = _resourceSaved.asStateFlow()

    init {
        viewModelScope.launch {
            fetchQuestionnaireJson()
        }
    }

    private suspend fun fetchQuestionnaireJson() {
        questionnaire = fhirEngine.get<Questionnaire>("2")
        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        questionnaireJson = jsonParser.encodeResourceToString(questionnaire)

        viewModelScope.launch { _formFetched.emit(DispatcherStatus.Success(true)) }
    }

    fun addPatient(questionnaireResponse: QuestionnaireResponse) {

        viewModelScope.launch {
            val targetResource = transformResource(questionnaireResponse)

            var patientId = "";
            if (targetResource is Bundle) {
                targetResource.entry.forEach { bundleEntryComponent ->
                    // Save Resource
                    val resource = bundleEntryComponent.resource
                    if( resource is Patient ) {
                        patientId = resource.id
                    }
                    if (resource is Observation && resource.effective is DateType) {
                        resource.effective = null
                    }
                    if (resource.id == null) {
                        resource.id = UUID.randomUUID().toString()
                    }

                    setLastUpdate(resource)
                    fhirEngine.create(resource)

                    Timber.tag("saveResources").d("Resource ${resource.resourceType} is saved")
                }

                try {
                    // Save QuestionnaireResponse
                    questionnaireResponse.id = UUID.randomUUID().toString()
                    questionnaireResponse.subject =
                        Reference("${ResourceType.Patient.name}/${patientId}")
                    fhirEngine.create(questionnaireResponse)

                    // Create CarePlan
                    CoroutineScope(Dispatchers.IO).launch {
                        carePlanManager.createCarePlan(patientId, "")
                    }

                    viewModelScope.launch { _resourceSaved.emit(DispatcherStatus.Success(data = true)) }
                }
                catch( ex: Exception) {
                    ex.printStackTrace()
                    viewModelScope.launch { _resourceSaved.emit(DispatcherStatus.Error(ex.message?: "There are some error while saving resources.")) }
                }

            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // For Update resources

    fun updatePatient(questionnaireResponse: QuestionnaireResponse, patientDetailData: PatientDetailData) {
        viewModelScope.launch {

            val targetResource = transformResource(questionnaireResponse)

            var updatedList: MutableMap<String, ArrayList<Resource>> = mutableMapOf()
            if (targetResource is Bundle) {
                targetResource.entry.forEach { bundleEntryComponent ->
                    // Save Resource
                    val resource = bundleEntryComponent.resource
                    val resourceType = resource.resourceType.toString()
                    if (updatedList[resourceType] == null) {
                        val list = ArrayList<Resource>()
                        list.add(resource)
                        updatedList[resourceType] = list
                    } else {
                        updatedList[resourceType]!!.add(resource)
                    }
                }

                // Put IDs for resources
                for (resourceType in updatedList.keys) {
                    var list = updatedList[resourceType]!!
                    when (resourceType) {
                        ResourceType.Patient.toString() -> {
                            val resource = list[0]
                            setLastUpdate(resource)
                            resource.id = patientDetailData.patient.id
                            fhirEngine.update(resource)
                        }

                        else -> {
                            Unit
                        }
                    }
                }
            }

            fhirEngine.update(questionnaireResponse)

            viewModelScope.launch { _resourceSaved.emit(DispatcherStatus.Success(true)) }
        }
    }


    private fun setLastUpdate( resource: Resource ){
        if( resource.hasMeta() ) {
            resource.meta.lastUpdated = Date()
        }
        else {
            var meta = Meta()
            meta.lastUpdated = Date()
            resource.setMeta(meta)
        }
    }

    suspend fun populateData(patientId: String): Pair<String, String> {

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val questionnaireStr = iParser.encodeResourceToString(questionnaire)

        val foundQR = fhirEngine
            .search<QuestionnaireResponse> {
                filter(QuestionnaireResponse.SUBJECT, { value = "Patient/$patientId" })
            }

        if(foundQR.isNotEmpty()) {
            return questionnaireStr to iParser.encodeResourceToString(foundQR[0].resource)
        }

        return "" to ""

//        val questionnaireResponse: QuestionnaireResponse =
//            ResourceMapper.populate(questionnaire!!, launchContexts)

//        val questionnaireResponseJson = iParser.encodeResourceToString(questionnaireResponse)
//        val fasdf = questionnaireStr to questionnaireResponseJson
//        println("====================== ")
//        println(questionnaireResponseJson)
//        return fasdf
    }


    // ---------------------------------------------------------------------------------------------
    // Supportive methods

    private suspend fun transformResource (questionnaireResponse: QuestionnaireResponse): Bundle {
//        val contextR4 =
//            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
//        val transformSupportServices = TransformSupportServices(contextR4)
//
//        return ResourceMapper.extract(
//            questionnaire = questionnaire!!,
//            questionnaireResponse = questionnaireResponse,
//            structureMapExtractionContext =
//                StructureMapExtractionContext(
//                    context = getApplication<Application>().applicationContext,
//                    transformSupportServices = transformSupportServices,
//                    structureMapProvider = { structureMapUrl: String?, _: IWorkerContext ->
//    //                    structureMapUrl?.substringAfterLast("/")?.let {
//    //                //                        defaultRepository.loadResource(it)
//    //                        fhirEngine.get(ResourceType.StructureMap, questionnaire!!.targetStructureMap!!)
//    //                    } as StructureMap?
//
//                        contextR4.getTransform(questionnaire!!.targetStructureMap)
//                    },
//            ),
//        )

//        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
//        val questionnaireResponseStr = iParser.encodeResourceToString(questionnaireResponse)
//        println("QR: ${jsonParser.encodeResourceToString(questionnaireResponse)}")

        val contextR4 =
            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities = StructureMapUtilities(contextR4, transformSupportServices)

        var structureMap = fhirEngine.get<StructureMap>("5")
//        val structureMapStr = contextR4.getTransform(questionnaire!!.targetStructureMap)

        val targetResource = Bundle()
        structureMapUtilities.transform(
            contextR4,
            questionnaireResponse,
            structureMap,
            targetResource
        )

        return targetResource
    }


}

