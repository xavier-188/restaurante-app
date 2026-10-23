package com.example.restauranteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.ui.navigation.AppNavHost
import com.example.restauranteapp.ui.navigation.BottomMenu
import com.example.restauranteapp.ui.navigation.Routes
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.viewmodel.AppViewModelFactory
import com.example.restauranteapp.ui.viewmodel.AuthViewModel
import com.example.restauranteapp.ui.viewmodel.PratoViewModel
import com.example.restauranteapp.ui.viewmodel.ReservaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cria o banco e a fábrica que monta os ViewModels (cada um recebe seu Repository)
        val helper = DatabaseHelper(this)
        val factory = AppViewModelFactory(helper)

        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        val pratoViewModel = ViewModelProvider(this, factory)[PratoViewModel::class.java]
        val reservaViewModel = ViewModelProvider(this, factory)[ReservaViewModel::class.java]

        setContent {
            RestauranteAppTheme {
                MainApp(authViewModel, pratoViewModel, reservaViewModel)
            }
        }
    }
}

@Composable
fun MainApp(
    authViewModel: AuthViewModel,
    pratoViewModel: PratoViewModel,
    reservaViewModel: ReservaViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Menu escondido no login e no cadastro (e no primeiro instante, enquanto a rota ainda é null)
    val showMenu = currentRoute != null &&
            currentRoute != Routes.LOGIN &&
            currentRoute != Routes.CADASTRO

    Scaffold(
        bottomBar = {
            if (showMenu) {
                BottomMenu(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            authViewModel = authViewModel,
            pratoViewModel = pratoViewModel,
            reservaViewModel = reservaViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
