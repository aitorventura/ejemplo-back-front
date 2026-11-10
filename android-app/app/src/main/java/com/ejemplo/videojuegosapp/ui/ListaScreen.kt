package com.ejemplo.videojuegosapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.videojuegosapp.data.Videojuego

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: VideojuegoViewModel,
    onVideojuegoClick: (Long) -> Unit,
    onNuevoClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Videojuegos") },
                actions = {
                    IconButton(onClick = { viewModel.cargarTodos() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevoClick) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo videojuego")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewModel.cargando && viewModel.videojuegos.isEmpty() -> CircularProgressIndicator()

                viewModel.error != null && viewModel.videojuegos.isEmpty() -> MensajeError(
                    mensaje = viewModel.error!!,
                    onReintentar = { viewModel.cargarTodos() }
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.videojuegos, key = { it.id }) { videojuego ->
                        FilaVideojuego(videojuego, onClick = { onVideojuegoClick(videojuego.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaVideojuego(videojuego: Videojuego, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(videojuego.titulo, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${videojuego.genero} · ${videojuego.anioLanzamiento}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(formatearPrecio(videojuego.precio), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun MensajeError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(mensaje, color = MaterialTheme.colorScheme.error)
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}

fun formatearPrecio(precio: Double): String =
    if (precio == 0.0) "Gratis" else String.format("%.2f €", precio)
