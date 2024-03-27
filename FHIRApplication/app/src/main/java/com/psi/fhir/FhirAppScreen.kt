package com.psi.fhir

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.fhir.FhirEngine
//import com.google.android.fhir.sync.CurrentSyncJobStatus
import com.psi.fhir.ui.composes.PatientListScreen
import com.psi.fhir.ui.viewmodels.PatientListViewModel


enum class FhirScreen(@StringRes val title: Int) {
    PatientList(title = R.string.app_name),
    PatientDetails(title = R.string.patient_details),
    AddPatient(title = R.string.add_patient),
    EditPatient(title = R.string.edit_patient)
}

@Composable
fun FhirAppBar (
    currentScreen: FhirScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun FhirApp(
    application: Application,
    fhirEngine: FhirEngine,
    navController: NavHostController = rememberNavController(),
) {
    // Initialize the ViewModel using the viewModel function
//    val viewModel: PatientListViewModel = PatientListViewModel(application)
    val viewModel: PatientListViewModel = PatientListViewModel( application, fhirEngine )

    val backStackEntry by navController.currentBackStackEntryAsState()
    // Convert the current screen's title to a value of CupcakeScreen
    val currentScreen = FhirScreen.valueOf(
        backStackEntry?.destination?.route ?: FhirScreen.PatientList.name
    )

    // Collect the flow and represent its values as state
    viewModel.performOneTimeSync()

//    // Call the suspend function when the composable is launched
//    LaunchedEffect(Unit) {
//        try {
//            val result = viewModel.performOneTimeSync()
//            viewModel.uiState.value = result
//        } catch (e: Exception) {
//            // Handle any errors
//            // You can display an error message or perform other actions as needed
//            syncResultState.value = "Error: ${e.message}"
//        }
//    }


    Scaffold(
        topBar = {
            FhirAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {  innerPadding ->

        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = FhirScreen.PatientList.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = FhirScreen.PatientList.name) {
                PatientListScreen(
                    patients = uiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }

    }


}