package com.ejemplo.videojuegosapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ejemplo.videojuegosapp.data.Videojuego
import com.ejemplo.videojuegosapp.data.VideojuegoRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    videojuego: Videojuego?,           // null = crear (POST), con valor = editar (PUT)
    viewModel: VideojuegoViewModel,
    onVolver: () -> Unit,
    onGuardado: (Long?) -> Unit        // recibe el id editado, o null si se ha creado uno nuevo
) {
    val esEdicion = videojuego != null

    // Los campos de texto siempre trabajan con String; se convierten a número al guardar
    var titulo by rememberSaveable { mutableStateOf(videojuego?.titulo ?: "") }
    var genero by rememberSaveable { mutableStateOf(videojuego?.genero ?: "") }
    var precio by rememberSaveable { mutableStateOf(videojuego?.precio?.toString() ?: "") }
    var anio by rememberSaveable { mutableStateOf(videojuego?.anioLanzamiento?.toString() ?: "") }
    var errorLocal by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar videojuego" else "Nuevo videojuego") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = titulo, onValueChange = { titulo = it },
                label = { Text("Título") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = genero, onValueChange = { genero = it },
                label = { Text("Género") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = precio, onValueChange = { precio = it },
                label = { Text("Precio (usa punto: 19.99)") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = anio, onValueChange = { anio = it },
                label = { Text("Año de lanzamiento") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Error de la propia app (campo mal escrito) o error que devuelve el servidor
            (errorLocal ?: viewModel.error)?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                enabled = !viewModel.cargando,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val precioNumero = precio.replace(',', '.').toDoubleOrNull()
                    val anioNumero = anio.toIntOrNull()
                    if (precioNumero == null || anioNumero == null) {
                        errorLocal = "El precio y el año tienen que ser números."
                        return@Button
                    }
                    errorLocal = null

                    // Los campos vacíos se envían tal cual: es el backend (@NotBlank) quien los
                    // rechaza con un 400, y el ViewModel lo convierte en mensaje de error.
                    val datos = VideojuegoRequest(titulo, genero, precioNumero, anioNumero)

                    if (videojuego == null) {
                        viewModel.crear(datos, alTerminar = { onGuardado(null) })
                    } else {
                        viewModel.actualizar(videojuego.id, datos, alTerminar = { onGuardado(videojuego.id) })
                    }
                }
            ) {
                Text(if (esEdicion) "Guardar cambios (PUT)" else "Crear (POST)")
            }
        }
    }
}
