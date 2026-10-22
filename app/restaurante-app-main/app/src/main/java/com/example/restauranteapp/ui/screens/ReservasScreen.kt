package com.example.restauranteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restauranteapp.data.Reserva
import com.example.restauranteapp.ui.theme.Terracotta
import com.example.restauranteapp.viewmodel.ReservaViewModel

@Composable
fun TelaReservas(viewModel: ReservaViewModel) {
    var editandoId by remember { mutableStateOf<Int?>(null) }
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var pessoas by remember { mutableStateOf("2") }
    var erro by remember { mutableStateOf<String?>(null) }

    fun limparFormulario() {
        editandoId = null
        nome = ""
        telefone = ""
        data = ""
        horario = ""
        pessoas = "2"
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (editandoId == null) "Nova reserva" else "Editando reserva",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = { Text("Telefone") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = data,
                            onValueChange = { data = it },
                            label = { Text("Data") },
                            placeholder = { Text("dd/mm/aaaa") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = horario,
                            onValueChange = { horario = it },
                            label = { Text("Horário") },
                            placeholder = { Text("--:--") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = pessoas,
                        onValueChange = { pessoas = it },
                        label = { Text("Número de pessoas") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (erro != null) {
                        Text(text = erro!!, color = Color.Red, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            erro = viewModel.salvar(editandoId, nome, telefone, data, horario, pessoas)
                            if (erro == null) limparFormulario()
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
                        OutlinedButton(
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

                    if (viewModel.reservas.isEmpty()) {
                        Text("Nenhuma reserva cadastrada.", fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }

            items(viewModel.reservas, key = { it.id }) { reserva ->
                ReservaItem(
                    reserva = reserva,
                    onEditar = {
                        editandoId = reserva.id
                        nome = reserva.nome
                        telefone = reserva.telefone
                        data = reserva.data
                        horario = reserva.hora
                        pessoas = reserva.pessoas.toString()
                        erro = null
                    },
                    onExcluir = {
                        viewModel.excluir(reserva.id)
                        if (editandoId == reserva.id) limparFormulario()
                    }
                )
            }
        }
    }
}

@Composable
private fun ReservaItem(reserva: Reserva, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(reserva.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(3.dp))
        Text("${reserva.data} às ${reserva.hora}", fontSize = 13.sp, color = Terracotta)
        Text("${reserva.pessoas} pessoa(s) • ${reserva.telefone}", fontSize = 12.sp, color = Color.Gray)

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onEditar) {
                Text("Editar", color = Terracotta)
            }
            Button(
                onClick = onExcluir,
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
            ) {
                Text("Excluir")
            }
        }
    }
}
