package com.psi.fhir

import com.psi.fhir.data.PatientUiState
import com.psi.fhir.helper.convertObjToJson
import org.junit.Test

import org.junit.Assert.*
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.time.LocalDate
import javax.script.ScriptEngineManager

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

//    @Test
//    fun expressionTest() {
//
//        // Create a Rhino context
//        val rhinoContext = Context.enter()
//
////        try {
//        // Initialize the scope
//        val scope: Scriptable = rhinoContext.initStandardObjects()
//
//        val iconId  = "val id = (item.gender=='female') ? 'patient_female' : 'patient_male';"
//
//        val patientUiState = PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", "2001-02-03", "0123456789", "City 1", "Country 1", true )
//
//        println("${convertObjToJson(patientUiState)}")
////        var expression = "var item =  JSON.parse(${convertObjToJson(patientUiState)}); ${iconId};"
////        return expression.evaluateExpression() as String
//
//
//        // Evaluate the expression
//        val result = rhinoContext.evaluateString(scope, "1", "<cmd>", 1, null)
//
//        // Print the result
//        assertEquals("patient_female", result)
////        } finally {
////            // Exit the Rhino context
////            Context.exit()
////        }
//    }
}