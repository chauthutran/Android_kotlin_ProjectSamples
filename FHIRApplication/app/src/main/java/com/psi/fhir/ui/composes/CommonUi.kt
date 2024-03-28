package com.psi.fhir.ui.composes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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