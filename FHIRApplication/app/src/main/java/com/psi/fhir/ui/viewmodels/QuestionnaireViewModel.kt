package com.psi.fhir.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.testing.jsonParser
import com.google.gson.Gson
import com.psi.fhir.di.FhirApplication
import com.psi.fhir.utils.AssestsFile
import com.psi.fhir.utils.JSONUtils
import com.psi.fhir.utils.TransformSupportServices
import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.model.Base
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.IdType
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.StructureMap
import org.hl7.fhir.r4.utils.StructureMapUtilities
import org.hl7.fhir.utilities.npm.FilesystemPackageCacheManager
import org.json.JSONObject
import timber.log.Timber
import java.io.File

class QuestionnaireViewModel ( application: Application) : AndroidViewModel(application){

    private var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

//    private var _questionnaireJson: String? = null
    val questionnaireJson: String
        get() = fetchQuestionnaireJson()

    private fun fetchQuestionnaireJson(): String {
//        _questionnaireJson?.let {
//            return it
//        }
        val _questionnaireJson = AssestsFile.readFileFromAssets( getApplication<Application>(),"new-patient-registration-paginated.json")
//        return _questionnaireJson!!



        val questionnaire = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser().parseResource(_questionnaireJson)
                    as Questionnaire
        val jsonParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        return jsonParser.encodeResourceToString(questionnaire)
    }

    suspend fun saveResources(questionnaireResponse: QuestionnaireResponse) {
        val outputFile =
            File(getApplication<Application>().externalCacheDir, "questionnaireResponse.json")
        outputFile.writeText( FhirContext
                .forCached(FhirVersionEnum.R4)
                .newJsonParser()
                .encodeResourceToString(questionnaireResponse)
        )

        val contextR4 = FhirApplication.contextR4(getApplication<FhirApplication>().applicationContext)
        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities =  org.hl7.fhir.r4.utils.StructureMapUtilities(contextR4, transformSupportServices)

        val structureMap = fhirEngine.get<StructureMap>(IdType("PatientRegistration").idPart)


//        val myStructureMap: StructureMap = contextR4.getStructure("MyStructureMap")

//        val structureMapStr = AssestsFile.readFileFromAssets(getApplication<Application>(), "StructureMap.json")
//        val structureMap =
//            structureMapUtilities.parse(structureMapStr, "Patient Registration")

println("--------- structureMap : ${structureMap}")
//        val targetResource: Base = if (currentTargetResourceType == "Patient") Patient() else Bundle()
        val targetResource: Base =  Bundle()

        val baseElement =
            jsonParser.parseResource(
                QuestionnaireResponse::class.java,
                jsonParser.encodeResourceToString(questionnaireResponse)
            )
        structureMapUtilities.transform(contextR4, baseElement, structureMap, targetResource)

        var gson = Gson()
        var jsonString = gson.toJson(targetResource)
        println( JSONObject(jsonString) )

//        Timber.tag("saveResources").d(targetResource)
        Timber.tag("saveResources").d( "fasdf")

    }

}