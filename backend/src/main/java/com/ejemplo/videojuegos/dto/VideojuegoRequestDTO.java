package com.ejemplo.videojuegos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// Lo que el cliente ENVÍA en un POST o un PUT (sin id: el id lo pone la base de datos)
public record VideojuegoRequestDTO(
        @NotBlank(message = "El título no puede estar vacío")
        String titulo,

        @NotBlank(message = "El género no puede estar vacío")
        String genero,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        Double precio,

        @NotNull(message = "El año de lanzamiento es obligatorio")
        Integer anioLanzamiento
) {}
