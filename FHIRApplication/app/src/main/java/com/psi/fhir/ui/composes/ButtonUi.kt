package com.psi.fhir.ui.composes

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.parser.IParser
import com.google.android.fhir.FhirEngine
import com.psi.fhir.utils.JSONUtils
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

            for (i in 0 until it.length()) {
                var buttonConfig = it.getJSONObject(i)

                when (buttonConfig.getString("actionType")) {
                    "queueActivity" -> {
                        val resource = buttonConfig.getString("resource")
                        if(  data != null ) {
                            val payload = generatePayload(buttonConfig, data)

                            if (resource.equals("Patient")) {

                                // Create a new FhirContext
                                val fhirContext = FhirContext.forR4()

                                // Create a new JSON parser
                                val jsonParser: IParser = fhirContext.newJsonParser()

                                val patient = jsonParser.parseResource(payload.toString()) as Patient
                                patient.id = UUID.randomUUID().toString()

                                fhirEngine?.apply { create(patient) }

                            }
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
    return JSONUtils.parseJSONValue(data, configPayload, JSONObject())
}