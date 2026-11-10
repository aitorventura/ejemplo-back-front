package com.ejemplo.videojuegosapp.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Cada función es UN endpoint del VideojuegoController del backend.
// Retrofit genera el código que hace la petición HTTP: tú solo describes la ruta y los datos.
//  - suspend: la llamada tarda (va por la red), así que se ejecuta dentro de una corrutina.
//  - Si el servidor responde con un código de error (404, 400, 500...), la función lanza HttpException.
//  - Si no hay conexión con el servidor, lanza IOException.
interface VideojuegoApi {

    // GET /api/v1/videojuegos -> el JSON [ {...}, {...} ] se convierte en List<Videojuego>
    @GET("api/v1/videojuegos")
    suspend fun getAll(): List<Videojuego>

    // GET /api/v1/videojuegos/3 -> {id} se sustituye por el valor del parámetro id
    @GET("api/v1/videojuegos/{id}")
    suspend fun getById(@Path("id") id: Long): Videojuego

    // POST /api/v1/videojuegos -> @Body convierte el objeto Kotlin en el JSON del cuerpo
    @POST("api/v1/videojuegos")
    suspend fun create(@Body videojuego: VideojuegoRequest): Videojuego

    // PUT /api/v1/videojuegos/3
    @PUT("api/v1/videojuegos/{id}")
    suspend fun update(@Path("id") id: Long, @Body videojuego: VideojuegoRequest): Videojuego

    // DELETE /api/v1/videojuegos/3 -> el backend responde 204 sin cuerpo, por eso devuelve Unit
    @DELETE("api/v1/videojuegos/{id}")
    suspend fun delete(@Path("id") id: Long)
}
