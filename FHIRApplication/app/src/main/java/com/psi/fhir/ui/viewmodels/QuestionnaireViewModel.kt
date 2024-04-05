package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.datacapture.extensions.targetStructureMap
import com.psi.fhir.FhirApplication
import com.psi.fhir.di.TransformSupportServices
import com.psi.fhir.utils.AssestsFile
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.utils.StructureMapUtilities
import timber.log.Timber


class QuestionnaireViewModel ( application: Application) : AndroidViewModel(application){

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

    val questionnaireJson: String
        get() = fetchQuestionnaireJson()

    private var questionnaire: Questionnaire? = null


    private fun fetchQuestionnaireJson(): String {
        val _questionnaireJson = AssestsFile.readFileFromAssets( getApplication<Application>(),"new-patient-registration-paginated.json")

        questionnaire = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser().parseResource(_questionnaireJson) as Questionnaire
        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        return jsonParser.encodeResourceToString(questionnaire)
    }

    suspend fun saveResources(questionnaireResponse: QuestionnaireResponse) {

        val contextR4 = FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities = StructureMapUtilities(contextR4, transformSupportServices)

        val structureMapStr = contextR4.getTransform(questionnaire!!.targetStructureMap)
        val targetResource = Bundle()
        structureMapUtilities.transform(contextR4, questionnaireResponse, structureMapStr, targetResource)

        targetResource.entry.forEach { bundleEntryComponent ->
            val resource = bundleEntryComponent.resource
            fhirEngine.create(resource)
            Timber.tag("saveResources").d( "Resource ${resource.resourceType} is saved")
        }


    }

}