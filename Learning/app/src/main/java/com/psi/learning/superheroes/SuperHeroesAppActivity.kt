package com.psi.learning.superheroes


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.learning.superheroes.model.HeroesRepository
import com.psi.learning.ui.themehero.SuperheroesTheme

class SuperHeroesAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SuperheroesTheme {
                SuperheroesTheme(darkTheme = false) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SuperHeroesApp()
                    }
                }
            }
        }

    }
}

@Composable
fun SuperHeroesApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeroScreenTopAppBar()
        }
    ) {
        /* Important: It is not a good practice to access data source directly from the UI.
        In later units you will learn how to use ViewModel in such scenarios that takes the
        data source as a dependency and exposes heroes.
         */
        val heroes = HeroesRepository.heroes
        HeroesScreen(heroes = heroes, contentPadding = it)
    }
}


@Composable
fun HeroScreenTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Supperheroes",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}
