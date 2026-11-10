package com.ejemplo.videojuegos;

import com.ejemplo.videojuegos.dto.VideojuegoRequestDTO;
import com.ejemplo.videojuegos.dto.VideojuegoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideojuegoService {

    private final VideojuegoRepository videojuegoRepository;

    @Transactional(readOnly = true)
    public List<VideojuegoResponseDTO> findAll() {
        return videojuegoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VideojuegoResponseDTO findById(Long id) {
        return videojuegoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado"));
    }

    @Transactional
    public VideojuegoResponseDTO create(VideojuegoRequestDTO dto) {
        Videojuego videojuego = new Videojuego();
        copiarDatos(dto, videojuego);
        return mapToDTO(videojuegoRepository.save(videojuego));
    }

    @Transactional
    public VideojuegoResponseDTO update(Long id, VideojuegoRequestDTO dto) {
        Videojuego videojuego = videojuegoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado"));
        copiarDatos(dto, videojuego);
        return mapToDTO(videojuegoRepository.save(videojuego));
    }

    @Transactional
    public void delete(Long id) {
        if (!videojuegoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Videojuego no encontrado");
        }
        videojuegoRepository.deleteById(id);
    }

    private void copiarDatos(VideojuegoRequestDTO dto, Videojuego videojuego) {
        videojuego.setTitulo(dto.titulo());
        videojuego.setGenero(dto.genero());
        videojuego.setPrecio(dto.precio());
        videojuego.setAnioLanzamiento(dto.anioLanzamiento());
    }

    private VideojuegoResponseDTO mapToDTO(Videojuego v) {
        return new VideojuegoResponseDTO(v.getId(), v.getTitulo(), v.getGenero(), v.getPrecio(), v.getAnioLanzamiento());
    }
}
