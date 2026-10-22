package com.example.restauranteapp.navigation

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
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = { navController.navigate(Routes.HOME) },
            label = { Text("Início") },
            icon = { Text("🏠") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.CARDAPIO,
            onClick = { navController.navigate(Routes.CARDAPIO) },
            label = { Text("Cardápio") },
            icon = { Text("🍽️") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.RESERVAS,
            onClick = { navController.navigate(Routes.RESERVAS) },
            label = { Text("Reservas") },
            icon = { Text("🍽️") }
        )
        NavigationBarItem(
            selected = currentRoute == Routes.SOBRE,
            onClick = { navController.navigate(Routes.SOBRE) },
            label = { Text("Sobre") },
            icon = { Text("🍽️") }
        )

    }
}