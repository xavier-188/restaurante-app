package com.example.restauranteapp.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restauranteapp.data.ImagemConversor
import com.example.restauranteapp.data.model.Prato
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.viewmodel.PratoViewModel

// Categorias para filtrar a lista (com "Todos") e para escolher no formulário (sem "Todos")
private val categoriasFiltro = listOf("Todos", "Entradas", "Pratos", "Lanches", "Sobremesas", "Bebidas")
private val categoriasForm = categoriasFiltro.drop(1)

// Parte "conectada": fala com o ViewModel
@Composable
fun CardapioScreen(pratoViewModel: PratoViewModel = viewModel()) {
    // Carrega os pratos do banco quando a tela abre
    LaunchedEffect(Unit) {
        pratoViewModel.carregar()
    }

    CardapioContent(
        pratos = pratoViewModel.pratos,
        onSalvar = { id, nome, descricao, preco, categoria, emoji, fotoNova ->
            pratoViewModel.salvar(id, nome, descricao, preco, categoria, emoji, fotoNova)
        },
        onExcluir = { id -> pratoViewModel.excluir(id) }
    )
}

// Parte "visual": só recebe dados e callbacks (por isso funciona no Preview)
@Composable
fun CardapioContent(
    pratos: List<Prato>,
    onSalvar: (Int?, String, String, String, String, String, Bitmap?) -> String?,
    onExcluir: (Int) -> Unit
) {
    var categoriaFiltro by remember { mutableStateOf("Todos") }

    // Estado do formulário de prato
    var mostrarForm by remember { mutableStateOf(false) }
    var editandoId by remember { mutableStateOf<Int?>(null) }
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Pratos") }
    var emoji by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    // Foto: a que o prato já tem (bytes do banco) e a que acabou de ser tirada (ainda não salva)
    var fotoAtual by remember { mutableStateOf<ByteArray?>(null) }
    var fotoNova by remember { mutableStateOf<Bitmap?>(null) }

    // Recurso nativo: abre a câmera do celular e devolve a foto como Bitmap
    // (TakePicturePreview não precisa de permissão nem de FileProvider)
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) fotoNova = bitmap // null = usuário cancelou
    }

    fun fecharFormulario() {
        mostrarForm = false
        editandoId = null
        nome = ""
        descricao = ""
        preco = ""
        categoria = "Pratos"
        emoji = ""
        erro = null
        fotoAtual = null
        fotoNova = null
    }

    val filtrados = if (categoriaFiltro == "Todos") {
        pratos
    } else {
        pratos.filter { it.categoria == categoriaFiltro }
    }

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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Filtro por categoria
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriasFiltro.forEach { cat ->
                        CategoriaChip(
                            nome = cat,
                            selecionada = cat == categoriaFiltro,
                            onClick = { categoriaFiltro = cat }
                        )
                    }
                }
            }

            // Botão "Novo prato" ou o formulário
            item {
                if (!mostrarForm) {
                    Button(
                        onClick = { mostrarForm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Novo prato")
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Terracotta, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (editandoId == null) "NOVO PRATO" else "EDITANDO PRATO",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Terracotta
                        )

                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = descricao,
                            onValueChange = { descricao = it },
                            label = { Text("Descrição") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = preco,
                                onValueChange = { preco = it },
                                label = { Text("Preço (R$)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = emoji,
                                onValueChange = { emoji = it },
                                label = { Text("Emoji") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text("FOTO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Mostra a foto nova, se tirou; senão a que o prato já tinha
                            val preview = fotoNova?.asImageBitmap() ?: carregarFoto(fotoAtual)
                            if (preview != null) {
                                Image(
                                    bitmap = preview,
                                    contentDescription = "Foto do prato",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            OutlinedButton(onClick = { cameraLauncher.launch(null) }) {
                                Text(
                                    if (preview == null) "📷 Tirar foto" else "📷 Tirar outra",
                                    color = Terracotta
                                )
                            }
                        }

                        Text("CATEGORIA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categoriasForm.forEach { cat ->
                                CategoriaChip(
                                    nome = cat,
                                    selecionada = cat == categoria,
                                    onClick = { categoria = cat }
                                )
                            }
                        }

                        erro?.let { mensagem ->
                            Text(text = mensagem, color = Color.Red, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val resultado = onSalvar(editandoId, nome, descricao, preco, categoria, emoji, fotoNova)
                                if (resultado == null) {
                                    fecharFormulario()
                                } else {
                                    erro = resultado
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (editandoId == null) "Salvar prato" else "Salvar alterações")
                        }

                        TextButton(
                            onClick = { fecharFormulario() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelar", color = Terracotta)
                        }
                    }
                }
            }

            if (filtrados.isEmpty()) {
                item {
                    Text("Nenhum prato nesta categoria.", fontSize = 13.sp, color = Color.Gray)
                }
            }

            items(filtrados, key = { it.id }) { prato ->
                PratoItem(
                    prato = prato,
                    onEditar = {
                        nome = prato.nome
                        descricao = prato.descricao
                        preco = "%.2f".format(prato.preco).replace('.', ',')
                        categoria = prato.categoria
                        emoji = prato.emoji
                        fotoAtual = prato.foto
                        fotoNova = null
                        editandoId = prato.id
                        erro = null
                        mostrarForm = true
                    },
                    onExcluir = {
                        if (editandoId == prato.id) fecharFormulario()
                        onExcluir(prato.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoriaChip(nome: String, selecionada: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(
                color = if (selecionada) Terracotta else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(1.dp, Terracotta, RoundedCornerShape(20.dp))
            .clickable { onClick() }
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
private fun PratoItem(
    prato: Prato,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // Lê o arquivo só quando o caminho muda (remember), não a cada recomposição
        val foto = remember(prato.foto) { carregarFoto(prato.foto) }
        if (foto != null) {
            Image(
                bitmap = foto,
                contentDescription = prato.nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
        } else {
            Column(
                modifier = Modifier
                    .background(Color(0xFFF7E7DF), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Text(prato.emoji, fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(prato.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(prato.descricao, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "R$ " + "%.2f".format(prato.preco).replace('.', ','),
                color = Terracotta,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEditar) { Text("Editar", color = Terracotta) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = Color.Red) }
            }
        }
    }
}

// Converte os bytes da foto (BLOB do banco) em imagem para o Compose (ou null se não tem foto)
private fun carregarFoto(foto: ByteArray?): ImageBitmap? {
    if (foto == null) return null
    return ImagemConversor.paraBitmap(foto)?.asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun CardapioScreenPreview() {
    RestauranteAppTheme {
        CardapioContent(
            pratos = listOf(
                Prato(1, "Filé à Parmegiana", "Filé empanado, molho de tomate e queijo gratinado.", 52.0, "Pratos", "🥩"),
                Prato(2, "Pudim da Casa", "Pudim cremoso com calda de caramelo.", 18.0, "Sobremesas", "🍮")
            ),
            onSalvar = { _, _, _, _, _, _, _ -> null },
            onExcluir = {}
        )
    }
}
