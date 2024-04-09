package com.psi.fhir.ui.composes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.psi.fhir.FhirScreen
import com.psi.fhir.R
import com.psi.fhir.data.PatientListItemUiState
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import com.psi.fhir.ui.viewmodels.PatientListViewModel
import kotlinx.coroutines.launch

@Composable
fun PatientListScreen(
    viewModel: PatientListViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onItemClick: (PatientListItemUiState) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var uiState by remember { mutableStateOf<List<PatientListItemUiState>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        uiState = viewModel.searchPatientsByName("")
    }

    LoadingProgressBar(isLoading = (uiState == null))
    Column {
        EditTextField(
            value = "",
            icon = Icons.Default.Search,
            label = stringResource(R.string.search_by_name),
            modifier = Modifier
                .padding(bottom = 8.dp, top = 4.dp, start = 4.dp, end = 4.dp)
                .fillMaxWidth(),
            onValueChange = { newValue ->
                coroutineScope.launch {
                    uiState = viewModel.searchPatientsByName(newValue)
                }
            }
        )
        uiState?.let {
            LazyColumn(contentPadding = contentPadding) {
                itemsIndexed(uiState!!) { index, patient ->
                    PatientItemCard(
                        patientUiState = patient,
                        onItemClick = onItemClick,
                        modifier = modifier
                    )
                }
            }

        }
    }

}



@Composable
fun PatientItemCard (
    patientUiState: PatientListItemUiState,
    onItemClick: (PatientListItemUiState) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                onClick = { onItemClick(patientUiState) }
            )
//        colors = CardColors(
//            containerColor = Color.White,
//            contentColor = Color.White,
//            disabledContainerColor = Color.White,
//            disabledContentColor = Color.White,
//        )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_super_small))
        ){
            val iconName = AppConfigurationHelper.getListItemIcon(patientUiState)
            PatientIcon(
                context = LocalContext.current,
                drawableName = iconName,
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
    patientUiState: PatientListItemUiState,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .padding(top = 5.dp, start = 5.dp)
    ){
        val dataConfigList = AppConfigurationHelper.getListItemConfig_Data(patientUiState)
        for (i in 0 until dataConfigList.length()) {
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
    val patients = listOf<PatientListItemUiState>(
        PatientListItemUiState( "1" , "Test 1", "F", "2001-02-09", "0123456789", "City 1", "Country 1", true ),
        PatientListItemUiState( "2", "Test 1", "M", "2001-02-03", "0123456789", "City 1", "Country 1", true )
    )

    val  patientUiState1 =   PatientListItemUiState( "1", "Test 1", "F", "2001-02-09", "0123456789", "City 1", "Country 1", true )

    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
//            PatientListScreen(patients)
            PatientItemCard(patientUiState = patientUiState1, onItemClick = {})

//            Row (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(dimensionResource(R.dimen.padding_small))
//            ){
//
//                PatientIcon(
//                    context = LocalContext.current,
//                    drawableName = "patient_female",
//                    modifier = Modifier
//                        .width(68.dp)
//                        .height(68.dp)
//                        .aspectRatio(1f)
//                )
//
//                PatientColumn(
//                    patientUiState = patientUiState1,
//                    modifier = Modifier
//                        .padding(top = 5.dp, start = 5.dp)
//                )
//
//            }
        }
    }
}