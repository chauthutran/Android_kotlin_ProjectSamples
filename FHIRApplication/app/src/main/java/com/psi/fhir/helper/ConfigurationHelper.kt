package com.psi.fhir.helper

import android.app.Application
import com.psi.fhir.data.PatientListItemUiState
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

    fun getFormatDatePattern(): String? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getString("formatDatePattern")
    }

    // ---------------------------------------------------------------------------------------------
    // For ListItem configuration

    fun getListItemConfig(): JSONObject? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getJSONObject("listItem")
    }

    fun getListItemIcon(patientUiState: PatientListItemUiState): String {
        val listItemConfig = getListItemConfig() ?: return "patient_unknown"
        var expression = "var item = ${JSONUtils.convertObjToJson(patientUiState)}; ${listItemConfig!!.getString("icon")};"
        return evaluateJavaScript(expression) as String
    }

    fun getListItemConfig_Data( patientUiState: PatientListItemUiState ): JSONArray {
        val listItemConfig = getListItemConfig()

        var dataConfigList = if (listItemConfig != null ) JSONUtils.cloneJsonArray(listItemConfig.getJSONArray("data")) else generateDefaultListItemConfig()
        for (i in 0 until dataConfigList!!.length()) {
            var jsonObject = dataConfigList.getJSONObject(i)
            val value = jsonObject.getString("value")
            var expression = "var item = ${JSONUtils.convertObjToJson(patientUiState)}; ${value};"
            jsonObject.put("value", evaluateJavaScript(expression) as String )
        }

        return dataConfigList
    }

    private fun generateDefaultListItemConfig(): JSONArray {
        var config = JSONArray()

        var itemName = JSONObject()
        itemName.put("value", "item.name")
        itemName.put("style", "displaySmall")


        var itemId = JSONObject()
        itemId.put("value", "item.id")
        itemId.put("style", "displaySmall")


        var itemAddress = JSONObject()
        itemAddress.put("value", "item.city + ', ' + item.country")
        itemAddress.put("style", "displaySmall")

        config.put(itemName)
        config.put(itemId)
        config.put(itemAddress)

        return config
    }


    // ---------------------------------------------------------------------------------------------
    // For Add/Update form

    fun getPatientRegistrationQuestionnaire(): String? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getString("patientRegistrationQuestionnaire")
    }

    fun getPatientPersonalDataQuestionnaire(): String? {
        if( appConfigData == null ) {
            return null
        }

        return appConfigData!!.getString("patientPersonalDataEditQuestionnaire")
    }

}

