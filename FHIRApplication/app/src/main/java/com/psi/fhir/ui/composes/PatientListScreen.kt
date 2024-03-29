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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.fhir.R
import com.psi.fhir.data.PatientUiState
import com.psi.fhir.helper.AppConfigurationHelper
import com.psi.fhir.ui.theme.FHIRApplicationTheme

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
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}


@Composable
fun PatientItemCard (
    patientUiState: PatientUiState,
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
            val iconName = AppConfigurationHelper.getListItemIcon(patientUiState)
            LoadDrawable(
                context = LocalContext.current,
                drawableName = iconName!!,
                modifier = Modifier
                    .width(68.dp)
                    .height(68.dp)
                    .aspectRatio(1f)
            )

            ItemInfo(
                patientUiState = patientUiState,
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp)
            )

//            Column (
//                modifier = Modifier
//                    .padding(top = 5.dp, start = 5.dp)
//            ){
//                Text(
//                    text = patient.name,
//                    style = MaterialTheme.typography.displaySmall
//                )
//
//                Text(
//                    text = patient.id,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//
//                Text(
//                    text = "${patient.city}, ${patient.country}",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
        }
    }
}


@Preview
@Composable
fun PatientListScreenPreview() {
    val patients = listOf<PatientUiState>(
        PatientUiState( "1", "xxx-xxx-xxx-xxx" , "Test 1", "F", "2001-02-09", "0123456789", "City 1", "Country 1", true ),
        PatientUiState( "2", "xxx-xxx-xxx-xxx" , "Test 1", "M", "2001-02-03", "0123456789", "City 1", "Country 1", true )
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

fun getDrawableResourceId(context: Context, drawableName: String): Int {
    return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
}

@Composable
fun LoadDrawable(
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
fun getTypographyByName(name: String): TextStyle {
    return when (name) {
        "bodyLarge" -> MaterialTheme.typography.bodyLarge
        "displayMedium" -> MaterialTheme.typography.displayMedium
        "displayLarge" -> MaterialTheme.typography.displayLarge


        else -> MaterialTheme.typography.displaySmall
    }
}

@Composable
fun ItemInfo(
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