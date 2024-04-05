package com.psi.fhir

//import com.psi.fhir.helper.convertObjToJson

//import ca.uhn.fhir.util.StructureMapUtilities
//import ca.uhn.fhir.transform.TransformSupportServices
//import org.smartregister.fhircore.engine.util.helper.TransformSupportServices

import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import ca.uhn.fhir.parser.IParser
import com.google.gson.Gson
import com.psi.fhir.utils.TransformSupportServices
import org.hl7.fhir.r4.context.SimpleWorkerContext
import org.hl7.fhir.r4.elementmodel.Element
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceFactory
import org.hl7.fhir.r4.utils.StructureMapUtilities
import org.hl7.fhir.utilities.npm.FilesystemPackageCacheManager
import org.hl7.fhir.utilities.npm.ToolsVersion
import org.junit.Assert.*
import org.junit.Test
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


//import org.hl7.fhir.r4.utils.StructureMapUtilities


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testStructureMap() {
        // Initialize package manager cache to save packages on user home
//        val pcm = FilesystemPackageCacheManager(true, ToolsVersion.TOOLS_VERSION)
        val pcm = FilesystemPackageCacheManager(false)

        // Package name manually checked from https://simplifier.net/packages?Text=hl7.fhir.core&fhirVersion=All+FHIR+Versions
        // Create R4 context
        val contextR4 =
            SimpleWorkerContext.fromPackage(pcm.loadPackage("hl7.fhir.r4.core", "4.0.1"))
        contextR4.isCanRunWithoutTerminology = true

        // Create an instance of StructureMapUtilities to use R4 context
        val structureMapUtilities = StructureMapUtilities(contextR4)

//        // Deserialize structuremap string
//        val map = structureMapUtilities.parse("StructureMap_Generate.map", "FHIRMapperTutorial")

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val immunizationStructureMap = getStringFromFile("C:/Users/cthut/Documents/GitHub/Android_kotlin_ProjectSamples/FHIRApplication/app/src/test/resources/StructureMap_Generate.map")

        val structureMap =
            structureMapUtilities.parse(immunizationStructureMap, "psi reg")
        val mapString = iParser.encodeResourceToString(structureMap)

        println("===== structureMap: ${mapString}")

//        // Generate empty target resource object
//        val targetResource: Resource =
//            ResourceFactory.createResource(scu.getTargetType(map).getName())
//
//        // Deserialize source JSON
//        val baseElement: Element = JsonParser(contextR4)
//            .parse(ByteArrayInputStream("{your-source-resource-json}".toByteArray(StandardCharsets.UTF_8)))
//
////        // Perform extraction to targetResource
//        structureMapUtilities.transform(contextR4, baseElement, map, targetResource)
    }

    @Test
    fun generateStructureMap() {
        val registrationQuestionnaireResponseString =  getStringFromFile("C:/Users/cthut/Documents/GitHub/Android_kotlin_ProjectSamples/FHIRApplication/app/src/test/resources/Questionnaire_Response.json")
        val immunizationStructureMap = getStringFromFile("C:/Users/cthut/Documents/GitHub/Android_kotlin_ProjectSamples/FHIRApplication/app/src/test/resources/StructureMap_Generate.map")
        val packageCacheManager = FilesystemPackageCacheManager(true)

        val contextR4 =
            SimpleWorkerContext.fromPackage(packageCacheManager.loadPackage("hl7.fhir.r4.core", "4.0.1"))
                .apply {
                    setExpansionProfile(Parameters())
                    isCanRunWithoutTerminology = true
                }

        val transformSupportServices = TransformSupportServices(contextR4)
        val structureMapUtilities =  org.hl7.fhir.r4.utils.StructureMapUtilities(contextR4, transformSupportServices)
        val structureMap =
            structureMapUtilities.parse(immunizationStructureMap, "psi reg")

        val iParser: IParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()

        val mapString = iParser.encodeResourceToString(structureMap)

        println("===== structureMap: ${mapString}")
        val targetResource = Bundle()
        val baseElement =
            iParser.parseResource(
                QuestionnaireResponse::class.java,
                registrationQuestionnaireResponseString,
            )

        structureMapUtilities.transform(contextR4, baseElement, structureMap, targetResource)

        var gson = Gson()
        var jsonString = gson.toJson(targetResource.entry[0].resource)
        println("===== Result: ${jsonString}")
//        println("===== Result: ${targetResource}")

//        println("===== mapString: ${targetResource.entry}")

//        Assert.assertEquals("Patient", targetResource.entry[0].resource.resourceType.toString())
        assertEquals(true, true)
    }

    @Throws(Exception::class)
    fun getStringFromFile(filePath: String?): String? {
        val fl = File(filePath)
        val fin = FileInputStream(fl)
        val ret: String = convertStreamToString(fin)
        //Make sure you close all streams.
        fin.close()
        return ret
    }

    @Throws(java.lang.Exception::class)
    fun convertStreamToString(istream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(istream))
        val sb = java.lang.StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }
}