package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.model.Usuario
import com.example.restauranteapp.data.dao.UsuarioDao
import com.example.restauranteapp.data.local.DatabaseHelper
import java.security.MessageDigest

// AndroidViewModel recebe o application, usado para abrir o banco e criar o DAO (igual ao exemplo da aula)
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UsuarioDao(DatabaseHelper(application))

    var usuarioLogado by mutableStateOf<Usuario?>(dao.buscarUsuarioDaSessao())
        private set

    var erro by mutableStateOf<String?>(null)
        private set

    fun limparErro() {
        erro = null
    }

    fun login(email: String, senha: String) {
        val emailLimpo = email.trim().lowercase()

        if (emailLimpo.isEmpty() || senha.isEmpty()) {
            erro = "Preencha e-mail e senha."
            return
        }

        val usuario = dao.buscarPorEmail(emailLimpo)
        if (usuario == null || usuario.senha != gerarHash(senha)) {
            erro = "E-mail ou senha incorretos."
            return
        }

        dao.salvarSessao(usuario.id)
        erro = null
        usuarioLogado = usuario
    }

    fun cadastrar(nome: String, email: String, senha: String, confirmarSenha: String) {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        if (nomeLimpo.isEmpty() || emailLimpo.isEmpty() || senha.isEmpty()) {
            erro = "Preencha todos os campos."
            return
        }
        if (!emailLimpo.contains("@") || !emailLimpo.contains(".")) {
            erro = "Digite um e-mail válido."
            return
        }
        if (senha.length < 6) {
            erro = "A senha deve ter pelo menos 6 caracteres."
            return
        }
        if (senha != confirmarSenha) {
            erro = "As senhas não conferem."
            return
        }
        if (dao.buscarPorEmail(emailLimpo) != null) {
            erro = "Já existe uma conta com esse e-mail."
            return
        }

        val novo = Usuario(nome = nomeLimpo, email = emailLimpo, senha = gerarHash(senha))
        val id = dao.inserir(novo)
        if (id == -1L) {
            erro = "Não foi possível cadastrar. Tente novamente."
            return
        }

        dao.salvarSessao(id.toInt())
        erro = null
        usuarioLogado = novo.copy(id = id.toInt())
    }

    fun logout() {
        dao.encerrarSessao()
        usuarioLogado = null
    }

    private fun gerarHash(senha: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(senha.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
