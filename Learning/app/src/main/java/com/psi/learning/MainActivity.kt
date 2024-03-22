package com.psi.learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psi.learning.ui.theme.LearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    GreetingText(
//                        getString(R.string.happy_birthday_same),
//                        getString(R.string.signature_text)
//                    )
//                }

                GreetingImage(
                    stringResource(R.string.happy_birthday_same),
                    stringResource(R.string.signature_text),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun GreetingImage(message: String, from: String, modifier: Modifier = Modifier) {
    val image = painterResource(id = R.drawable.androidparty)
    Box(modifier){
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5F // Change opacity
        )
        GreetingText(
            message,
            from,
            modifier = Modifier.padding(8.dp)
        )
    }

}


@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = message,
            modifier = modifier,
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = from,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.End)
        )
    }
}

@Preview(showBackground = true, name="My Preview")
@Composable
fun BirthdayCardPreview() {
    LearningTheme {
        GreetingImage(
            stringResource(R.string.happy_birthday_same),
            stringResource(R.string.signature_text),
            modifier = Modifier.padding(8.dp)
        )
    }
}