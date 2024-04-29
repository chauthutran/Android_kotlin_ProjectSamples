package com.psi.fhir.careplan

import android.content.Context
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.knowledge.FhirNpmPackage
import com.google.android.fhir.knowledge.KnowledgeManager
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import com.google.android.fhir.workflow.FhirOperator
import com.google.android.fhir.workflow.FhirOperator.Builder
import com.psi.fhir.FhirApplication
import com.psi.fhir.data.PatientListItemUiState
import com.psi.fhir.helper.app.AppConfigurationHelper
import com.psi.fhir.ui.viewmodels.toPatientItem
import org.hl7.fhir.r4.model.ActivityDefinition
import org.hl7.fhir.r4.model.CanonicalType
import org.hl7.fhir.r4.model.CarePlan
import org.hl7.fhir.r4.model.IdType
import org.hl7.fhir.r4.model.Meta
import org.hl7.fhir.r4.model.MetadataResource
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.PlanDefinition
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.Task
import java.io.File
import java.time.Instant
import java.time.Period
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

        val carePlan: CarePlan = fhirOperator.generateCarePlan(
            planDefinition = CanonicalType("http://172.30.1.26:8080/fhir/PlanDefinition/1"),
            subject = "Patient/$patientId"
        ) as CarePlan

        carePlan.id = UUID.randomUUID().toString()
        setLastUpdate(carePlan)

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        println(iParser.encodeResourceToString(carePlan))

        fhirEngine.create(carePlan)

        createTask(patientId, carePlan)
    }

//    suspend fun getCarePlanStatus(patientId : String) {
//        val carePlans: MutableList<CarePlan> = mutableListOf()
//
//        fhirEngine.search<CarePlan> {
//            filter (
//                CarePlan.SUBJECT,
//                {
//                    value = "Patient/${patientId}"
//                }
//            )
//        }
//            .mapIndexed {index, searchResult -> searchResult.resource }
//            .let { carePlans.addAll(it) }
//    }

    private fun setLastUpdate( resource: Resource ) {
        if( resource.hasMeta() ) {
            resource.meta.lastUpdated = Date()
        }
        else {
            var meta = Meta()
            meta.lastUpdated = Date()
            resource.setMeta(meta)
        }
    }

    private suspend fun createTask( patientId: String, carePlan: CarePlan ) {
        val taskQuestionnaire = AppConfigurationHelper.getTaskQuestionnaireId()
        val task =
            Task().apply {
                id = UUID.randomUUID().toString()
                status = Task.TaskStatus.DRAFT
                intent = Task.TaskIntent.PROPOSAL
                description = "Medication Review"
                focus.reference = "Questionnaire/${taskQuestionnaire}"
                `for`.reference = "Patient/${IdType(patientId).idPart}"
                restriction.period.end = Date.from(Instant.now().plus(Period.ofDays(180)))
            }
        fhirEngine.create(task)

        carePlan.addActivity().setReference(Reference(task)).detail.status =
            mapTaskStatusToCarePlanStatus(task)
    }

    /** Map [Task] status to [CarePlan] status */
    private fun mapTaskStatusToCarePlanStatus(resource: Task): CarePlan.CarePlanActivityStatus {
        // Refer: http://hl7.org/fhir/R4/valueset-care-plan-activity-status.html for some mapping
        // guidelines
        return when (resource.status) {
            Task.TaskStatus.ACCEPTED -> CarePlan.CarePlanActivityStatus.SCHEDULED
            Task.TaskStatus.DRAFT -> CarePlan.CarePlanActivityStatus.NOTSTARTED
            Task.TaskStatus.REQUESTED -> CarePlan.CarePlanActivityStatus.NOTSTARTED
            Task.TaskStatus.RECEIVED -> CarePlan.CarePlanActivityStatus.NOTSTARTED
            Task.TaskStatus.REJECTED -> CarePlan.CarePlanActivityStatus.STOPPED
            Task.TaskStatus.READY -> CarePlan.CarePlanActivityStatus.NOTSTARTED
            Task.TaskStatus.CANCELLED -> CarePlan.CarePlanActivityStatus.CANCELLED
            Task.TaskStatus.INPROGRESS -> CarePlan.CarePlanActivityStatus.INPROGRESS
            Task.TaskStatus.ONHOLD -> CarePlan.CarePlanActivityStatus.ONHOLD
            Task.TaskStatus.FAILED -> CarePlan.CarePlanActivityStatus.STOPPED
            Task.TaskStatus.COMPLETED -> CarePlan.CarePlanActivityStatus.COMPLETED
            Task.TaskStatus.ENTEREDINERROR -> CarePlan.CarePlanActivityStatus.ENTEREDINERROR
            Task.TaskStatus.NULL -> CarePlan.CarePlanActivityStatus.NULL
            else -> CarePlan.CarePlanActivityStatus.NULL
        }
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


}