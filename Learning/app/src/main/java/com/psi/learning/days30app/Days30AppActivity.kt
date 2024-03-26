package com.psi.learning.days30app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.learning.R
import com.psi.learning.days30app.models.DayList
import com.psi.learning.superheroes.HeroScreenTopAppBar
import com.psi.learning.superheroes.HeroesScreen
import com.psi.learning.superheroes.SuperHeroesApp
import com.psi.learning.superheroes.model.HeroesRepository
import com.psi.learning.ui.themehero.SuperheroesTheme

class Days30AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SuperheroesTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DaysInMonthScreenApp()
                }
            }
        }
    }
}

@Composable
fun DaysInMonthScreenApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DayInMonthScreenTopAppBar()
        }
    ) {
        DaysInMonthScreen(dayList = DayList.days, contentPadding = it)
    }
}

@Composable
fun DayInMonthScreenTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "30 Days Of Wellness",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun Days30AppReview() {
    SuperheroesTheme(darkTheme = true) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            DaysInMonthScreenApp()
        }
    }

}
