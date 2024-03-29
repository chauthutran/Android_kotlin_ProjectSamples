package com.psi.fhir.helper

import android.app.Application
import com.google.gson.Gson
import com.psi.fhir.data.PatientUiState
import org.json.JSONArray
import org.json.JSONObject
import org.mozilla.javascript.Context
import org.mozilla.javascript.NativeJSON
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject

object AppConfiguration {

    private var appConfigData : JSONObject? = null
    fun readConfiguration( application: Application ): JSONObject? {

        appConfigData = AssetsFileHelper.readAndConvertFileContentToJson(application, "app_configuration.json")

        return appConfigData
    }

    fun getListItemConfig(): JSONObject? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getJSONObject("listItem")
    }

    fun getListItemConfig_Icon(patientUiState: PatientUiState): String? {
        val listItemConfig = getListItemConfig() ?: return null
        var expression = "var item = ${convertObjToJson(patientUiState)}; ${listItemConfig!!.getString("icon")};"
        return evaluateJavaScript(expression) as String
    }

    fun getListItemConfig_Data(): JSONArray? {
        val listItemConfig = getListItemConfig() ?: return null

        return (listItemConfig!!.getJSONArray("data")
    }
}

object AssetsFileHelper {
    fun readFileFromAssets(application: Application, filename: String): String {
        return application.assets.open(filename).bufferedReader().use {
            it.readText()
        }.trimIndent()
    }

    fun readAndConvertFileContentToJson( application: Application, filename: String ): JSONObject? {
        val jsonString = readFileFromAssets(application, filename)
        return jsonString.toJSONObject()
    }
}



fun String.toJSONObject(): JSONObject? {
    if( this.isNullOrEmpty() ) {
        return null
    }

    return JSONObject(this)
}


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

fun convertObjToJson(obj: Any): JSONObject {
    var gson = Gson()
    var jsonString = gson.toJson(obj)
    return JSONObject(jsonString)
}

fun main() {
    val result = evaluateJavaScript("1 + 1")
    println("Result: $result") // Output: Result: 2
}