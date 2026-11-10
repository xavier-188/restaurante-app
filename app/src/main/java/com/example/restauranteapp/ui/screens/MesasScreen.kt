package com.example.restauranteapp.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restauranteapp.data.model.Mesa
import com.example.restauranteapp.ui.theme.CoralLight
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.viewmodel.MesaViewModel

private val areasMesa = listOf("Salão", "Varanda", "Área externa", "Reservado")

// Parte "conectada": fala com o ViewModel
@Composable
fun MesasScreen(mesaViewModel: MesaViewModel = viewModel()) {
    // Carrega as mesas do banco quando a tela abre
    LaunchedEffect(Unit) {
        mesaViewModel.carregar()
    }

    MesasContent(
        mesas = mesaViewModel.mesas,
        onSalvar = { id, numero, lugares, area -> mesaViewModel.salvar(id, numero, lugares, area) },
        onExcluir = { id -> mesaViewModel.excluir(id) }
    )
}

// Parte "visual": só recebe dados e callbacks (por isso funciona no Preview)
@Composable
fun MesasContent(
    mesas: List<Mesa>,
    onSalvar: (Int?, String, String, String) -> String?,
    onExcluir: (Int) -> Unit
) {
    var editandoId by remember { mutableStateOf<Int?>(null) }
    var numero by remember { mutableStateOf("") }
    var lugares by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("Salão") }
    var erro by remember { mutableStateOf<String?>(null) }

    fun limparFormulario() {
        editandoId = null
        numero = ""
        lugares = ""
        area = "Salão"
        erro = null
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Terracotta)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text("Mesas", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    "${mesas.size} mesas • ${mesas.sumOf { it.lugares }} lugares",
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
            // Formulário de criar / editar
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Terracotta, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (editandoId == null) "NOVA MESA" else "EDITANDO MESA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = numero,
                            onValueChange = { numero = it },
                            label = { Text("Número") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = lugares,
                            onValueChange = { lugares = it },
                            label = { Text("Lugares") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text("ÁREA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        areasMesa.forEach { opcao ->
                            OpcaoChip(
                                nome = opcao,
                                selecionada = opcao == area,
                                onClick = { area = opcao }
                            )
                        }
                    }

                    erro?.let { mensagem ->
                        Text(text = mensagem, color = Color.Red, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val resultado = onSalvar(editandoId, numero, lugares, area)
                            if (resultado == null) limparFormulario() else erro = resultado
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editandoId == null) "Adicionar mesa" else "Salvar alterações")
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

            if (mesas.isEmpty()) {
                item {
                    Text("Nenhuma mesa cadastrada.", fontSize = 13.sp, color = Color.Gray)
                }
            }

            items(mesas, key = { it.id }) { mesa ->
                MesaItem(
                    mesa = mesa,
                    onEditar = {
                        editandoId = mesa.id
                        numero = mesa.numero.toString()
                        lugares = mesa.lugares.toString()
                        area = mesa.area
                        erro = null
                    },
                    onExcluir = {
                        if (editandoId == mesa.id) limparFormulario()
                        onExcluir(mesa.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun OpcaoChip(nome: String, selecionada: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(
                color = if (selecionada) Terracotta else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .border(1.dp, Terracotta, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(nome, color = if (selecionada) Color.White else Terracotta, fontSize = 11.sp)
    }
}

@Composable
private fun MesaItem(mesa: Mesa, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF7E7DF), RoundedCornerShape(14.dp))
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("MESA", fontSize = 10.sp, color = Terracotta)
            Text(mesa.numero.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Terracotta)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${mesa.lugares} " + if (mesa.lugares == 1) "lugar" else "lugares",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(mesa.area, fontSize = 13.sp, color = Color.Gray)
            Row {
                TextButton(onClick = onEditar) { Text("Editar", color = Terracotta) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = Color.Red) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MesasScreenPreview() {
    RestauranteAppTheme {
        MesasContent(
            mesas = listOf(Mesa(1, 1, 2, "Salão"), Mesa(2, 5, 6, "Varanda")),
            onSalvar = { _, _, _, _ -> null },
            onExcluir = {}
        )
    }
}
