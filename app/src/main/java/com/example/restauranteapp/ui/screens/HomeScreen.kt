package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.data.model.Receita
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.viewmodel.HomeViewModel

// Parte "conectada": fala com o ViewModel
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    // Busca a sugestão do chef na API (Retrofit) quando a tela abre, só se ainda não tiver uma
    LaunchedEffect(Unit) {
        if (homeViewModel.sugestao == null) {
            homeViewModel.carregarSugestao()
        }
    }

    HomeContent(
        sugestao = homeViewModel.sugestao,
        carregando = homeViewModel.carregando,
        erro = homeViewModel.erro,
        onNovaSugestao = { homeViewModel.carregarSugestao() }
    )
}

// Parte "visual": só recebe dados e callbacks (por isso funciona no Preview)
@Composable
fun HomeContent(
    sugestao: Receita?,
    carregando: Boolean,
    erro: String?,
    onNovaSugestao: () -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(16.dp)
            ) {
                Text("Início", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(16.dp)
            ) {
                Text("BEM-VINDO AO", color = CoralLight, fontSize = 12.sp)
                Text("Restaurante Bella Vista", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Sabor e tradição desde 1998", color = Color.White, fontSize = 13.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem("ABERTURA", "11h – 23h")
                InfoItem("FUNCIONAMENTO", "Seg – Dom")
                InfoItem("MESAS", "40 lugares")
            }

            Text(
                "DESTAQUES DO DIA",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray
            )

            DishItem("Filé à Parmegiana", "Filé grelhado com molho de tomate e queijo gratinado", "R$ 52,00")
            DishItem("Risoto de Camarão", "Camarões salteados com arroz arbóreo ao molho cremoso", "R$ 68,00")
            DishItem("Frango Grelhado", "Peito de frango grelhado com ervas finas e limão siciliano", "R$ 44,00")

            SugestaoDoChef(sugestao, carregando, erro, onNovaSugestao)

            Localizacao()
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DishItem(nome: String, descricao: String, preco: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(nome, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(descricao, fontSize = 12.sp, color = Color.Gray)
        }
        Text(preco, color = Terracotta, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
// Card com a receita vinda da API TheMealDB via Retrofit
@Composable
fun SugestaoDoChef(
    sugestao: Receita?,
    carregando: Boolean,
    erro: String?,
    onNovaSugestao: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF7E7DF), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text("INSPIRAÇÃO DO CHEF", fontSize = 11.sp, color = Terracotta, fontWeight = FontWeight.Bold)
        Text("Receita do mundo, direto da API TheMealDB", fontSize = 11.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        when {
            carregando -> CircularProgressIndicator(color = Terracotta)
            erro != null -> Text(erro, fontSize = 13.sp, color = Color.Red)
            sugestao != null -> {
                Text(sugestao.strMeal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    listOfNotNull(sugestao.strCategory, sugestao.strArea).joinToString(" • "),
                    fontSize = 12.sp,
                    color = Terracotta
                )
                sugestao.strInstructions?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it.take(160) + if (it.length > 160) "..." else "", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        TextButton(onClick = onNovaSugestao, enabled = !carregando) {
            Text("Nova sugestão", color = Terracotta)
        }
    }
}

@Composable
fun Localizacao(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text("LOCALIZAÇÃO", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Rua das Acácias, 248 – Centro", fontSize = 13.sp, color = Color(0xFF1B3A6B))
        Text("Curitiba – PR, 01310-100", fontSize = 13.sp, color = Color(0xFF1B3A6B))
        Spacer(modifier = Modifier.height(4.dp))
        Text("(41) 3456-7890", fontSize = 13.sp, color = Terracotta, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RestauranteAppTheme {
        HomeContent(
            sugestao = Receita("1", "Spaghetti Bolognese", "Pasta", "Italian", "Cozinhe o macarrão..."),
            carregando = false,
            erro = null,
            onNovaSugestao = {}
        )
    }
}