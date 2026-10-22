package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta

private data class Prato(
    val nome: String,
    val descricao: String,
    val preco: String,
    val emoji: String
)

private val pratos = listOf(
    Prato("Filé à Parmegiana", "Filé empanado, molho de tomate, queijo gratinado e arroz com fritas.", "R$ 52,00", "🥩"),
    Prato("Risoto de Camarão", "Arroz arbóreo cremoso com camarões salteados e ervas frescas.", "R$ 68,00", "🍤"),
    Prato("Frango Grelhado", "Peito de frango grelhado, ervas finas e limão siciliano.", "R$ 44,00", "🍗"),
    Prato("Hambúrguer Bella", "Pão brioche, hambúrguer artesanal, queijo, salada e molho da casa.", "R$ 38,00", "🍔"),
    Prato("Bruschetta Italiana", "Pão italiano tostado, tomate, manjericão e azeite extravirgem.", "R$ 26,00", "🍅"),
    Prato("Pudim da Casa", "Pudim cremoso de leite condensado com calda de caramelo.", "R$ 18,00", "🍮")
)

private val categorias = listOf("Todos", "Entradas", "Pratos", "Lanches", "Sobremesas", "Bebidas")

@Composable
fun CardapioScreen() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text("Nosso cardápio", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Escolha seus pratos favoritos", color = CoralLight, fontSize = 13.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Categorias
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .horizontalScroll(rememberScrollState()),   // ← adiciona isso
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { categoria ->
                    CategoriaChip(nome = categoria, selecionada = categoria == "Todos")
                }
            }

            pratos.forEach { prato ->
                PratoItem(prato)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun CategoriaChip(nome: String, selecionada: Boolean) {
    Column(
        modifier = Modifier
            .background(
                color = if (selecionada) Terracotta else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(1.dp, Terracotta, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            nome,
            color = if (selecionada) Color.White else Terracotta,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PratoItem(prato: Prato) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF7E7DF), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Text(prato.emoji, fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(prato.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(prato.descricao, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text(prato.preco, color = Terracotta, fontSize = 15.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = { /* sem lógica de carrinho por enquanto */ },
                modifier = Modifier.padding(top = 7.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
            ) {
                Text("Adicionar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardapioScreenPreview() {
    RestauranteAppTheme {
        CardapioScreen()
    }
}