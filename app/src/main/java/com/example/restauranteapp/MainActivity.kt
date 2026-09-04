package com.example.restauranteapp

import com.example.restauranteapp.ui.screens.TelaReservas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.restauranteapp.ui.screens.CardapioScreen
import com.example.restauranteapp.ui.screens.SobreScreen
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.seupacote.restauranteapp.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestauranteAppTheme {
                HomeScreen()
                CardapioScreen()
                TelaReservas()
                SobreScreen()
            }
        }
    }
}
