package com.example.restauranteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.repository.PratoRepository
import com.example.restauranteapp.repository.ReservaRepository
import com.example.restauranteapp.repository.UsuarioRepository

// Ensina o Android a criar cada ViewModel que precisa de um Repository no construtor
class AppViewModelFactory(private val helper: DatabaseHelper) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java))
            return AuthViewModel(UsuarioRepository(helper)) as T

        if (modelClass.isAssignableFrom(PratoViewModel::class.java))
            return PratoViewModel(PratoRepository(helper)) as T

        if (modelClass.isAssignableFrom(ReservaViewModel::class.java))
            return ReservaViewModel(ReservaRepository(helper)) as T

        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
