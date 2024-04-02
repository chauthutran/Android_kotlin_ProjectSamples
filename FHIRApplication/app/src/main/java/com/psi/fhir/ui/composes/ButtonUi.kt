package com.psi.fhir.ui.composes

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.android.fhir.FhirEngine
import com.psi.fhir.utils.JSONUtils
import com.psi.fhir.utils.evaluateJavaScript
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import org.json.JSONObject
import java.util.UUID

@Composable
fun FormButton (
    buttonConfig: JSONObject,
    data: JSONObject?,
    fhirEngine: FhirEngine?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        modifier = modifier,
        onClick = {
        coroutineScope.launch {
            runAction(
                config = buttonConfig,
                data = data,
                fhirEngine = fhirEngine,
            )

            onClick()
        }

    }) {
        Text(buttonConfig.getString("defaultLabel"), fontSize = 24.sp)
    }
}

suspend fun runAction(config: JSONObject, data: JSONObject?, fhirEngine: FhirEngine? ) {
    var onClickConfig = config.getJSONArray("onClick")

    onClickConfig?.let {

        for( i in 0 until it.length() ) {
            var buttonConfig = it.getJSONObject(i)

            when( buttonConfig.getString("actionType")) {
                "queueActivity" -> {
                    val resource = buttonConfig.getString("resource")
                    if( resource == "Patient" && data != null ) {
                        val patient: Patient = generatePayload(buttonConfig, data) as Patient
                        patient.id = UUID.randomUUID().toString()

                        println(" ============== Payload: ${generatePayload(buttonConfig, data)}")
                        println(" ----------- patient: ${patient}")
                        fhirEngine?.apply {  create(patient) }
                    }

                }

                "closeForm" -> {

                }

                else -> Unit
            }
        }
    }
}
fun generatePayload( buttonConfig: JSONObject, data: JSONObject ): Any? {
    var configPayload = buttonConfig.getJSONObject("payload")
    println("---------- payload: ${configPayload}")
    return JSONUtils.parseJSONValue(data, configPayload)
}