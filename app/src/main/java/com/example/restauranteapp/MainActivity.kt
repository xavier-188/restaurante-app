package com.example.restauranteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restauranteapp.ui.screens.*
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.seupacote.restauranteapp.ui.screens.HomeScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.example.restauranteapp.ui.theme.Terracotta

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
    val showMenu = currentRoute != "login" && currentRoute != "cadastro"

    Scaffold(
        bottomBar = {
            if (showMenu) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") },
                        label = { Text("Início") },
                        icon = { Text("🏠") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "cardapio",
                        onClick = { navController.navigate("cardapio") },
                        label = { Text("Cardápio") },
                        icon = { Text("🍽️") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "reservas",
                        onClick = { navController.navigate("reservas") },
                        label = { Text("Reservas") },
                        icon = { Text("📅") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "sobre",
                        onClick = { navController.navigate("sobre") },
                        label = { Text("Sobre") },
                        icon = { Text("ℹ️") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToCadastro = {
                        navController.navigate("cadastro")
                    }
                )
            }

            composable("cadastro") {
                CadastroScreen(
                    onCadastroSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable("home") { HomeScreen() }
            composable("cardapio") { CardapioScreen() }
            composable("reservas") { TelaReservas() }
            composable("sobre") { SobreScreen() }
        }
    }
}