package com.ejemplo.videojuegosapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.videojuegosapp.data.RetrofitClient
import com.ejemplo.videojuegosapp.data.Videojuego
import com.ejemplo.videojuegosapp.data.VideojuegoRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// El ViewModel es quien habla con el backend. Las pantallas solo leen su estado y llaman a sus funciones.
class VideojuegoViewModel : ViewModel() {

    private val api = RetrofitClient.api

    // Estado que observan las pantallas: cuando cambia, Compose vuelve a dibujar.
    var videojuegos by mutableStateOf<List<Videojuego>>(emptyList())
        private set
    var seleccionado by mutableStateOf<Videojuego?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        cargarTodos()
    }

    // GET all
    fun cargarTodos() = llamarAlServidor {
        videojuegos = api.getAll()
    }

    // GET por id
    fun cargarPorId(id: Long) = llamarAlServidor {
        seleccionado = null
        seleccionado = api.getById(id)
    }

    // POST
    fun crear(datos: VideojuegoRequest, alTerminar: () -> Unit) = llamarAlServidor {
        api.create(datos)
        videojuegos = api.getAll() // se recarga la lista para que aparezca el nuevo
        alTerminar()
    }

    // PUT
    fun actualizar(id: Long, datos: VideojuegoRequest, alTerminar: () -> Unit) = llamarAlServidor {
        seleccionado = api.update(id, datos)
        videojuegos = api.getAll()
        alTerminar()
    }

    // DELETE
    fun borrar(id: Long, alTerminar: () -> Unit) = llamarAlServidor {
        api.delete(id)
        videojuegos = api.getAll()
        alTerminar()
    }

    // Todas las llamadas siguen el mismo patrón, así que se agrupa aquí:
    //  1. viewModelScope.launch: lanza una corrutina. La petición de red NO puede ir en el hilo
    //     principal (Android lanzaría NetworkOnMainThreadException y la interfaz se congelaría).
    //  2. Marca "cargando" para que la pantalla muestre el indicador de progreso.
    //  3. Captura los dos fallos típicos y los convierte en un mensaje para el usuario.
    private fun llamarAlServidor(peticion: suspend () -> Unit) {
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                peticion()
            } catch (e: HttpException) {
                // El servidor SÍ ha respondido, pero con un código de error
                error = when (e.code()) {
                    400 -> "Datos no válidos (400). Revisa el formulario."
                    404 -> "Ese videojuego ya no existe (404)."
                    else -> "El servidor ha respondido con error ${e.code()}."
                }
            } catch (e: IOException) {
                // No se ha podido ni conectar: backend apagado, URL mal puesta, sin red...
                error = "No se puede conectar con el servidor. ¿Está arrancado el backend?"
            } finally {
                cargando = false
            }
        }
    }
}
