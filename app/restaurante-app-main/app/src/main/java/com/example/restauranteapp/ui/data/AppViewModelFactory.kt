package com.example.restauranteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restauranteapp.data.DatabaseHelper
import com.example.restauranteapp.data.ReservaRepository
// TODO: adicionar os imports de cada Repository e ViewModel conforme forem ficando prontos

class AppViewModelFactory(private val helper: DatabaseHelper) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Cada integrante descomenta a linha do seu ViewModel quando ele existir:
        //
        // if (modelClass.isAssignableFrom(AuthViewModel::class.java))
        //     return AuthViewModel(UsuarioRepository(helper)) as T
        //
        // if (modelClass.isAssignableFrom(PratoViewModel::class.java))
        //     return PratoViewModel(PratoRepository(helper)) as T
        //
        if (modelClass.isAssignableFrom(ReservaViewModel::class.java))
            return ReservaViewModel(ReservaRepository(helper)) as T

        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}