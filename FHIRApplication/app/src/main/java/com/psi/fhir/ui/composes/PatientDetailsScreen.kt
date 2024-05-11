package com.psi.fhir.ui.composes

import android.view.Surface
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.fhir.R
import com.psi.fhir.data.PatientDetailsUiState
import com.psi.fhir.helper.app.AppConfigurationHelper
import com.psi.fhir.ui.viewmodels.PatientDetailsViewModel
import com.psi.fhir.utils.DateUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.psi.fhir.fragments.AddPatientRegistrationFragment
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.ObservationListItem
import com.psi.fhir.ui.viewmodels.PatientDetailData
import org.hl7.fhir.r4.model.CarePlan
import org.hl7.fhir.r4.model.Task
import java.time.LocalDate

@Composable
fun PatientDetailsScreen(
    patientId: String,
    viewModel: PatientDetailsViewModel = viewModel(),
    editButtonClick: (PatientDetailData) -> Unit,
    openBloodTestBtnClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var uiState by remember { mutableStateOf<PatientDetailData?>(null) }
    LaunchedEffect(Unit) {
        // Call the suspend function from the ViewModel within a coroutine
        uiState = viewModel.getPatientDetails(patientId)
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
//            .background( MaterialTheme.colorScheme.inversePrimary, shape = RoundedCornerShape(4.dp) )
    ) {

        if( uiState == null ) {
            LoadingProgressBar(isLoading = true)
        }
        else {
            PatientCard(
                uiState = uiState!!,
                editButtonClick = editButtonClick,
                openBloodTestBtnClick = openBloodTestBtnClick,
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }

}

@Composable
private fun PatientCard(
    uiState: PatientDetailData,
    editButtonClick: (PatientDetailData) -> Unit,
    openBloodTestBtnClick: (Task) -> Unit = {},
    viewModel: PatientDetailsViewModel,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
//            .background( MaterialTheme.colorScheme.inversePrimary, shape = RoundedCornerShape(4.dp) )
    ) {
        // Patient Details
        PersonalCard(
            patientUiState = uiState!!.patient,
            editButtonClick = editButtonClick,
            viewModel = viewModel
        )
        uiState!!.tasks?.let{ CarePlanScreen( it, openBloodTestBtnClick) }
        ObservationListCard(uiState!!.observations)
    }
}

@Composable
private fun CarePlanScreen(
    tasks: List<Task>,
    openBloodTestBtnClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary, shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = stringResource(R.string.tasks),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 10.dp, start = 10.dp)
        )

        Card (
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 300.dp)
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
        ){
            LazyVerticalGrid(
                modifier = modifier.padding(10.dp),
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                items(tasks) { task ->
                    Column {
                        Text(
                            text = task.description,
                            modifier = Modifier.padding(end = 10.dp)
                        )

                        Text(
                            text = task.status.display,
                            modifier = Modifier.padding(end = 10.dp)
                        )

                        if( task.status == Task.TaskStatus.REQUESTED ) {
                            IconButton(onClick = {
                                println("==== Enter blood test result.")
                                openBloodTestBtnClick(task)
                            }) {
                                Icon(
                                    painterResource(id = com.google.android.material.R.drawable.material_ic_edit_black_24dp),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(5.dp)
                                )
                            }
                        }


                    }
                }
            }
        }


    }
}

@Composable
private fun ObservationListCard(
    observations: List<ObservationListItem>,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary, shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = stringResource(R.string.observations),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 10.dp, start = 10.dp)
        )

        Card (
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 300.dp)
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
        ){
            LazyVerticalGrid(
                modifier = modifier.padding(10.dp),
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                items(observations) { gridItem ->
                    Row {
                        Text(
                            text = gridItem.effective,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = gridItem.value,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun PersonalCard(
    patientUiState: PatientDetailsUiState,
    editButtonClick:  (PatientDetailData) -> Unit = {},
    viewModel: PatientDetailsViewModel,
    modifier: Modifier = Modifier
) {

    var formatDatePattern = AppConfigurationHelper.getFormatDatePattern()
    val ages = "${DateUtils.getAge(patientUiState.dob)} (${stringResource(R.string.years)})"
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(1.dp))
    ) {
        Row {
            Text(
                text = patientUiState.name,
                style = MaterialTheme.typography.displayLarge,
                modifier = modifier.padding(10.dp)
            )

            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(30.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                IconButton(onClick = {
                    println("========= editButtonClick ")
                    editButtonClick(viewModel.patientDetailData!!)
                }) {
                    Icon(
                        painterResource(id = com.google.android.material.R.drawable.material_ic_edit_black_24dp),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }


            }

        }


        Card(
            modifier = modifier.padding(start = 10.dp, bottom = 20.dp, end = 10.dp)
        ) {
            LazyVerticalGrid(
                modifier = modifier.padding(10.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                val dataItemList = listOf<GridItem>(
                    GridItem(title = R.string.gender, value = patientUiState.name),
                    GridItem(title = R.string.age, value = ages),
                    GridItem(
                        title = R.string.dob,
                        value = DateUtils.formatDate(patientUiState.dob, formatDatePattern)
                    ),
                )
                items(dataItemList) { gridItem ->
                    PersonalItem(
                        item = gridItem
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonalItem(item: GridItem) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
        ) {
            Text(
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            Text(
                text = item.value.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp)
            )


        }

    }
}

data class GridItem(
    @StringRes val title: Int,
    val value: String
)


@Preview
@Composable
fun PreviewPatientDetails() {

    val  patientUiState1 =   PatientDetailsUiState( "1","Test 1", "F", LocalDate.now(), "0123456789", "City 1", "Country 1" )
    val obs = listOf<ObservationListItem>(
       ObservationListItem ( id = "1", effective = "2012-01-01", value = "Fever" ),
       ObservationListItem ( id = "2", effective = "2012-02-02", value = "Fever 2" )
    )

    val patientDetails = PatientDetailData( patientUiState1, emptyList(),  emptyList(), obs )
    var patientDetailsViewModel: PatientDetailsViewModel = viewModel()

    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PatientCard(patientDetails, {}, {}, patientDetailsViewModel  )
        }
    }

}