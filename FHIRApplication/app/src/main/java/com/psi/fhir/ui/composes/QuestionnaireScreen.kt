package com.psi.fhir.ui.composes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.psi.fhir.R
import com.psi.fhir.fragments.AddPatientRegistrationFragment

@Composable
fun QuestionnaireScreen(
    fragmentManager: FragmentManager,
    questionnaireContainerId : Int,
    fragment: Fragment,
    modifier: Modifier = Modifier
) {

    // Use AndroidView to embed a Fragment in Jetpack Compose
    AndroidView(
        factory = { context ->
            // Create the Fragment instance
            val containerId = questionnaireContainerId // some unique id
            val fragmentContainerView = FragmentContainerView(context).apply {
                id = containerId
            }

            fragmentManager.beginTransaction()
                .replace(containerId, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

            fragmentContainerView

        },
        modifier = modifier.fillMaxSize()
    ) { fragment ->
        // Do any setup or customization of the Fragment here if needed
        // For example, you can call fragment methods or access its properties
    }
}

