package com.psi.fhir.ui.composes

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.fhir.R
import com.psi.fhir.data.PatientDetailsUiState
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.PatientDetailsViewModel
import com.psi.fhir.utils.DateUtils
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PatientDetailsScreen(
    patientId: String,
    viewModel: PatientDetailsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var uiState by remember { mutableStateOf<PatientDetailsUiState?>(null) }
    LaunchedEffect(Unit) {
        // Call the suspend function from the ViewModel within a coroutine
        uiState = viewModel.findPatientById(patientId)
    }

    var formatDatePattern = AppConfigurationHelper.getFormatDatePattern()

    var data by remember { mutableStateOf("") }

    Column (
        modifier = modifier.padding(10.dp)
    ) {

        if( uiState == null ) {
           LoadingProgressBar(isLoading = true)
        }
        else {
               val ages = "${DateUtils.getAge(uiState!!.dob)} (${stringResource(R.string.years)})"

               Text(
                   text = uiState!!.name,
                   style = MaterialTheme.typography.displayLarge
               )

               Card {
                   LazyVerticalGrid(
                       modifier = Modifier.padding(10.dp),
                       columns = GridCells.Fixed(3),
                       verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                       horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                   ) {
                       val dataItemList = listOf<GridItem>(
                           GridItem(title = R.string.gender, value = uiState!!.name),
                           GridItem(title = R.string.age, value = ages),
                           GridItem(
                               title = R.string.dob,
                               value = DateUtils.formatDate(uiState!!.dob, formatDatePattern)
                           ),
                       )
                       items(dataItemList) { gridItem ->
                           GridItemLayout(
                               item = gridItem
                           )
                       }
                   }
               }
        }
    }

}

@Composable
private fun GridItemLayout(item: GridItem) {
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

    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
//            PatientDetailsScreen(patientUiState1)

        }
    }

}