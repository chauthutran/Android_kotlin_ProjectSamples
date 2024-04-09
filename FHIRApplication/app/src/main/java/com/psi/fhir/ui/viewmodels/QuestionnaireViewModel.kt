package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.extensions.targetStructureMap
import com.google.android.fhir.testing.jsonParser
import com.psi.fhir.FhirApplication
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
import org.hl7.fhir.r4.utils.StructureMapUtilities
import timber.log.Timber
import java.util.Timer
import java.util.UUID


class QuestionnaireViewModel ( application: Application) : AndroidViewModel(application){

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    val questionnaireJson: String
        get() = fetchQuestionnaireJson()

    private var questionnaire: Questionnaire? = null

    private var _resourceSaved = MutableStateFlow<ProcessStatus<Boolean>>(ProcessStatus.UnSpecified())
    val resourceSaved = _resourceSaved.asStateFlow()

    private fun fetchQuestionnaireJson(): String {
        val questionnaireId = AppConfigurationHelper.getPatientRegistrationQuestionnaire()
        val contextR4 = FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        questionnaire = contextR4.searchResourceById(questionnaireId!!)

        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        return jsonParser.encodeResourceToString(questionnaire)
    }

    fun saveResources(questionnaireResponse: QuestionnaireResponse) {

//        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
//        val questionnaireResponseStr = iParser.encodeResourceToString(questionnaireResponse)


        viewModelScope.launch{ _resourceSaved.emit(ProcessStatus.Loading()) }
        viewModelScope.launch {
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
            else if (targetResource is Resource) {
                // Save Resource
                if (targetResource.id == null) {
                    targetResource.id = UUID.randomUUID().toString()
                }
                fhirEngine.create(targetResource)

                // Save QuestionnaireResponse
                questionnaireResponse.id = UUID.randomUUID().toString()
                questionnaireResponse.subject =
                    Reference("${targetResource.resourceType}/${IdType(targetResource.id).idPart}")
//                println("QR: ${jsonParser.encodeResourceToString(questionnaireResponse)}")
                fhirEngine.create(questionnaireResponse)

//                createImmunizationReviewTask(targetResource.id)

//                savedPatient.value = if (targetResource is Patient) targetResource else null
            }

            viewModelScope.launch { _resourceSaved.emit(ProcessStatus.Success(true)) }
        }
    }

}