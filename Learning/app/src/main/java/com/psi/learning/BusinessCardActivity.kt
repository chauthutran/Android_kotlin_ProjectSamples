package com.psi.learning

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psi.learning.ui.theme.LearningTheme

class BusinessCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

        }
    }
}

@Composable
fun BusinessCard(modifier: Modifier) {
    NameCard(modifier)
}

@Composable
fun NameCard( modifier: Modifier)  {

    val colorString = ("#d2e8d4").substring(1)
    val textColor = ("#2e8557").substring(1)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(android.graphics.Color.parseColor("#$colorString"))),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.k2tki),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier.padding(0.dp)
                )

                Text(
                    text = "Full Name",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Title",
                    fontWeight = FontWeight.Bold,
                    color = Color(android.graphics.Color.parseColor("#$textColor")),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )


            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentAlignment = Alignment.Center
        ) {
            CardDetails(
                modifier = Modifier.padding(8.dp)
            )
        }


    }
}

@Composable
fun CardDetails(modifier: Modifier) {
    Column {
        CardDetailsItem( R.drawable.call_black_24dp, "+11 (123) 444 555 666", modifier )
        CardDetailsItem( R.drawable.ic_share, "@AndroidDev", modifier )
        CardDetailsItem( R.drawable.ic_email, "abc@gmail.com", modifier )
    }
}

@Composable
fun CardDetailsItem(iconId: Int, text: String, modifier: Modifier) {
    val iconColor =  ("#2e8557").substring(1)

    Row {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = modifier,
            tint = Color(android.graphics.Color.parseColor("#$iconColor"))
        )
        Text(
            text = text,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, name = "BusinessCardActivity")
@Composable
fun BusinessCardPreview() {
    LearningTheme {
        BusinessCard(
//            message1 = "All tasks completed",
//            message2 = "Nice work!",
            modifier = Modifier.padding(0.dp)
        )
    }
}