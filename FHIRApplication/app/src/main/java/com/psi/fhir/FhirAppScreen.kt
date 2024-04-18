package com.psi.fhir

import android.app.AlertDialog
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.psi.fhir.data.RequestResult
import com.psi.fhir.fragments.AddPatientRegistrationFragment
import com.psi.fhir.fragments.EditPatientRegistrationFragment
import com.psi.fhir.ui.composes.EditTextField
import com.psi.fhir.ui.composes.LoadingProgressBar
import com.psi.fhir.ui.composes.LoginScreen
import com.psi.fhir.ui.composes.PatientDetailsScreen
import com.psi.fhir.ui.composes.PatientListScreen
import com.psi.fhir.ui.composes.QuestionnaireScreen
import com.psi.fhir.ui.viewmodels.PatientDetailData
import com.psi.fhir.ui.viewmodels.PatientDetailsViewModel
import com.psi.fhir.ui.viewmodels.PatientListViewModel
import com.psi.fhir.ui.viewmodels.SyncDataStatus
import kotlinx.coroutines.launch


enum class FhirScreen(@StringRes val title: Int) {
    LOGIN(title = R.string.login),
    PATIENT_LIST(title = R.string.app_name),
    PATIENT_DETAILS(title = R.string.patient_details),
    ADD_PATIENT(title = R.string.add_patient),
    EDIT_PATIENT(title = R.string.edit_patient)
}

@Composable
fun FhirApp(
    navController: NavHostController = rememberNavController(),
    fragmentManager: FragmentManager,
) {
    val patientListViewModel: PatientListViewModel = viewModel()
    val patientDetailsViewModel: PatientDetailsViewModel = viewModel()

    var showActions by remember { mutableStateOf(true) }

    // Convert the current screen's title to a value of Fhir App Screen
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FhirScreen.valueOf(
        backStackEntry?.destination?.route ?: FhirScreen.LOGIN.name
    )

    Scaffold(
        topBar = {
            FhirAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                patientListViewModel = patientListViewModel,
                patientDetailsViewModel = patientDetailsViewModel
            )
        },
        floatingActionButton = {
            if(showActions ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(FhirScreen.ADD_PATIENT.name)
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

        var selectedPatientId by remember { mutableStateOf("") }
        var patientDetailsData by remember { mutableStateOf<PatientDetailData?>(null) }

        NavHost(
            navController = navController,
//            startDestination = FhirScreen.LOGIN.name,
            startDestination = FhirScreen.PATIENT_LIST.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = FhirScreen.LOGIN.name) {
                showActions = false
                LoginScreen(onClick = {
                    navController.navigate(FhirScreen.PATIENT_LIST.name)
                })
            }

            composable(route = FhirScreen.PATIENT_LIST.name) {
                showActions = true
                PatientListScreen(
                    onItemClick = { selectedItem ->
                        selectedPatientId = selectedItem.id
                        navController.navigate(FhirScreen.PATIENT_DETAILS.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )

            }


            composable(route = FhirScreen.PATIENT_DETAILS.name) {
                showActions = false
                PatientDetailsScreen(
                    patientId = selectedPatientId,
                    viewModel = patientDetailsViewModel,
                    editButtonClick = { data ->
                        println("=========================")
                        println(data)
                        patientDetailsData = data
                        navController.navigate(FhirScreen.EDIT_PATIENT.name)
                    }
                )
            }

            composable(route = FhirScreen.ADD_PATIENT.name) {
                showActions = false
//                val context = LocalContext.current
//                FormScreen(
//                    formConfig = AppConfigurationHelper.getRegistrationForm()!!,
//                    fhirEngine = FhirApplication.fhirEngine(context)
//                )
//                getSupportFragmentManager
                QuestionnaireScreen(
                    fragmentManager = fragmentManager,
                    questionnaireContainerId = R.id.edit_patient_container,
                    fragment = AddPatientRegistrationFragment()
                )
            }

            composable(route = FhirScreen.EDIT_PATIENT.name) {
                showActions = false
                QuestionnaireScreen(
                    fragmentManager = fragmentManager,
                    questionnaireContainerId = R.id.edit_patient_container,
                    fragment = EditPatientRegistrationFragment(patientDetailsData!!)
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
    patientListViewModel: PatientListViewModel,
    patientDetailsViewModel: PatientDetailsViewModel,
    modifier: Modifier = Modifier
) {
   when( currentScreen ) {
       FhirScreen.PATIENT_LIST ->
           PatientListToolBar(
               currentScreen = currentScreen,
               viewModel = patientListViewModel,
               modifier = modifier
           )
       FhirScreen.ADD_PATIENT ->
           AddPatientToolBar(
               currentScreen = currentScreen,
               navigateUp = navigateUp,
               modifier = modifier
           )

       FhirScreen.PATIENT_DETAILS ->
           PatientDetailsToolBar(
               currentScreen = currentScreen,
               navigateUp = navigateUp,
               viewModel = patientDetailsViewModel,
               modifier = modifier
           )

       FhirScreen.EDIT_PATIENT ->
           EditPatientToolBar(
               currentScreen = currentScreen,
               navigateUp = navigateUp,
               modifier = modifier
           )

       else -> Unit
   }

}

@Composable
fun PatientListToolBar (
    currentScreen: FhirScreen,
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
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = stringResource(R.string.back_button)
//                    )
//                }

        },
        actions = {
            IconButton(onClick = {
                viewModel.performSyncData()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sync_30),
                    contentDescription = "Sync"
                )
            }
        }
    )
}

@Composable
fun AddPatientToolBar (
    currentScreen: FhirScreen,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    )
}

@Composable
fun EditPatientToolBar (
    currentScreen: FhirScreen,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    )
}



@Composable
fun PatientDetailsToolBar (
    currentScreen: FhirScreen,
    viewModel: PatientDetailsViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var patientDeleted by remember {  mutableStateOf<RequestResult?>(null)  }
    var showLoading by remember {  mutableStateOf<Boolean>(false)  }

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                val alertDialog = AlertDialog.Builder( context ).apply {
                    setTitle("Order items")
                    setMessage("Do you want to order the product cart items ?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton( "Yes" ) { dialog,_ ->
                        showLoading = true
                        coroutineScope.launch {
                            patientDeleted = viewModel.deletePatientDetails()
                        }
                        dialog.dismiss()
                    }
                }

                alertDialog.create()
                alertDialog.show()
            }) {
                Icon(
                    painter = painterResource(id = com.google.android.fhir.datacapture.R.drawable.ic_delete),
                    contentDescription = "Sync"
                )
            }
        }
    )

    if( showLoading ) {
        LoadingProgressBar(isLoading = true)
    }

    patientDeleted?.let {
        showLoading = true
        if( it.success) {
            Toast.makeText(context, "The patient is deleted successfully.", Toast.LENGTH_SHORT).show()
            navigateUp()
        }
        else {
            Toast.makeText(context, it.errMsg, Toast.LENGTH_SHORT).show()
        }
    }

}
