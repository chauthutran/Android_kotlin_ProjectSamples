package com.psi.fhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.fhir.FhirEngine
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.PatientListViewModel

/**
 * https://github.com/google/android-fhir/wiki/FEL%3A-Search-FHIR-resources
 */
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fhirEngine: FhirEngine = FhirApplication.fhirEngine(application.applicationContext)

        setContent {
            FHIRApplicationTheme {

               FhirApp(application, fhirEngine)
            }
        }
    }
}
