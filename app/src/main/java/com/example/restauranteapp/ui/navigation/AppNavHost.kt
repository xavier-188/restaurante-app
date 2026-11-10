package com.example.restauranteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restauranteapp.ui.screens.AvaliacoesScreen
import com.example.restauranteapp.ui.screens.CadastroScreen
import com.example.restauranteapp.ui.screens.CardapioScreen
import com.example.restauranteapp.ui.screens.ConfigScreen
import com.example.restauranteapp.ui.screens.HomeScreen
import com.example.restauranteapp.ui.screens.LoginScreen
import com.example.restauranteapp.ui.screens.MesasScreen
import com.example.restauranteapp.ui.screens.SobreScreen
import com.example.restauranteapp.ui.screens.TelaReservas
import com.example.restauranteapp.ui.viewmodel.AuthViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Criado aqui, fora das rotas, para ser UM só para o app inteiro:
    // login, cadastro, reservas, avaliações e config enxergam o mesmo usuário logado.
    // Os outros ViewModels são criados dentro de cada tela com viewModel().
    val authViewModel: AuthViewModel = viewModel()

    // Decidido uma única vez, ao abrir o app: se já existe sessão salva, pula o login.
    // O remember evita que o destino inicial mude depois que o usuário fizer login.
    val telaInicial: Any = remember {
        if (authViewModel.usuarioLogado != null) Routes.Home else Routes.Login
    }

    NavHost(
        navController = navController,
        startDestination = telaInicial,
        modifier = modifier
    ) {
        composable<Routes.Login> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onNavigateToCadastro = { navController.navigate(Routes.Cadastro) }
            )
        }

        composable<Routes.Cadastro> {
            CadastroScreen(
                authViewModel = authViewModel,
                onCadastroSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable<Routes.Home> { HomeScreen() }

        composable<Routes.Cardapio> { CardapioScreen() }

        composable<Routes.Reservas> {
            TelaReservas(
                usuarioId = authViewModel.usuarioLogado?.id ?: 0
            )
        }

        composable<Routes.Avaliacoes> {
            AvaliacoesScreen(
                nomeUsuario = authViewModel.usuarioLogado?.nome ?: "",
                usuarioId = authViewModel.usuarioLogado?.id
            )
        }

        composable<Routes.Mesas> { MesasScreen() }

        composable<Routes.Sobre> { SobreScreen() }

        composable<Routes.Config> {
            ConfigScreen(
                nome = authViewModel.usuarioLogado?.nome ?: "",
                email = authViewModel.usuarioLogado?.email ?: "",
                onAbrirMesas = { navController.navigate(Routes.Mesas) },
                onAbrirSobre = { navController.navigate(Routes.Sobre) },
                onLogout = {
                    authViewModel.logout()
                    // Limpa toda a pilha (a Home é sempre a base) e volta para o login
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        }
    }
}
