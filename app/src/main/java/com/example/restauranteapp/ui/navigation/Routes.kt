package com.example.restauranteapp.ui.navigation

import kotlinx.serialization.Serializable

// Rotas serializadas: cada tela é um objeto marcado com @Serializable.
// O NavHost usa a própria classe como rota (composable<Routes.Home>), sem strings soltas.
object Routes {
    @Serializable
    object Login

    @Serializable
    object Cadastro

    @Serializable
    object Home

    @Serializable
    object Cardapio

    @Serializable
    object Reservas

    @Serializable
    object Mesas

    @Serializable
    object Avaliacoes

    @Serializable
    object Sobre

    @Serializable
    object Config
}
