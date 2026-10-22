package com.example.restauranteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restauranteapp.navigation.AppNavHost
import com.example.restauranteapp.navigation.BottomMenu
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.navigation.AppNavHost
import com.example.restauranteapp.navigation.BottomMenu
import com.example.restauranteapp.navigation.Routes


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
    val currentRoute = navBackStackEntry?.destination?.route
    val showMenu = currentRoute != Routes.LOGIN && currentRoute != Routes.CADASTRO

    Scaffold(
        bottomBar = {
            if (showMenu) {
                BottomMenu(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}