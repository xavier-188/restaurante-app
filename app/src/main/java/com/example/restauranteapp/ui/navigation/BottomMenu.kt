package com.example.restauranteapp.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController

@Composable
fun BottomMenu(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    // Navega para uma aba sem empilhar cópias: volta até a Home e abre a rota (uma só vez)
    fun irPara(rota: Any) {
        navController.navigate(rota) {
            popUpTo(Routes.Home)
            launchSingleTop = true
        }
    }

    // Mesas e Sobre são abertas pela aba "Mais", então ela fica marcada nessas telas também
    val emMais = currentDestination?.hasRoute(Routes.Config::class) == true ||
            currentDestination?.hasRoute(Routes.Mesas::class) == true ||
            currentDestination?.hasRoute(Routes.Sobre::class) == true

    NavigationBar {
        NavigationBarItem(
            selected = currentDestination?.hasRoute(Routes.Home::class) == true,
            onClick = { irPara(Routes.Home) },
            label = { Text("Início") },
            icon = { Text("🏠") }
        )
        NavigationBarItem(
            selected = currentDestination?.hasRoute(Routes.Cardapio::class) == true,
            onClick = { irPara(Routes.Cardapio) },
            label = { Text("Cardápio") },
            icon = { Text("🍽️") }
        )
        NavigationBarItem(
            selected = currentDestination?.hasRoute(Routes.Reservas::class) == true,
            onClick = { irPara(Routes.Reservas) },
            label = { Text("Reservas") },
            icon = { Text("📅") }
        )
        NavigationBarItem(
            selected = currentDestination?.hasRoute(Routes.Avaliacoes::class) == true,
            onClick = { irPara(Routes.Avaliacoes) },
            label = { Text("Avaliações") },
            icon = { Text("⭐") }
        )
        NavigationBarItem(
            selected = emMais,
            onClick = { irPara(Routes.Config) },
            label = { Text("Mais") },
            icon = { Text("☰") }
        )
    }
}
