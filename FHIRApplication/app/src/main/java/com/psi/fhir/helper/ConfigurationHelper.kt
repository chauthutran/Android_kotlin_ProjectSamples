package com.psi.fhir.helper

import android.app.Application
import org.json.JSONArray
import org.json.JSONObject
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

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

    fun getListItemConfig_Icon(): JSONObject? {
        val listItemConfig = getListItemConfig() ?: return null

        return listItemConfig!!.getJSONObject("icon")
    }

    fun getListItemConfig_Data(): JSONArray? {
        val listItemConfig = getListItemConfig() ?: return null

        return listItemConfig!!.getJSONArray("data")
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

fun String.evaluateExpression(): Any? {

    // Create a Rhino context
    val rhinoContext = Context.enter()

    try {
        // Initialize the scope
        val scope: Scriptable = rhinoContext.initStandardObjects()

        // JavaScript expression to evaluate
        val expression = "if ( true ) \"patient_female\"; else \"patient_male\";"

        // Evaluate the expression
        return rhinoContext.evaluateString(scope, expression, "<cmd>", 1, null)
    } finally {
        // Exit the Rhino context
        Context.exit()
    }
}