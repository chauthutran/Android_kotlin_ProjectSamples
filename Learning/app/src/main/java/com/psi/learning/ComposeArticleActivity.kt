package com.psi.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.psi.learning.ui.theme.WoofTheme

class ComposeArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                Article(
                    title = "Jetpack Compose tutorial",
                    content1 = "Jetpack Compose is model toolkit for building native Android UI. Compose simplifies and accelerates UI development on Android with less code, powerful tool, and intuitive Kotlin APIs.",
                    content2 = "In this tutorial, you build a simple UI component with declarative function. You call Compose functions to say what elements you want and the Compose compiler does the rest. Compose is built around Composable functions. These functions let you define your app's UI programmatically because they let you described how it should look and provide daa dependencies, rather than focus on the process of the UI's construction, such as initializing an element and then attaching it to a parent. To create a composable function, you add the @Composable annotation to the function name.",
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun Article( title: String, content1: String, content2: String, modifier: Modifier) {
    Column(
        modifier = modifier
    ){
        ArticleImage(modifier)

        Text (
            text = title,
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Justify
        )

        Text (
            text = content1,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Justify
        )

        Text (
            text = content2,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun ArticleImage(modifier: Modifier) {
    val image = painterResource(R.drawable.bg_compose_background)
    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier.padding(0.dp)
    )
}

@Preview(showBackground = true, name="Actical Preview")
@Composable
fun ArticlePreview() {
    WoofTheme {
        Article(
            title = "Jetpack Compose tutorial",
            content1 = "Jetpack Compose is model toolkit for building native Android UI. Compose simplifies and accelerates UI development on Android with less code, powerful tool, and intuitive Kotlin APIs.",
            content2 = "In this tutorial, you build a simple UI component with declarative function. You call Compose functions to say what elements you want and the Compose compiler does the rest. Compose is built around Composable functions. These functions let you define your app's UI programmatically because they let you described how it should look and provide daa dependencies, rather than focus on the process of the UI's construction, such as initializing an element and then attaching it to a parent. To create a composable function, you add the @Composable annotation to the function name.",
            modifier = Modifier.padding(5.dp)
        )
    }
}
