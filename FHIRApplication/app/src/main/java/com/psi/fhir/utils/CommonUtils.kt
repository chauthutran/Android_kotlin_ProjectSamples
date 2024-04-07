package com.psi.fhir.utils

import android.app.Application
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable



/** We shouldn't use
 *      fun String.evaluateJavaScript(): Any
 *  because "this" we will use in context.evaluateString(scope, this, "JavaScript", 1, null) cause an exception
 */
fun evaluateJavaScript(jsCode: String): Any? {
    val context = Context.enter()
    return try {
        // Android doesn't run JVM bytecode byt Dalvik bytecode --> SO we have to disable optimization
        context.optimizationLevel  = -1
        val scope: Scriptable = context.initStandardObjects()
        context.evaluateString(scope, jsCode, "JavaScript", 1, null)
    } finally {
        Context.exit()
    }
}

object AssestsFile {
    fun readFileFromAssets(application: Application, filename: String): String {
        return application.assets.open(filename).bufferedReader().use {
            it.readText()
        }.trimIndent()
    }

    fun readAndConvertFileContentToJson(application: Application, filename: String ): JSONObject? {
        val jsonString = readFileFromAssets(application, filename)
        return JSONUtils.toJSONObject(jsonString)
    }
}