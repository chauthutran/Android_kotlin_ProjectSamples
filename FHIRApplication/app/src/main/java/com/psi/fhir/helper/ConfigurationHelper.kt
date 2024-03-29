package com.psi.fhir.helper

import android.app.Application
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.utils.AssestsFile
import com.psi.fhir.utils.JSONUtils
import com.psi.fhir.utils.evaluateJavaScript
import org.json.JSONArray
import org.json.JSONObject

object AppConfigurationHelper {

    private var appConfigData : JSONObject? = null

    fun readConfiguration( application: Application ) {
        appConfigData = AssestsFile.readAndConvertFileContentToJson(application, "app_configuration.json")
    }


    // ---------------------------------------------------------------------------------------------
    // For ListItem configuration

    fun getListItemConfig(): JSONObject? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getJSONObject("listItem")
    }

    fun getListItemIcon(patientUiState: PatientUiState): String? {
        val listItemConfig = getListItemConfig() ?: return null
        var expression = "var item = ${JSONUtils.convertObjToJson(patientUiState)}; ${listItemConfig!!.getString("icon")};"
        return evaluateJavaScript(expression) as String
    }

    fun getListItemConfig_Data( patientUiState: PatientUiState ): JSONArray? {
        val listItemConfig = getListItemConfig() ?: return null

        var dataConfigList = JSONUtils.cloneJsonArray(listItemConfig!!.getJSONArray("data"))
        println("**************** dataConfigList: ${dataConfigList }")
        for (i in 0 until dataConfigList!!.length()) {
            var jsonObject = dataConfigList.getJSONObject(i)
            val value = jsonObject.getString("value")
            var expression = "var item = ${JSONUtils.convertObjToJson(patientUiState)}; ${value};"
            println(" =============== i : ${i}")
            println(" ---- value : ${value}")
            println(" ---- jsonObject : ${dataConfigList}")
            jsonObject.put("value", evaluateJavaScript(expression) as String )
        }

        return dataConfigList;
    }

}

