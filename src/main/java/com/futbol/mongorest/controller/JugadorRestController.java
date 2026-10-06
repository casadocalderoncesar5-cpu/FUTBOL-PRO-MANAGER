package com.futbol.mongorest.controller;

import com.futbol.mongorest.contrato.JugadorApiContrato;
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
@RequestMapping({"/api/jugadores", "/api/clubes/jugadores"})
@CrossOrigin(origins = "*")
public class JugadorRestController implements JugadorApiContrato {

    private final JugadorMongoService jugadorService;

    public JugadorRestController(JugadorMongoService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Jugador>> listar() {
        return ResponseEntity.ok(jugadorService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return jugadorService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "No existe jugador con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @PostMapping
    public ResponseEntity<Jugador> crear(@Valid @RequestBody Jugador jugador) {
        Jugador nuevo = jugadorService.guardar(jugador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Jugador jugador) {
        return jugadorService.actualizar(id, jugador)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "No se encontró el jugador con ID: " + id + " para modificar.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (jugadorService.eliminar(id)) {
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Jugador con ID " + id + " eliminado con éxito de MongoDB.");
            return ResponseEntity.ok(res);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", "Jugador no encontrado con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
