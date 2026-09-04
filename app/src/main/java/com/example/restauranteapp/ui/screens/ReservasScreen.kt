package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta

@Composable
fun TelaReservas() {
    // Variáveis de estado do formulário
    var nome by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var pessoas by remember { mutableStateOf("2 pessoas") }
    var observacoes by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            // Topo da tela utilizando Column e background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reservas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            // Barra inferior estruturada com Row para alinhar os itens horizontalmente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Início", fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Cardápio", fontSize = 12.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Reservas", fontSize = 12.sp, color = Terracotta, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Sobre", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    ) { innerPadding ->
        // Estrutura principal utilizando Column para empilhar os elementos do formulário
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo 1: Nome Completo
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "NOME COMPLETO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = { Text("Ex: João Silva") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Linha contendo Data e Horário lado a lado usando Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "DATA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    OutlinedTextField(
                        value = data,
                        onValueChange = { data = it },
                        placeholder = { Text("dd/mm/aaaa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "HORÁRIO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    OutlinedTextField(
                        value = horario,
                        onValueChange = { horario = it },
                        placeholder = { Text("--:--") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Campo 3: Número de Pessoas
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "NÚMERO DE PESSOAS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                OutlinedTextField(
                    value = pessoas,
                    onValueChange = { pessoas = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Campo 4: Observações
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "OBSERVAÇÕES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                OutlinedTextField(
                    value = observacoes,
                    onValueChange = { observacoes = it },
                    placeholder = { Text("Alergias, ocasião especial, preferências...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão de Confirmação do Formulário
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(Terracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "CONFIRMAR RESERVA", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Texto Informativo Rodapé
            Text(
                text = "Nossa equipe entrará em contato para confirmar a reserva.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ReservasScreenPreview() {
    RestauranteAppTheme {
        TelaReservas()
    }
}