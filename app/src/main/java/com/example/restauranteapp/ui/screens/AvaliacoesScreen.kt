package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restauranteapp.data.model.Avaliacao
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.viewmodel.AvaliacaoViewModel

// Parte "conectada": fala com o ViewModel
@Composable
fun AvaliacoesScreen(
    nomeUsuario: String,
    usuarioId: Int?,
    avaliacaoViewModel: AvaliacaoViewModel = viewModel()
) {
    // Carrega as avaliações do banco quando a tela abre
    LaunchedEffect(Unit) {
        avaliacaoViewModel.carregar()
    }

    AvaliacoesContent(
        avaliacoes = avaliacaoViewModel.avaliacoes,
        usuarioId = usuarioId,
        onSalvar = { id, nota, comentario ->
            avaliacaoViewModel.salvar(id, nomeUsuario, usuarioId, nota, comentario)
        },
        onExcluir = { id -> avaliacaoViewModel.excluir(id) }
    )
}

// Parte "visual": só recebe dados e callbacks (por isso funciona no Preview)
@Composable
fun AvaliacoesContent(
    avaliacoes: List<Avaliacao>,
    usuarioId: Int?,
    onSalvar: (Int?, Int, String) -> String?,
    onExcluir: (Int) -> Unit
) {
    var editandoId by remember { mutableStateOf<Int?>(null) }
    var nota by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    fun limparFormulario() {
        editandoId = null
        nota = 0
        comentario = ""
        erro = null
    }

    val media = if (avaliacoes.isEmpty()) 0.0 else avaliacoes.map { it.nota }.average()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text("Avaliações", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    if (avaliacoes.isEmpty()) "Seja o primeiro a avaliar"
                    else "Nota média " + "%.1f".format(media).replace('.', ',') + " ★ (${avaliacoes.size})",
                    color = CoralLight,
                    fontSize = 13.sp
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Terracotta, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (editandoId == null) "DEIXE SUA AVALIAÇÃO" else "EDITANDO AVALIAÇÃO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta
                    )

                    // Estrelas clicáveis: a nota é a posição da estrela tocada
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (i in 1..5) {
                            Text(
                                text = if (i <= nota) "★" else "☆",
                                fontSize = 32.sp,
                                color = Terracotta,
                                modifier = Modifier.clickable { nota = i }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Comentário") },
                        placeholder = { Text("Conte como foi sua experiência...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    erro?.let { mensagem ->
                        Text(text = mensagem, color = Color.Red, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val resultado = onSalvar(editandoId, nota, comentario)
                            if (resultado == null) limparFormulario() else erro = resultado
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editandoId == null) "Publicar avaliação" else "Salvar alterações")
                    }

                    if (editandoId != null) {
                        TextButton(
                            onClick = { limparFormulario() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelar edição", color = Terracotta)
                        }
                    }
                }
            }

            if (avaliacoes.isEmpty()) {
                item {
                    Text("Ainda não há avaliações.", fontSize = 13.sp, color = Color.Gray)
                }
            }

            items(avaliacoes, key = { it.id }) { avaliacao ->
                AvaliacaoItem(
                    avaliacao = avaliacao,
                    // Cada usuário só pode alterar/excluir as próprias avaliações
                    podeEditar = avaliacao.usuarioId == usuarioId,
                    onEditar = {
                        editandoId = avaliacao.id
                        nota = avaliacao.nota
                        comentario = avaliacao.comentario
                        erro = null
                    },
                    onExcluir = {
                        if (editandoId == avaliacao.id) limparFormulario()
                        onExcluir(avaliacao.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun AvaliacaoItem(
    avaliacao: Avaliacao,
    podeEditar: Boolean,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(avaliacao.autor, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("★".repeat(avaliacao.nota) + "☆".repeat(5 - avaliacao.nota), color = Terracotta, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(avaliacao.comentario, fontSize = 13.sp, color = Color.Gray)

        if (podeEditar) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditar) { Text("Editar", color = Terracotta) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = Color.Red) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvaliacoesScreenPreview() {
    RestauranteAppTheme {
        AvaliacoesContent(
            avaliacoes = listOf(
                Avaliacao(1, "Maria Silva", 5, "Comida excelente e atendimento rápido!", 1),
                Avaliacao(2, "João", 4, "Ótimo risoto.", 2)
            ),
            usuarioId = 1,
            onSalvar = { _, _, _ -> null },
            onExcluir = {}
        )
    }
}
