package com.ejemplo.videojuegosapp.data

// Espejo de VideojuegoResponseDTO del backend: lo que llega en el JSON de un GET.
// Los nombres de las propiedades TIENEN que coincidir con las claves del JSON,
// porque Gson los empareja por nombre ("anioLanzamiento" <-> "anioLanzamiento").
data class Videojuego(
    val id: Long,
    val titulo: String,
    val genero: String,
    val precio: Double,
    val anioLanzamiento: Int
)

// Espejo de VideojuegoRequestDTO: lo que la app envía en un POST o un PUT (sin id).
data class VideojuegoRequest(
    val titulo: String,
    val genero: String,
    val precio: Double,
    val anioLanzamiento: Int
)
