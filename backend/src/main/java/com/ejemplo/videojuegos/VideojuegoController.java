package com.ejemplo.videojuegos;

import com.ejemplo.videojuegos.dto.VideojuegoRequestDTO;
import com.ejemplo.videojuegos.dto.VideojuegoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videojuegos")
@RequiredArgsConstructor
public class VideojuegoController {

    private final VideojuegoService videojuegoService;

    // GET /api/v1/videojuegos -> 200 + lista
    @GetMapping
    public ResponseEntity<List<VideojuegoResponseDTO>> getAll() {
        return ResponseEntity.ok(videojuegoService.findAll());
    }

    // GET /api/v1/videojuegos/3 -> 200 + videojuego, o 404
    @GetMapping("/{id}")
    public ResponseEntity<VideojuegoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(videojuegoService.findById(id));
    }

    // POST /api/v1/videojuegos -> 201 + videojuego creado (ya con id)
    @PostMapping
    public ResponseEntity<VideojuegoResponseDTO> create(@RequestBody @Valid VideojuegoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videojuegoService.create(dto));
    }

    // PUT /api/v1/videojuegos/3 -> 200 + videojuego actualizado, o 404
    @PutMapping("/{id}")
    public ResponseEntity<VideojuegoResponseDTO> update(@PathVariable Long id, @RequestBody @Valid VideojuegoRequestDTO dto) {
        return ResponseEntity.ok(videojuegoService.update(id, dto));
    }

    // DELETE /api/v1/videojuegos/3 -> 204 sin cuerpo, o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        videojuegoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
