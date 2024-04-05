package com.psi.fhir

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.ui.composes.EditTextField
import com.psi.fhir.ui.composes.FormScreen
import com.psi.fhir.ui.composes.LoadingProgressBar
import com.psi.fhir.ui.composes.PatientListScreen
import com.psi.fhir.ui.composes.QuestionnaireScreen
import com.psi.fhir.ui.viewmodels.PatientListViewModel
import com.psi.fhir.ui.viewmodels.SyncDataStatus


enum class FhirScreen(@StringRes val title: Int) {
    PatientList(title = R.string.app_name),
    PatientDetails(title = R.string.patient_details),
    AddPatient(title = R.string.add_patient),
    EditPatient(title = R.string.edit_patient)
}

@Composable
fun FhirApp(
    navController: NavHostController = rememberNavController(),
    fragmentManager: FragmentManager,
) {
    val viewModel: PatientListViewModel = viewModel()
    var showActions by remember { mutableStateOf(true) }

    // Convert the current screen's title to a value of Fhir App Screen
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FhirScreen.valueOf(
        backStackEntry?.destination?.route ?: FhirScreen.PatientList.name
    )

    // Sync data
    viewModel.performSyncData()

    Scaffold(
        topBar = {
            FhirAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                showActions = showActions,
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            if(showActions ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(FhirScreen.AddPatient.name)
                    }
                ) {
                    // You can customize the appearance of the FAB here
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        },
//        content = {
//            // Your content goes here
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Hello, Jetpack Compose!")
//            }
//        }
    ) { innerPadding ->

        var searchKeyword: String by remember { mutableStateOf("") }
        val pollState by viewModel.pollState.collectAsState()
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = FhirScreen.PatientList.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = FhirScreen.PatientList.name) {

                showActions = true
                LoadingProgressBar(isLoading = (pollState == SyncDataStatus.LOADING))
                Column {
                    EditTextField(
                        value = searchKeyword,
                        icon = Icons.Default.Search,
                        label = stringResource(R.string.search_by_name),
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 4.dp)
                            .fillMaxWidth()
                    ) {
                        searchKeyword = it
                        viewModel.searchPatients(searchKeyword)
                    }

                    PatientListScreen(
                        patients = uiState,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

            }

            composable(route = FhirScreen.AddPatient.name) {
                showActions = false
//                val context = LocalContext.current
//                FormScreen(
//                    formConfig = AppConfigurationHelper.getRegistrationForm()!!,
//                    fhirEngine = FhirApplication.fhirEngine(context)
//                )
//                getSupportFragmentManager
                QuestionnaireScreen( fragmentManager = fragmentManager )
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
    showActions: Boolean,
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
            if (showActions ) {
                IconButton(onClick = {
                    viewModel.performSyncData()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sync_30),
                        contentDescription = "Sync"
                    )
                }
            }
        }
    )
}

