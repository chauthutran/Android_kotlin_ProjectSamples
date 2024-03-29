package com.psi.fhir.ui.composes

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingProgressBar(isLoading: Boolean) {
    if (isLoading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
    }
}

@Preview
@Composable
fun PreviewLoadingProgressBar() {
    LoadingProgressBar(isLoading = true)
}


@Composable
fun getTypographyByName(name: String): TextStyle {
    return when (name) {
        "bodyLarge" -> MaterialTheme.typography.bodyLarge
        "displayMedium" -> MaterialTheme.typography.displayMedium
        "displayLarge" -> MaterialTheme.typography.displayLarge

        else -> MaterialTheme.typography.displaySmall
    }
}

@Composable
fun EditNumberField(
    value: String,
    icon: ImageVector,
    hint: String, modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(text = hint) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            IconButton(onClick = { /* Handle icon click if needed */ }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
    )
}

fun getDrawableResourceId(context: Context, drawableName: String): Int {
    return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
}
