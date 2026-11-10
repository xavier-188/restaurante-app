package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restauranteapp.data.model.Receita
import com.example.restauranteapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Serviço do Retrofit (igual ao ViaCepApi.service do exemplo da aula)
    private val api = RetrofitClient.receitaApi

    var sugestao by mutableStateOf<Receita?>(null)
        private set

    var carregando by mutableStateOf(false)
        private set

    var erro by mutableStateOf<String?>(null)
        private set

    // Chamada de rede não pode rodar na thread principal: viewModelScope.launch
    // abre uma coroutine e o Retrofit (função suspend) faz a requisição em segundo plano.
    fun carregarSugestao() {
        viewModelScope.launch {
            carregando = true
            erro = null
            try {
                sugestao = api.buscarReceitaAleatoria().meals?.firstOrNull()
            } catch (e: Exception) {
                erro = "Não foi possível carregar a sugestão. Verifique a internet."
            }
            carregando = false
        }
    }
}
