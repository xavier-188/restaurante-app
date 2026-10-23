package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToCadastro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Ao abrir a tela, apaga qualquer erro que tenha sobrado de outra tela
    LaunchedEffect(Unit) {
        authViewModel.limparErro()
    }

    // Roda de novo sempre que usuarioLogado mudar: se virou um usuário, entra no app
    LaunchedEffect(authViewModel.usuarioLogado) {
        if (authViewModel.usuarioLogado != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Mensagem de erro vinda do ViewModel (só aparece quando existe)
        authViewModel.erro?.let { mensagem ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mensagem, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.login(email, senha) },
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToCadastro) {
            Text("Não tem uma conta? Cadastre-se", color = Terracotta)
        }
    }
}
