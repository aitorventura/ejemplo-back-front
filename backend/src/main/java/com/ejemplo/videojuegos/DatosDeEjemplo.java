package com.ejemplo.videojuegos;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Spring ejecuta run() una vez, justo al terminar de arrancar el servidor.
// Solo inserta datos si la tabla está vacía, así no se duplican en cada arranque.
@Component
@RequiredArgsConstructor
public class DatosDeEjemplo implements CommandLineRunner {

    private final VideojuegoRepository videojuegoRepository;

    @Override
    public void run(String... args) {
        if (videojuegoRepository.count() > 0) {
            return;
        }
        videojuegoRepository.saveAll(List.of(
                new Videojuego("Hollow Knight", "Metroidvania", 14.99, 2017),
                new Videojuego("Celeste", "Plataformas", 19.99, 2018),
                new Videojuego("Stardew Valley", "Simulación", 13.99, 2016),
                new Videojuego("Hades", "Roguelike", 24.99, 2020),
                new Videojuego("The Witcher 3", "RPG", 29.99, 2015),
                new Videojuego("Portal 2", "Puzles", 9.99, 2011),
                new Videojuego("Minecraft", "Sandbox", 26.95, 2011),
                new Videojuego("Rocket League", "Deportes", 0.0, 2015)
        ));
        System.out.println(">>> Datos de ejemplo insertados: " + videojuegoRepository.count() + " videojuegos");
    }
}
