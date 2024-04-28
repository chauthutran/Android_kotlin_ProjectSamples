package com.psi.fhir.careplan

import android.content.Context
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.knowledge.FhirNpmPackage
import com.google.android.fhir.knowledge.KnowledgeManager
import com.google.android.fhir.workflow.FhirOperator
import com.google.android.fhir.workflow.FhirOperator.Builder
import com.psi.fhir.FhirApplication
import com.psi.fhir.helper.app.AppConfigurationHelper
import org.hl7.fhir.r4.model.ActivityDefinition
import org.hl7.fhir.r4.model.CanonicalType
import org.hl7.fhir.r4.model.CarePlan
import org.hl7.fhir.r4.model.Meta
import org.hl7.fhir.r4.model.MetadataResource
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.PlanDefinition
import org.hl7.fhir.r4.model.Resource
import java.io.File
import java.util.Date
import java.util.UUID

/** The care plan creator interface allows healthcare providers to easily create, update, and manage
care plans for patients. It provides a user-friendly way to input information such as goals, interventions, and timelines.
**/

class CarePlanManager (
    private var fhirEngine: FhirEngine,
    private var fhirContext: FhirContext,
    private var context: Context
){

    private var knowledgeManager = KnowledgeManager.create(context, inMemory = true)

    private val fhirOperator = Builder(context.applicationContext)
    .fhirContext(fhirContext)
    .fhirEngine(fhirEngine)
    .knowledgeManager(knowledgeManager)
    .build()


    suspend fun setKnowledgeResouces() {
//        val rootDirectory = File(javaClass.getResource("/package")!!.file)
//        knowledgeManager.install(
//            FhirNpmPackage(
//                "com.google.android.fhir",
//                "1.0.0",
//                "http://github.com/google/android-fhir",
//            ),
//            rootDirectory,
//        )

//        val fileList = context.assets.list("/package")
//        if (fileList != null) {
//            for (filename in fileList) {
//                if (filename.contains(".json")) {
//                    val contents = AssestsFile(context, "$path/$filename")
//            fhirEngine.create(resource)
//        }


        val planDefinitionId = AppConfigurationHelper.getPlanDefinitionId()!!
        var planDefinition = fhirEngine.get<PlanDefinition>(planDefinitionId)
        knowledgeManager.install(writeToFile(planDefinition))

        val activityDefinitionId = AppConfigurationHelper.getActivityDefinitionId()!!
        var activityDefinition = fhirEngine.get<ActivityDefinition>(activityDefinitionId)
        knowledgeManager.install(writeToFile(activityDefinition))
    }


    private fun writeToFile(resource: Resource): File {
        val jsonParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()


        val fileName =
            if (resource is MetadataResource && resource.name != null) {
                resource.name
            } else {
                "${resource.fhirType()}_${resource.idElement.idPart}"
            }
        return File(context.filesDir, fileName).apply {
            writeText(jsonParser.encodeResourceToString(resource))
        }
    }

    suspend fun createCarePlan(patientId: String, encounterId: String) {
        setKnowledgeResouces()
//        val jsonParser = FhirApplication.fhirContext(this).newJsonParser()

        // We need to add all resource defination inside the fhirEngine local storage,
        // then we can use them to create a CarePlan
//        FhirApplication.fhirEngine(this).apply {
//            create(jsonParser.parseResource(planDefinitionStr) as PlanDefinition)
//            create(jsonParser.parseResource(activityDefinitionStr) as ActivityDefinition)
//            create(jsonParser.parseResource(patientStr) as Patient)
//        }

println("==================== createCarePlan ")
       // And call FhirOperator.generateCarePlan with the ID of the PlanDefinition and the patient ID desired.

//        val carePlan = FhirApplication.fhirOperator(this).generateCarePlan(
//            planDefinitionId = "MedRequest-Example",
//            patientId = "Patient-Example"
//        )

//        val carePlanProposal =
//            fhirOperator.generateCarePlan(
//                planDefinition = CanonicalType(planDefinitionUri),
//                subject = "Patient/$patientId"
//            ) as CarePlan


        val carePlan: CarePlan = fhirOperator.generateCarePlan(
//            planDefinitionId = "MedRequest-Example",
//            planDefinitionId = "1",
            planDefinition =
            CanonicalType(
                "http://172.30.1.26:8080/fhir/PlanDefinition/1",
            ),
            subject = "Patient/$patientId"
//            patientId = patientId,
//            encounterId = encounterId
        ) as CarePlan

        carePlan.id = UUID.randomUUID().toString()
        setLastUpdate(carePlan)

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        println(iParser.encodeResourceToString(carePlan))

        fhirEngine.create(carePlan)
    }

    private fun setLastUpdate( resource: Resource ){

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

        if( resource.hasMeta() ) {
            println("=========== setLastUpdate: ${Date()} " )
            resource.meta.lastUpdated = Date()
        }
        else {
            var meta = Meta()
            meta.lastUpdated = Date()
            resource.setMeta(meta)
        }
        println("=========== setLastUpdate 1 :  ${iParser.encodeResourceToString(resource)}")
    }
}