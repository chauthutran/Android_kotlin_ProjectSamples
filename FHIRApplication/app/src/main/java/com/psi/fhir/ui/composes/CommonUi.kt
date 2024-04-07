package com.psi.fhir.ui.composes

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.animation.core.*
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue


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

@Composable
fun getTypographyByName(name: String): TextStyle {
    return when (name) {
        "bodyLarge" -> MaterialTheme.typography.bodyLarge
        "displayMedium" -> MaterialTheme.typography.displayMedium
        "displayLarge" -> MaterialTheme.typography.displayLarge

        else -> MaterialTheme.typography.displaySmall
    }
}

fun getDrawableResourceId(context: Context, drawableName: String): Int {
    return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
}


@Preview
@Composable
fun PreviewLoadingProgressBar() {
    LoadingProgressBar(isLoading = false)
}
