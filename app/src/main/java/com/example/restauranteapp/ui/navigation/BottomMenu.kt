package com.example.restauranteapp.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomMenu(
    navController: NavHostController,
    currentRoute: String?
) {
    // Navega para uma aba sem empilhar cópias: volta até a Home e abre a rota (uma só vez)
    fun irPara(rota: String) {
        navController.navigate(rota) {
            popUpTo(Routes.HOME)
            launchSingleTop = true
        }
    }

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = { irPara(Routes.HOME) },
            label = { Text("Início") },
            icon = { Text("🏠") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.CARDAPIO,
            onClick = { irPara(Routes.CARDAPIO) },
            label = { Text("Cardápio") },
            icon = { Text("🍽️") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.RESERVAS,
            onClick = { irPara(Routes.RESERVAS) },
            label = { Text("Reservas") },
            icon = { Text("📅") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.SOBRE,
            onClick = { irPara(Routes.SOBRE) },
            label = { Text("Sobre") },
            icon = { Text("ℹ️") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.CONFIG,
            onClick = { irPara(Routes.CONFIG) },
            label = { Text("Config") },
            icon = { Text("⚙️") }
        )
    }
}
