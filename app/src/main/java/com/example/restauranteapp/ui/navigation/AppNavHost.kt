package com.example.restauranteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restauranteapp.ui.screens.CadastroScreen
import com.example.restauranteapp.ui.screens.CardapioScreen
import com.example.restauranteapp.ui.screens.ConfigScreen
import com.example.restauranteapp.ui.screens.HomeScreen
import com.example.restauranteapp.ui.screens.LoginScreen
import com.example.restauranteapp.ui.screens.SobreScreen
import com.example.restauranteapp.ui.screens.TelaReservas
import com.example.restauranteapp.ui.viewmodel.AuthViewModel
import com.example.restauranteapp.ui.viewmodel.PratoViewModel
import com.example.restauranteapp.ui.viewmodel.ReservaViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    pratoViewModel: PratoViewModel,
    reservaViewModel: ReservaViewModel,
    modifier: Modifier = Modifier
) {
    // Decidido uma única vez, ao abrir o app: se já existe sessão salva, pula o login.
    // O remember evita que o destino inicial mude depois que o usuário fizer login.
    val telaInicial = remember {
        if (authViewModel.usuarioLogado != null) Routes.HOME else Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = telaInicial,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
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
                authViewModel = authViewModel,
                onCadastroSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) { HomeScreen() }

        composable(Routes.CARDAPIO) { CardapioScreen(pratoViewModel = pratoViewModel) }

        composable(Routes.RESERVAS) {
            TelaReservas(
                reservaViewModel = reservaViewModel,
                usuarioId = authViewModel.usuarioLogado?.id ?: 0
            )
        }

        composable(Routes.SOBRE) { SobreScreen() }

        composable(Routes.CONFIG) {
            ConfigScreen(
                nome = authViewModel.usuarioLogado?.nome ?: "",
                email = authViewModel.usuarioLogado?.email ?: "",
                onLogout = {
                    authViewModel.logout()
                    // Limpa toda a pilha (a Home é sempre a base) e volta para o login
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
