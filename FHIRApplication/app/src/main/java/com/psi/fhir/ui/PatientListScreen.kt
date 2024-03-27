package com.psi.fhir.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.fhir.R
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import java.time.LocalDate

@Composable
fun PatientListScreen(
    patients: List<PatientUiState>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(contentPadding = contentPadding) {
        itemsIndexed(patients) {index, patient ->
            PatientItemCard(
                patient = patient,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}


@Composable
fun PatientItemCard (
    patient: PatientUiState,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ){
            if(patient.gender == "F") {
                Image(
                    painter = painterResource(id = R.drawable.patient_female),
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp)
                        .aspectRatio(1f)
                )
            }
            else {
                Image(
                    painter = painterResource(id = R.drawable.patient_male),
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp)
                        .aspectRatio(1f)
                )
            }

            Column (
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp)
            ){
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.displaySmall
                )

                Text(
                    text = patient.id,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "${patient.city}, ${patient.country}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview
@Composable
fun PatientListScreenPreview() {
    val patients = listOf<PatientUiState>(
        PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", LocalDate.now(), "0123456789", "City 1", "Country 1", true ),
        PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "M", LocalDate.now(), "0123456789", "City 1", "Country 1", true )
    )
    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            PatientListScreen(
                patients = patients
            )
        }
    }
}