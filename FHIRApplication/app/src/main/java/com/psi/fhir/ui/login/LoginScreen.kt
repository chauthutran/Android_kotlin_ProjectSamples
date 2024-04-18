package com.psi.fhir.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psi.fhir.R
import com.psi.fhir.ui.composes.EditTextField
import com.psi.fhir.ui.composes.LoadingProgressBar
import com.psi.fhir.ui.theme.FHIRApplicationTheme

@Composable
fun LoginScreen(
    onClick: (HashMap<String,String>) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var loginData: HashMap<String, String> by remember { mutableStateOf(HashMap()) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()) //  enable the column to scroll vertically. The rememberScrollState() creates and automatically remembers the scroll state.
                .safeDrawingPadding()
                .padding(start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_health_care_50),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier.padding(0.dp)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(10.dp)
                )
            }


            EditTextField(
                value = loginData["username"] ?: "",
                keyboardType = KeyboardType.Text,
                label = stringResource(id = R.string.username),
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = { value ->
                    loginData["username"] = value
                }
            )

            EditTextField(
                value = loginData["password"] ?: "",
                keyboardType = KeyboardType.Password,
                label = stringResource(id = R.string.password),
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 4.dp),
                onValueChange = { value ->
                    loginData["password"] = value
                }
            )

            LoadingProgressBar( isLoading = isLoading )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    onClick = {
                        isLoading = true
                        onClick(loginData)
                    }
                ) {
                    Text(text = "Login", fontSize = 24.sp)
//                    AutoAnimateVectorIcon(id = R.drawable.ic_loading_30)
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    FHIRApplicationTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }
    }
}