package com.example.restauranteapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restauranteapp.data.DatabaseHelper
import com.example.restauranteapp.viewmodel.AppViewModelFactory
import com.example.restauranteapp.viewmodel.ReservaViewModel
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
        composable(Routes.RESERVAS) {
            val context = LocalContext.current
            val helper = remember { DatabaseHelper(context.applicationContext) }
            val reservaViewModel: ReservaViewModel = viewModel(factory = AppViewModelFactory(helper))
            TelaReservas(reservaViewModel)
        }
        composable(Routes.SOBRE) { SobreScreen() }
    }
}