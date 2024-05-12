package com.psi.fhir.careplan

import android.content.Context
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.get
import com.google.android.fhir.knowledge.KnowledgeManager
import com.google.android.fhir.workflow.FhirOperator.Builder
import com.psi.fhir.helper.app.AppConfigurationHelper
import org.hl7.fhir.r4.model.CanonicalType
import org.hl7.fhir.r4.model.CarePlan
import org.hl7.fhir.r4.model.IdType
import org.hl7.fhir.r4.model.Meta
import org.hl7.fhir.r4.model.MetadataResource
import org.hl7.fhir.r4.model.PlanDefinition
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ServiceRequest
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
        var planDefinition = fhirEngine.get<PlanDefinition>("1")
        knowledgeManager.install(writeToFile(planDefinition))
    }


    suspend fun createCarePlan(patientId: String, encounterId: String) {
        setKnowledgeResouces()

        // Create CarePlan
        val carePlan: CarePlan = fhirOperator.generateCarePlan(
            planDefinition = CanonicalType("${AppConfigurationHelper.getFhirBaseUrl()}/PlanDefinition/${AppConfigurationHelper.getPlanDefinitionId()}"),
            subject = "Patient/$patientId"
        ) as CarePlan

        carePlan.id = UUID.randomUUID().toString()
        setLastUpdate(carePlan)

        fhirEngine.create(carePlan)

        createCarePlanActivities(patientId, carePlan)
    }

    suspend fun createCarePlanActivities(patientId: String, carePlan: CarePlan){

//        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val task =
            Task().apply {
                id = UUID.randomUUID().toString()
                status = Task.TaskStatus.REQUESTED
                intent = Task.TaskIntent.PROPOSAL
                description = "Dose of Prevenar 13 vaccine"
                focus.reference = "CarePlan/${carePlan.id}"
                `for`.reference = "Patient/${patientId}"
                restriction.period.end = Date.from(Instant.now().plus(Period.ofDays(180)))
            }

        fhirEngine.create(task)

    }

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


    suspend fun createVaccinationServiceRequest(patientId: String, carePlan: CarePlan, taskDescription: String, noStartDays: Int?, noEndDate: Int?) {
        val vaccinateRequest = ServiceRequest().apply {
            status = ServiceRequest.ServiceRequestStatus.ACTIVE
            intent = ServiceRequest.ServiceRequestIntent.DIRECTIVE
            subject.reference = "Patient/${patientId}"
        }

        fhirEngine.create(vaccinateRequest)

        val task =
            Task().apply {
                id = UUID.randomUUID().toString()
                status = Task.TaskStatus.REQUESTED
                intent = Task.TaskIntent.PROPOSAL
                description = taskDescription // "Vaccination Dose 1"
                `for`.reference = "Patient/${IdType(patientId).idPart}"
                noStartDays?.let{ restriction.period.start = Date.from(Instant.now().plus(Period.ofDays(it))) }
                noEndDate?.let{restriction.period.end = Date.from(Instant.now().plus(Period.ofDays(it)))}
            }
        fhirEngine.create(task)

        carePlan.addActivity().setReference(Reference(task)).detail.status =
            CarePlan.CarePlanActivityStatus.NOTSTARTED
        fhirEngine.update(carePlan)
    }

//    private suspend fun createTask( patientId: String, carePlan: CarePlan ) {
//        val taskQuestionnaire = AppConfigurationHelper.getTaskQuestionnaireId()
//        val task =
//            Task().apply {
//                id = UUID.randomUUID().toString()
//                status = Task.TaskStatus.DRAFT
//                intent = Task.TaskIntent.PROPOSAL
//                description = "Blood Test"
////                focus.reference = "Questionnaire/${taskQuestionnaire}"
//                `for`.reference = "Patient/${IdType(patientId).idPart}"
//                restriction.period.end = Date.from(Instant.now().plus(Period.ofDays(180)))
//            }
//        fhirEngine.create(task)
//
//        carePlan.addActivity().setReference(Reference(task)).detail.status =
//            mapTaskStatusToCarePlanStatus(task)
//    }

    /** Map [Task] status to [CarePlan] status */
//    private fun mapTaskStatusToCarePlanStatus(resource: Task): CarePlan.CarePlanActivityStatus {
//        // Refer: http://hl7.org/fhir/R4/valueset-care-plan-activity-status.html for some mapping
//        // guidelines
//        return when (resource.status) {
//            Task.TaskStatus.ACCEPTED -> CarePlan.CarePlanActivityStatus.SCHEDULED
//            Task.TaskStatus.DRAFT -> CarePlan.CarePlanActivityStatus.NOTSTARTED
//            Task.TaskStatus.REQUESTED -> CarePlan.CarePlanActivityStatus.NOTSTARTED
//            Task.TaskStatus.RECEIVED -> CarePlan.CarePlanActivityStatus.NOTSTARTED
//            Task.TaskStatus.REJECTED -> CarePlan.CarePlanActivityStatus.STOPPED
//            Task.TaskStatus.READY -> CarePlan.CarePlanActivityStatus.NOTSTARTED
//            Task.TaskStatus.CANCELLED -> CarePlan.CarePlanActivityStatus.CANCELLED
//            Task.TaskStatus.INPROGRESS -> CarePlan.CarePlanActivityStatus.INPROGRESS
//            Task.TaskStatus.ONHOLD -> CarePlan.CarePlanActivityStatus.ONHOLD
//            Task.TaskStatus.FAILED -> CarePlan.CarePlanActivityStatus.STOPPED
//            Task.TaskStatus.COMPLETED -> CarePlan.CarePlanActivityStatus.COMPLETED
//            Task.TaskStatus.ENTEREDINERROR -> CarePlan.CarePlanActivityStatus.ENTEREDINERROR
//            Task.TaskStatus.NULL -> CarePlan.CarePlanActivityStatus.NULL
//            else -> CarePlan.CarePlanActivityStatus.NULL
//        }
//    }

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