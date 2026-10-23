package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta

// Tela "sem estado": recebe os dados prontos e avisa quando o botão é clicado.
// Assim ela também funciona no Preview, sem precisar de ViewModel.
@Composable
fun ConfigScreen(
    nome: String,
    email: String,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(16.dp)
            ) {
                Text("Configurações", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("CONTA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(email, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("SAIR DA CONTA", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    RestauranteAppTheme {
        ConfigScreen(nome = "Maria Silva", email = "maria@email.com", onLogout = {})
    }
}
