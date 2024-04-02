package com.psi.fhir.ui.composes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.psi.fhir.R
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.PatientListViewModel

@Composable
fun PatientListScreen(
    patients: List<PatientUiState>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(contentPadding = contentPadding) {
        itemsIndexed(patients) {index, patient ->
            PatientItemCard(
                patientUiState = patient,
                modifier = modifier
            )
        }
    }
}


@Composable
fun PatientItemCard (
    patientUiState: PatientUiState,
    modifier: Modifier = Modifier
) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ){
            val iconName = AppConfigurationHelper.getListItemIcon(patientUiState)
            PatientIcon(
                context = LocalContext.current,
                drawableName = iconName!!,
                modifier = Modifier
                    .width(68.dp)
                    .height(68.dp)
                    .aspectRatio(1f)
            )

            PatientColumn(
                patientUiState = patientUiState,
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp)
            )

        }
    }
}


@Composable
fun PatientIcon(
    context: Context,
    drawableName: String,
    modifier: Modifier = Modifier
) {
    val resourceId = getDrawableResourceId(context, drawableName)
    val painter = if (resourceId != 0) {
        painterResource(resourceId)
    } else {
        painterResource(R.drawable.patient_unknown)
    }

    Image(
        painter = painter,
        contentDescription = null, // Provide content description if needed
        modifier = modifier
    )
}


@Composable
fun PatientColumn(
    patientUiState: PatientUiState,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .padding(top = 5.dp, start = 5.dp)
    ){
        val dataConfigList = AppConfigurationHelper.getListItemConfig_Data(patientUiState)
        for (i in 0 until dataConfigList!!.length()) {
            val jsonObject = dataConfigList.getJSONObject(i)
            Text(
                text = jsonObject.getString("value"),
                style = getTypographyByName(jsonObject.getString("style"))
            )
        }
    }
}



@Preview
@Composable
fun PatientListScreenPreview() {
//    val patients = listOf<PatientUiState>(
//        PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", "2001-02-09", "0123456789", "City 1", "Country 1", true ),
//        PatientUiState( "2", "xxx-xxx-xxx-xxx" , "Test 1", "M", "2001-02-03", "0123456789", "City 1", "Country 1", true )
//    )

       val   patientUiState1 =   PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", "2001-02-09", "0123456789", "City 1", "Country 1", true )

    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ){

                PatientIcon(
                    context = LocalContext.current,
                    drawableName = "patient_female",
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp)
                        .aspectRatio(1f)
                )

                PatientColumn(
                    patientUiState = patientUiState1,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp)
                )

            }
        }
    }
}