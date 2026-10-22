package com.example.restauranteapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restauranteapp.ui.screens.*
import com.seupacote.restauranteapp.ui.screens.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToCadastro = { navController.navigate(Routes.CADASTRO) }
            )
        }

        composable(Routes.CADASTRO) {
            CadastroScreen(
                onCadastroSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) { HomeScreen() }
        composable(Routes.CARDAPIO) { CardapioScreen() }
        composable(Routes.RESERVAS) { TelaReservas() }
        composable(Routes.SOBRE) { SobreScreen() }
    }
}