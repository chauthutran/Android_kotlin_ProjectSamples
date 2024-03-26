package com.psi.fhir.composes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.psi.fhir.R
import com.psi.fhir.data.PatientData
import com.psi.fhir.ui.theme.FHIRApplicationTheme
import org.hl7.fhir.r4.model.Patient
import java.time.LocalDate

@Composable
fun PatientListScreen(
    patients: List<PatientData>,
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
    patient: PatientData,
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
    val patients = listOf<PatientData>(
        PatientData( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", LocalDate.now(), "0123456789", "City 1", "Country 1", true ),
        PatientData( "1", "xxx-xxx-xxx-xxx" , "Test 1", "M", LocalDate.now(), "0123456789", "City 1", "Country 1", true )
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