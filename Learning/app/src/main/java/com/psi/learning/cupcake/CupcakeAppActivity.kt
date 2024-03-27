package com.psi.learning.cupcake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.psi.learning.ui.themehero.SuperheroesTheme

class CupcakeAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SuperheroesTheme {
                CupcakeApp()
            }
        }
    }
}