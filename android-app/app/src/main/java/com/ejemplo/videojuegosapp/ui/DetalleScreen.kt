package com.ejemplo.videojuegosapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.videojuegosapp.data.Videojuego

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    id: Long,
    viewModel: VideojuegoViewModel,
    onVolver: () -> Unit,
    onEditar: (Videojuego) -> Unit
) {
    // LaunchedEffect(id): se ejecuta al entrar en la pantalla (y si cambia el id).
    // Aquí se hace el GET /api/v1/videojuegos/{id}.
    LaunchedEffect(id) {
        viewModel.cargarPorId(id)
    }

    var confirmarBorrado by remember { mutableStateOf(false) }
    val videojuego = viewModel.seleccionado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewModel.cargando && videojuego == null -> CircularProgressIndicator()

                viewModel.error != null -> MensajeError(
                    mensaje = viewModel.error!!,
                    onReintentar = { viewModel.cargarPorId(id) }
                )

                videojuego != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(videojuego.titulo, style = MaterialTheme.typography.headlineMedium)
                    Text("Id: ${videojuego.id}")
                    Text("Género: ${videojuego.genero}")
                    Text("Año de lanzamiento: ${videojuego.anioLanzamiento}")
                    Text("Precio: ${formatearPrecio(videojuego.precio)}")

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { onEditar(videojuego) }) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Text("  Editar")
                        }
                        OutlinedButton(
                            onClick = { confirmarBorrado = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Text("  Borrar")
                        }
                    }
                }
            }
        }
    }

    if (confirmarBorrado && videojuego != null) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("Borrar videojuego") },
            text = { Text("¿Seguro que quieres borrar \"${videojuego.titulo}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrado = false
                    // DELETE /api/v1/videojuegos/{id}; al terminar, vuelve a la lista
                    viewModel.borrar(videojuego.id, alTerminar = onVolver)
                }) { Text("Borrar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") }
            }
        )
    }
}
