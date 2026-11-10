package com.example.restauranteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restauranteapp.ui.navigation.AppNavHost
import com.example.restauranteapp.ui.navigation.BottomMenu
import com.example.restauranteapp.ui.navigation.Routes
import com.example.restauranteapp.ui.theme.RestauranteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestauranteAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Menu escondido no login e no cadastro (e no primeiro instante, enquanto o destino ainda é null)
    val showMenu = currentDestination != null &&
            !currentDestination.hasRoute(Routes.Login::class) &&
            !currentDestination.hasRoute(Routes.Cadastro::class)

    Scaffold(
        bottomBar = {
            if (showMenu) {
                BottomMenu(navController = navController, currentDestination = currentDestination)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
