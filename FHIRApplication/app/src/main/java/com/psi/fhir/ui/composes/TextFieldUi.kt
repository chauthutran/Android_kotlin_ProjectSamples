package com.psi.fhir.ui.composes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.psi.fhir.data.SelectOptionUi
import com.psi.fhir.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun EditTextField(
    value: String,
    icon: ImageVector? = null,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {

    var value: String by remember { mutableStateOf(value) }

    if( icon == null ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            modifier = modifier,
            label = { Text(text = label) },
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            )
        )
    }
    else {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            modifier = modifier,
            label = { Text(text = label) },
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                IconButton(onClick = { /* Handle icon click if needed */ }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
    }
}

@Composable
fun DropdownList (
    value: String,
    label: String,
    optionList: List<SelectOptionUi>,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption: SelectOptionUi by remember { mutableStateOf(SelectOptionUi()) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { expanded = true })
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedOption.text,
                onValueChange = {},
                label = { Text(text = label) },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                optionList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.text) },
                        onClick = {
                            selectedOption = option
                            onOptionSelected(option.value)
                            expanded = false
                        }
                    )
                }
            }
        }

    }

}

@Composable
fun BooleanField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    val optionList = listOf(
        SelectOptionUi(text = "Please select", value = ""),
        SelectOptionUi(text = "Yes", value = "true"),
        SelectOptionUi(text = "No", value = "false")
    )

    DropdownList(
        value = value,
        label = label,
        optionList = optionList,
        modifier = modifier,
        onOptionSelected = onOptionSelected
    )
}

@Composable
fun DateField(
    value: String,
    formatPattern: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDateSelected: (String) -> Unit,
    modifier: Modifier
) {
    var date: String by remember { mutableStateOf(value) }

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    val context = LocalContext.current as FragmentActivity

    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = date,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        readOnly = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            IconButton(onClick = {
                showDatePicker = true
//                val datePicker = MaterialDatePicker.Builder.datePicker()
//                    .setTitleText("Select Date")
//                    .setSelection(selectedDate?.time ?: MaterialDatePicker.todayInUtcMilliseconds())
//                    .build()
//
//                datePicker.addOnPositiveButtonClickListener { date ->
//                    println("======================= date: ${date}")
//                    selectedDate = Date(date)
//                    println("======================= selectedDate: ${selectedDate}")
//                    val formattedDate = SimpleDateFormat(formatPattern, Locale.getDefault()).format(selectedDate!!) // "yyyy-MM-dd"
//println("--------- formattedDate: ${formattedDate}")
//                    onDateSelected(formattedDate)
//                }

//                datePicker.show(context.supportFragmentManager, null)

            }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        }
    )

    if (showDatePicker) {
        CustomDatePickerDialog(
            value = value,
            formatPattern = formatPattern,
            hint = label,
            onDateSelected = {
                date = it
                onDateSelected(date)
            },
            onDismiss =  { showDatePicker = false },
            modifier = modifier
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    value: String,
    formatPattern: String,
    hint: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        DateUtils.convertMillisToDate(it, formatPattern)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
