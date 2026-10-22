package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta

@Composable
fun SobreScreen() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    "Sobre nós",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Conheça o Bella Vista",
                    color = CoralLight,
                    fontSize = 13.sp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Restaurante Bella Vista",
                color = Terracotta,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Sabor e tradição desde 1998",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "O Bella Vista é um restaurante familiar que une receitas tradicionais, ingredientes selecionados e um ambiente agradável para receber você, sua família e seus amigos.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            InformacaoSobre("Nossa missão", "Oferecer uma experiência saborosa, acolhedora e de qualidade em cada visita.")

            Spacer(modifier = Modifier.height(12.dp))

            InformacaoSobre("Horário de funcionamento", "Segunda a domingo, das 11h às 23h.")

            Spacer(modifier = Modifier.height(12.dp))

            InformacaoSobre("Onde estamos", "Rua das Acácias, 248 – Centro, Curitiba – PR.")

            Spacer(modifier = Modifier.height(12.dp))

            InformacaoSobre("Contato", "(41) 3456-7890")
        }
    }
}

@Composable
private fun InformacaoSobre(titulo: String, texto: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            titulo,
            color = Terracotta,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            texto,
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SobreScreenPreview() {
    RestauranteAppTheme {
        SobreScreen()
    }
}
