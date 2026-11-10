package com.ejemplo.videojuegosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.videojuegosapp.data.Videojuego
import com.ejemplo.videojuegosapp.ui.DetalleScreen
import com.ejemplo.videojuegosapp.ui.FormularioScreen
import com.ejemplo.videojuegosapp.ui.ListaScreen
import com.ejemplo.videojuegosapp.ui.VideojuegoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                VideojuegosApp()
            }
        }
    }
}

// Las tres pantallas de la app. Navegación mínima con un estado,
// para que el ejemplo se centre en la comunicación con el backend.
sealed interface Pantalla {
    data object Lista : Pantalla
    data class Detalle(val id: Long) : Pantalla
    data class Formulario(val videojuego: Videojuego?) : Pantalla // null = crear, con valor = editar
}

@Composable
fun VideojuegosApp() {
    // Un único ViewModel compartido por las tres pantallas
    val viewModel: VideojuegoViewModel = viewModel()
    var pantalla by remember { mutableStateOf<Pantalla>(Pantalla.Lista) }

    // Botón "atrás" del sistema: vuelve a la lista en lugar de cerrar la app
    BackHandler(enabled = pantalla != Pantalla.Lista) {
        pantalla = Pantalla.Lista
    }

    when (val actual = pantalla) {
        Pantalla.Lista -> ListaScreen(
            viewModel = viewModel,
            onVideojuegoClick = { id -> pantalla = Pantalla.Detalle(id) },
            onNuevoClick = { pantalla = Pantalla.Formulario(null) }
        )

        is Pantalla.Detalle -> DetalleScreen(
            id = actual.id,
            viewModel = viewModel,
            onVolver = { pantalla = Pantalla.Lista },
            onEditar = { videojuego -> pantalla = Pantalla.Formulario(videojuego) }
        )

        is Pantalla.Formulario -> FormularioScreen(
            videojuego = actual.videojuego,
            viewModel = viewModel,
            onVolver = { pantalla = Pantalla.Lista },
            onGuardado = { id -> pantalla = if (id == null) Pantalla.Lista else Pantalla.Detalle(id) }
        )
    }
}
