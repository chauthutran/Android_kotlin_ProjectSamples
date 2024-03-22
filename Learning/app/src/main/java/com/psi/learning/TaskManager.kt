package com.psi.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psi.learning.ui.theme.LearningTheme

class TaskManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningTheme {
                TaskManagerComppose(
                    message1 = "All tasks completed",
                    message2 = "Nice work!",
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    }
}

@Composable
fun TaskManagerComppose(message1: String, message2: String, modifier: Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskManagerImage()

            Text(
                text = message1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = message2,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )


        }
    }
}

@Composable
fun TaskManagerImage() {
    val image = painterResource(R.drawable.ic_task_completed)
    Image(
        painter = image,
        contentDescription = null,
        alignment = Alignment.Center,
        modifier = Modifier.padding(0.dp)
    )
}

@Preview(showBackground = true, name="TaskManager Review")
@Composable
fun TaskManagerPreview() {
    LearningTheme {
        TaskManagerComppose(
            message1 = "All tasks completed",
            message2 = "Nice work!",
            modifier = Modifier.padding(0.dp)
        )
    }
}
