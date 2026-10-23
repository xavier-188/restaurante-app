package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.restauranteapp.data.model.Reserva
import com.example.restauranteapp.ui.theme.RestauranteAppTheme
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.ui.viewmodel.ReservaViewModel

// Parte "conectada": fala com o ViewModel
@Composable
fun TelaReservas(
    reservaViewModel: ReservaViewModel,
    usuarioId: Int
) {
    // Carrega as reservas do usuário quando a tela abre (e de novo se o usuário mudar)
    LaunchedEffect(usuarioId) {
        reservaViewModel.carregar(usuarioId)
    }

    ReservasContent(
        reservas = reservaViewModel.reservas,
        onSalvar = { id, nome, telefone, data, hora, pessoas, observacoes ->
            reservaViewModel.salvar(id, nome, telefone, data, hora, pessoas, observacoes)
        },
        onExcluir = { id -> reservaViewModel.excluir(id) }
    )
}

// Parte "visual": só recebe dados e callbacks (por isso funciona no Preview)
@Composable
fun ReservasContent(
    reservas: List<Reserva>,
    onSalvar: (Int?, String, String, String, String, String, String) -> String?,
    onExcluir: (Int) -> Unit
) {
    // Variáveis de estado do formulário
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var pessoas by remember { mutableStateOf("2") }
    var observacoes by remember { mutableStateOf("") }

    // null = criando uma reserva nova; um número = editando a reserva com esse id
    var editandoId by remember { mutableStateOf<Int?>(null) }
    var erro by remember { mutableStateOf<String?>(null) }

    fun limparFormulario() {
        nome = ""
        telefone = ""
        data = ""
        horario = ""
        pessoas = "2"
        observacoes = ""
        editandoId = null
        erro = null
    }

    Scaffold(
        topBar = {
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
        }
    ) { innerPadding ->
        // LazyColumn: o formulário é o primeiro item e as reservas vêm logo abaixo
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (editandoId == null) "NOVA RESERVA" else "EDITANDO RESERVA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta
                    )

                    CampoReserva("NOME COMPLETO") {
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = { Text("Ex: João Silva") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    CampoReserva("TELEFONE") {
                        OutlinedTextField(
                            value = telefone,
                            onValueChange = { telefone = it },
                            placeholder = { Text("(00) 00000-0000") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Data e Horário lado a lado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            CampoReserva("DATA") {
                                OutlinedTextField(
                                    value = data,
                                    onValueChange = { data = it },
                                    placeholder = { Text("dd/mm/aaaa") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            CampoReserva("HORÁRIO") {
                                OutlinedTextField(
                                    value = horario,
                                    onValueChange = { horario = it },
                                    placeholder = { Text("--:--") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    CampoReserva("NÚMERO DE PESSOAS") {
                        OutlinedTextField(
                            value = pessoas,
                            onValueChange = { pessoas = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    CampoReserva("OBSERVAÇÕES") {
                        OutlinedTextField(
                            value = observacoes,
                            onValueChange = { observacoes = it },
                            placeholder = { Text("Alergias, ocasião especial, preferências...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )
                    }

                    erro?.let { mensagem ->
                        Text(text = mensagem, color = Color.Red, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val resultado = onSalvar(
                                editandoId, nome, telefone, data, horario, pessoas, observacoes
                            )
                            if (resultado == null) {
                                limparFormulario()
                            } else {
                                erro = resultado
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (editandoId == null) "CONFIRMAR RESERVA" else "SALVAR ALTERAÇÕES",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (editandoId != null) {
                        TextButton(
                            onClick = { limparFormulario() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelar edição", color = Terracotta)
                        }
                    }

                    Text(
                        text = "Minhas reservas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    if (reservas.isEmpty()) {
                        Text(
                            text = "Você ainda não tem reservas.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            items(reservas, key = { it.id }) { reserva ->
                ReservaItem(
                    reserva = reserva,
                    onEditar = {
                        nome = reserva.nome
                        telefone = reserva.telefone
                        data = reserva.data
                        horario = reserva.hora
                        pessoas = reserva.pessoas.toString()
                        observacoes = reserva.observacoes
                        editandoId = reserva.id
                        erro = null
                    },
                    onExcluir = {
                        // Se estava editando justamente essa reserva, limpa o formulário
                        if (editandoId == reserva.id) limparFormulario()
                        onExcluir(reserva.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun CampoReserva(rotulo: String, campo: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = rotulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        campo()
    }
}

@Composable
private fun ReservaItem(
    reserva: Reserva,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(reserva.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            "${reserva.data} às ${reserva.hora} • ${reserva.pessoas} " +
                if (reserva.pessoas == 1) "pessoa" else "pessoas",
            color = Terracotta,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text("Tel: ${reserva.telefone}", fontSize = 12.sp, color = Color.Gray)
        if (reserva.observacoes.isNotBlank()) {
            Text(reserva.observacoes, fontSize = 12.sp, color = Color.Gray)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onEditar) { Text("Editar", color = Terracotta) }
            TextButton(onClick = onExcluir) { Text("Excluir", color = Color.Red) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservasScreenPreview() {
    RestauranteAppTheme {
        ReservasContent(
            reservas = listOf(
                Reserva(1, "João Silva", "(11) 99999-0000", "25/10/2026", "20:00", 4, "Aniversário", 1)
            ),
            onSalvar = { _, _, _, _, _, _, _ -> null },
            onExcluir = {}
        )
    }
}
