package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.extensions.targetStructureMap
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import com.google.android.fhir.get
import com.google.android.fhir.testing.jsonParser
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.RequestResult
import com.psi.fhir.di.TransformSupportServices
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.utils.ProcessStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.IdType
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType
import org.hl7.fhir.r4.utils.StructureMapUtilities
import timber.log.Timber
import java.util.UUID


class PatientRegistrationViewModel (application: Application) : AndroidViewModel(application) {

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    val questionnaireJson: String
        get() = fetchQuestionnaireJson()

    private var questionnaire: Questionnaire? = null

    private var _resourceSaved =
        MutableStateFlow<ProcessStatus<Boolean>>(ProcessStatus.UnSpecified())
    val resourceSaved = _resourceSaved.asStateFlow()

    private fun fetchQuestionnaireJson(): String {
        val questionnaireId = AppConfigurationHelper.getPatientRegistrationQuestionnaire()
        val contextR4 =
            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        questionnaire = contextR4.searchResourceById(questionnaireId!!)

        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        return jsonParser.encodeResourceToString(questionnaire)
    }

    fun addResources(questionnaireResponse: QuestionnaireResponse) {

        viewModelScope.launch {
            val targetResource = transformResource(questionnaireResponse)

            if (targetResource is Bundle) {
                targetResource.entry.forEach { bundleEntryComponent ->
                    // Save Resource
                    val resource = bundleEntryComponent.resource
                    if (resource is Observation && resource.effective is DateType) {
                        resource.effective = null
                    }
                    if (resource.id == null) {
                        resource.id = UUID.randomUUID().toString()
                    }

                    fhirEngine.create(resource)


                    Timber.tag("saveResources").d("Resource ${resource.resourceType} is saved")
                }
            }
        }

//            else if (targetResource is Resource) {
//                // Save Resource
//                if (targetResource.id == null) {
//                    targetResource.id = UUID.randomUUID().toString()
//                }
//                fhirEngine.create(targetResource)
//
//                // Save QuestionnaireResponse
//                questionnaireResponse.id = UUID.randomUUID().toString()
//                questionnaireResponse.subject =
//                    Reference("${targetResource.resourceType}/${IdType(targetResource.id).idPart}")
//                fhirEngine.create(questionnaireResponse)
//            }

            viewModelScope.launch { _resourceSaved.emit(ProcessStatus.Success(true)) }
//        }
    }

    // ---------------------------------------------------------------------------------------------
    // For Update resources

    suspend fun updateResources( questionnaireResponse: QuestionnaireResponse, patientDetailData: PatientDetailData): RequestResult {

        val targetResource = transformResource (questionnaireResponse)

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

                // Put IDs for resources
                for (resourceType in updatedList.keys) {
                    var list = updatedList[resourceType]!!
                    when (resourceType) {
                        ResourceType.Patient.toString() -> {
                            val resource = list[0]
                            resource.id = patientDetailData.patient.id
                            fhirEngine.update(resource)
                        }

                        ResourceType.Encounter.toString() -> {
                            (0..<list.size).forEach {
                                val resource = list[it]
                                resource.id = patientDetailData.encounters[it].id
                                fhirEngine.update(resource)
                            }
                        }

                        ResourceType.Observation.toString() -> {
                            (0..<list.size).forEach {
                                val resource = list[it]
                                resource.id = patientDetailData.observations[it].id
                                fhirEngine.update(resource)
                            }
                        }

                        else -> {
                            Unit
                        }
                    }
                }

            }

        }

        return RequestResult(true)
    }

    suspend fun populateData(patientId: String): Pair<String, String> {
        val patient = fhirEngine.get<Patient>(patientId)
        val launchContexts = mapOf<String, Resource>("client" to patient)

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val questionnaireStr = iParser.encodeResourceToString(questionnaire)

        val questionnaireResponse: QuestionnaireResponse =
            ResourceMapper.populate(questionnaire!!, launchContexts)
        val questionnaireResponseJson = iParser.encodeResourceToString(questionnaireResponse)
        return questionnaireStr to questionnaireResponseJson
    }

//    private suspend fun prepareEditPatient(): Pair<String, String> {
//        val patient = fhirEngine.get<Patient>(patientId)
//        val launchContexts = mapOf<String, Resource>("client" to patient)
//        val question = readFileFromAssets("new-patient-registration-paginated.json").trimIndent()
//        val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
//        val questionnaire = parser.parseResource(Questionnaire::class.java, question) as Questionnaire
//
//        val questionnaireResponse: QuestionnaireResponse =
//            ResourceMapper.populate(questionnaire, launchContexts)
//        val questionnaireResponseJson = parser.encodeResourceToString(questionnaireResponse)
//        return question to questionnaireResponseJson
//    }


    // ---------------------------------------------------------------------------------------------
    // Supportive methods

    private fun transformResource (questionnaireResponse: QuestionnaireResponse): Bundle {

//        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
//        val questionnaireResponseStr = iParser.encodeResourceToString(questionnaireResponse)
//        println("QR: ${jsonParser.encodeResourceToString(questionnaireResponse)}")

        val contextR4 =
            FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities = StructureMapUtilities(contextR4, transformSupportServices)

        val structureMapStr = contextR4.getTransform(questionnaire!!.targetStructureMap)
        val targetResource = Bundle()
        structureMapUtilities.transform(
            contextR4,
            questionnaireResponse,
            structureMapStr,
            targetResource
        )

        return targetResource
    }


}

