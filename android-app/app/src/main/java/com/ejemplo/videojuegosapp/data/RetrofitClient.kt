package com.ejemplo.videojuegosapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ¿Por qué 10.0.2.2 y no localhost?
    // Dentro del emulador, "localhost" es el PROPIO emulador, no tu ordenador.
    // 10.0.2.2 es una dirección especial del emulador que apunta al localhost de tu PC,
    // que es donde está escuchando Spring Boot en el puerto 8080.
    // Con un móvil físico: pon la IP de tu PC en la wifi (ipconfig), p. ej. "http://192.168.1.40:8080/".
    // La URL tiene que terminar en "/".
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Escribe en Logcat cada petición y cada respuesta (método, URL, JSON...).
    // Filtra Logcat por "okhttp" para ver exactamente qué viaja por la red.
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: VideojuegoApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
        .build()
        .create(VideojuegoApi::class.java)
}
