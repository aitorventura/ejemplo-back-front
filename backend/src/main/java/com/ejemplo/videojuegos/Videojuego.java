package com.ejemplo.videojuegos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videojuego")
@Getter
@Setter
@NoArgsConstructor
public class Videojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String genero;
    private Double precio;
    private Integer anioLanzamiento;

    public Videojuego(String titulo, String genero, Double precio, Integer anioLanzamiento) {
        this.titulo = titulo;
        this.genero = genero;
        this.precio = precio;
        this.anioLanzamiento = anioLanzamiento;
    }
}
