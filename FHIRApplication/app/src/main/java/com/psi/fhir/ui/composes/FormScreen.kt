package com.psi.fhir.ui.composes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.material.Snackbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.fhir.FhirEngine
import com.psi.fhir.data.SelectOptionUi
import org.json.JSONArray
import org.json.JSONObject

enum class CONTROLTYPE {
    SHORT_TEXT,
    NUMBER,
    DROPDOWN_LIST,
    DATE,
    BOOLEAN,
    PHONE
}

@Composable
fun FormScreen(
    formConfig: JSONObject,
    fhirEngine: FhirEngine? = null,
    modifier: Modifier = Modifier,
) {

    var result: JSONObject by remember { mutableStateOf(JSONObject()) }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()) //  enable the column to scroll vertically. The rememberScrollState() creates and automatically remembers the scroll state.
            .safeDrawingPadding()
            .padding(start = 10.dp, end = 10.dp)
    ) {

        // Generate form
        val inputFieldsConfig = formConfig.getJSONArray("form")
        FormInputFields( inputFieldsConfig, JSONObject(), result)

        // Generate buttons
        Row {
            FormButtons(
                buttonConfigs = formConfig.getJSONArray("buttons"),
                data = result,
                fhirEngine = fhirEngine,
                modifier = Modifier
                    .padding(end = 10.dp)
            )
        }
    }
}

@Composable
fun FormInputFields(
    inputFieldsConfig: JSONArray,
    data: JSONObject,
    result: JSONObject
)
{
    for( i in 0 until inputFieldsConfig!!.length() ) {

        val fieldConfig = inputFieldsConfig.getJSONObject(i)
        val id = fieldConfig.getString("id")
        val label = fieldConfig.getString("defaultLabel")

        result.put(id, "")

        when (fieldConfig.getString("controlType")) {
            CONTROLTYPE.SHORT_TEXT.toString() -> {
                EditTextField(
                    value = result.getString(id),
                    keyboardType = KeyboardType.Text,
                    label = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp),
                    onValueChange = { value ->
                        result.put(id, value)
                    }
                )
            }

            CONTROLTYPE.NUMBER.toString() -> {
                EditTextField(
                    value = result.getString(id),
                    keyboardType = KeyboardType.Number,
                    label = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp)
                    //                        .fillMaxWidth()
                ) { value ->
                    result.put(id, value)
                }
            }

            CONTROLTYPE.PHONE.toString() -> {
                EditTextField(
                    value = result.getString(id),
                    keyboardType = KeyboardType.Phone,
                    label = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp)
                ) { value ->
                    result.put(id, value)
                }
            }

            CONTROLTYPE.DROPDOWN_LIST.toString() -> {
                val optionConfig = fieldConfig.getJSONArray("options")
                var optionList = ArrayList<SelectOptionUi>()

                for (j in 0 until optionConfig.length()) {
                    val optionConfig = optionConfig.getJSONObject(j)
                    optionList.add(
                        SelectOptionUi(
                            text = optionConfig.getString("defaultLabel"),
                            value = optionConfig.getString("value")
                        )
                    )
                }

                DropdownList(
                    value = result.getString(id),
                    label = label,
                    optionList = optionList,
                    onOptionSelected = { value -> result.put(id, value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp)
                )
            }

            CONTROLTYPE.BOOLEAN.toString() -> {
                BooleanField(
                    value = result.getString(id),
                    label = label,
                    onOptionSelected = { value -> result.put(id, value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp)
                )
            }

            CONTROLTYPE.DATE.toString() -> {
                DateField(
                    value = result.getString(id),
                    label = label,
                    formatPattern = fieldConfig.getString("formatDate"),
                    onValueChange = { },
                    onDateSelected = { value -> result.put(id, value) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 4.dp)
                )
            }
        }

    }

}

@Composable
fun FormButtons(
    buttonConfigs: JSONArray,
    data: JSONObject,
    fhirEngine: FhirEngine?,
    modifier: Modifier
) {


    println("---------------FormButtons :  Generate" )
    for (i in 0 until buttonConfigs.length()) {
        FormButton(
            buttonConfig = buttonConfigs.getJSONObject(i),
            data = data,
            fhirEngine = fhirEngine,
            modifier = modifier,
            onClick = {
//                Snackbar.make(LocalContext.current, "Success",Snackbar.LENGTH_SHORT).show()
//                Snackbar() {
//                    Text("This is a Snackbar")
//                }

            }
        )
    }
}

@Preview
@Composable
fun FormScreenPreview()
{
    val formConfig = JSONObject()

    var regFormConfig = JSONArray()

    val field1 = JSONObject()
    field1.put("id", "name")
    field1.put("defaultLabel", "Name field")
    field1.put( "controlType", "SHORT_TEXT")
    regFormConfig.put(field1)


    val field2 = JSONObject()
    field2.put("id", "gender")
    field2.put("defaultLabel", "Gender field")
    field2.put( "controlType", "DROPDOWN_LIST")

    val genderOptions = JSONArray()
    val opt1 = JSONObject()
    opt1.put("defaultLabel", "Male")
    opt1.put("value", "male")
    genderOptions.put(opt1)
    val opt2 = JSONObject()
    opt2.put("defaultLabel", "Female")
    opt2.put("value", "female")
    genderOptions.put(opt2)

    field2.put("options", genderOptions)
    regFormConfig.put(field2)

    val field3 = JSONObject()
    field3.put("id", "date")
    field3.put("defaultLabel", "Date field")
    field3.put( "controlType", "DATE")
    field3.put("formatDate", "yyyy-mm-dd")
    regFormConfig.put(field3)

    formConfig.put("form", regFormConfig)


    // -----------------------
    val buttonConfigs = JSONArray()

    val button1 = JSONObject()
    button1.put("defaultLabel", "Save")
    button1.put("onClick", JSONArray())
    buttonConfigs.put(button1)

    val button2 = JSONObject()
    button2.put("defaultLabel", "Cancel")
    button2.put("onClick", JSONArray())
    buttonConfigs.put(button2)
    // Buttons

    formConfig.put("buttons", buttonConfigs)


    FormScreen(formConfig = formConfig)
}