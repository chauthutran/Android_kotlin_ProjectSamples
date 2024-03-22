package com.psi.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.learning.ui.theme.LearningTheme

class Quadrant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningTheme {
                Quadrant(
                    title1 = "Text composable", msg1 = "Displays text and follows the recommended Material Design guidelines.",
                    title2 = "Image composable", msg2 = "Creates a composable that lays out and draws a given Painter class object.",
                    title3 = "Row composable", msg3 = "A layout composable that places its children in a horizontal sequence.",
                    title4 = "Column composable", msg4 = "A layout composable that places its children in a vertical sequence."
                )
            }
        }
    }
}

@Composable
fun Quadrant(title1 : String, msg1: String,
            title2: String, msg2: String,
             title3: String, msg3: String,
             title4: String, msg4: String ) {
    Column (
        Modifier.fillMaxWidth()
    ) {
        Row (Modifier.weight(1f)){
            QuadrantColumn(
                modifier =  Modifier.weight(1f),
                backgroundColor = Color(0xFFEADDFF),
                title = title1,
                description  = msg1
            )

            QuadrantColumn(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFD0BCFF),
                title = title2,
                description  = msg2
            )
        }

        Row(Modifier.weight(1f)){
            QuadrantColumn(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFB69DF8),
                title = title3,
                description  = msg3
            )

            QuadrantColumn(
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFF6EDFF),
                title = title4,
                description  = msg4
            )

        }

    }
}

@Composable
fun QuadrantColumn(title: String, description: String, backgroundColor: Color,modifier: Modifier)
{
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = description,
            textAlign = TextAlign.Justify
        )
    }

}
@Preview(showBackground = true, name="Quadrant Review")
@Composable
fun QuadrantPreview() {
    LearningTheme {
        Quadrant(
            title1 = "Text composable", msg1 = "Displays text and follows the recommended Material Design guidelines.",
            title2 = "Image composable", msg2 = "Creates a composable that lays out and draws a given Painter class object.",
            title3 = "Row composable", msg3 = "A layout composable that places its children in a horizontal sequence.",
            title4 = "Column composable", msg4 = "A layout composable that places its children in a vertical sequence."
        )
    }
}
