package com.psi.fhir

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.sync.SyncJobStatus
import com.psi.fhir.ui.composes.LoadingProgressBar
//import com.google.android.fhir.sync.CurrentSyncJobStatus
import com.psi.fhir.ui.composes.PatientListScreen
import com.psi.fhir.ui.viewmodels.PatientListViewModel
import kotlinx.coroutines.flow.collectLatest


enum class FhirScreen(@StringRes val title: Int) {
    PatientList(title = R.string.app_name),
    PatientDetails(title = R.string.patient_details),
    AddPatient(title = R.string.add_patient),
    EditPatient(title = R.string.edit_patient)
}

@Composable
fun FhirApp(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: PatientListViewModel = viewModel()

    val backStackEntry by navController.currentBackStackEntryAsState()
    // Convert the current screen's title to a value of Fhir App Screen
    val currentScreen = FhirScreen.valueOf(
        backStackEntry?.destination?.route ?: FhirScreen.PatientList.name
    )

//    viewModel.performOneTimeSync()
    viewModel.searchPatients()


    Scaffold(
        topBar = {
            FhirAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    ) {  innerPadding ->

        val uiState by viewModel.uiState.collectAsState()
        var loadingStatus by remember { mutableStateOf(false) }

        NavHost(
            navController = navController,
            startDestination = FhirScreen.PatientList.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = FhirScreen.PatientList.name) {
                loadingStatus = true

                LaunchedEffect(Unit) {
                    viewModel.pollState.collectLatest {
                        when (it) {
                            is SyncJobStatus.Finished -> {
                                loadingStatus = false
                            }
                            else -> {}
                        }
                    }
                }

               LoadingProgressBar(isLoading = loadingStatus)

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


@Composable
fun FhirAppBar (
    currentScreen: FhirScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    viewModel: PatientListViewModel,
    modifier: Modifier = Modifier
) {
    TopAppBar(
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
        },
        actions = {
            IconButton(onClick = {
                viewModel.performOneTimeSync()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sync_30),
                    contentDescription = "Sync"
                )
            }
        }
    )
}

