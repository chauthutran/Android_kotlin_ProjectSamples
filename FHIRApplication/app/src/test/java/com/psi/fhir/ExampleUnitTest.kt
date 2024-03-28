package com.psi.fhir

import org.junit.Test

import org.junit.Assert.*
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
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

    @Test
    fun expressionTest() {
//
//        // Create a Rhino context
//        val rhinoContext = Context.enter()
//
////        try {
//            // Initialize the scope
//            val scope: Scriptable = rhinoContext.initStandardObjects()
//
//            // JavaScript expression to evaluate
//            val expression = "1 + 1"
//
//            // Evaluate the expression
//            val result = rhinoContext.evaluateString(scope, expression, "<cmd>", 1, null)
//
//            // Print the result
//            assertEquals(2, result)
////        } finally {
////            // Exit the Rhino context
////            Context.exit()
////        }


        // Create a Rhino context
        val rhinoContext = Context.enter()

//        try {
        // Initialize the scope
        val scope: Scriptable = rhinoContext.initStandardObjects()

        // JavaScript expression to evaluate
        val expression = "if ( true ) \"patient_female\"; else \"patient_male\";"

        // Evaluate the expression
        val result = rhinoContext.evaluateString(scope, expression, "<cmd>", 1, null)

        // Print the result
        assertEquals("patient_female", result)
//        } finally {
//            // Exit the Rhino context
//            Context.exit()
//        }
    }
}