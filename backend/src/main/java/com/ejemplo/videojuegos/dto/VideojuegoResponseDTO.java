package com.ejemplo.videojuegos.dto;

// Lo que el servidor DEVUELVE. Este JSON es el que la app Android convierte en un objeto Kotlin.
public record VideojuegoResponseDTO(
        Long id,
        String titulo,
        String genero,
        Double precio,
        Integer anioLanzamiento
) {}
