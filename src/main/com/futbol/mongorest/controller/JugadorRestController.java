package com.futbol.mongorest.controller;

import com.futbol.mongorest.model.Jugador;
import com.futbol.mongorest.service.JugadorMongoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin(origins = "*")
public class JugadorRestController {

    private final JugadorMongoService jugadorService;

    public JugadorRestController(JugadorMongoService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @GetMapping
    public ResponseEntity<List<Jugador>> listar() {
        return ResponseEntity.ok(jugadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return jugadorService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Jugador no encontrado con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@Valid @RequestBody Jugador jugador) {
        Jugador nuevo = jugadorService.guardar(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Jugador jugador) {
        return jugadorService.actualizar(id, jugador)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "No se encontró el jugador con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (jugadorService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Jugador no encontrado con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
