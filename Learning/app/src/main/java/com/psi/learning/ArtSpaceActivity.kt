package com.psi.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psi.learning.ui.theme.LearningTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight

class ArtSpaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Surface(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                ArtSpaceLayout(
                    modifier = Modifier.width(300.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtSpaceLayout(modifier: Modifier) {

    var result by remember { mutableStateOf(1) }

    val imageId: Int = when(result) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            ArtSpaceImage(imageId, modifier)

            Spacer(modifier = Modifier.height(16.dp))

            ArtSpaceTitle("Sailing Under the Bridge", "Kat Kuan", "2017", modifier = modifier)

            Spacer(modifier = Modifier.height(16.dp))

            ArtSpaceButtons(result, modifier){ idx ->
                result = idx
            }
        }
    }
}

@Composable
fun ArtSpaceImage(imageResId: Int, modifier: Modifier) {
    Surface(
        modifier = modifier
            .height(300.dp),
        shadowElevation = 10.dp, // Adjust the elevation for the shadow intensity
        color = Color.White // Background color of the surface
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null
            )
        }
    }
}


@Composable
fun ArtSpaceTitle(title: String, artist: String, year: String, modifier: Modifier) {
    Box (
        modifier = Modifier
            .height(50.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column{
            Text(
                text = title,
                modifier = modifier
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = artist,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "($year)",
                    modifier = modifier.padding(3.dp),
                    color = Color.Gray
                )
            }
        }
    }

}

@Composable
fun ArtSpaceButtons(idx: Int, modifier : Modifier, onUpdateCount: (Int) -> Unit) {
    Box (
        modifier = modifier
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            //        horizontalArrangement = Arrangement.Vertical
        ) {
            Button(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                onClick = {
                    if(idx == 1)  onUpdateCount(idx)
                    else  onUpdateCount(idx - 1)
                }
            ) {
                Text("Previous", fontSize = 24.sp)
            }

            Button(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                onClick = {
                    if(idx == 4)  onUpdateCount(idx)
                    else  onUpdateCount(idx + 1)
                }
            ) {
                Text("Next", fontSize = 24.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "BusinessCardActivity")
@Composable
fun ArtSpacePreview() {
    LearningTheme {
        ArtSpaceLayout(
            modifier = Modifier.width(300.dp)
        )
    }
}