package com.futbol.mongorest.controller;

import com.futbol.mongorest.model.Entrenador;
import com.futbol.mongorest.service.EntrenadorMongoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entrenadores")
@CrossOrigin(origins = "*")
public class EntrenadorRestController {

    private final EntrenadorMongoService entrenadorService;

    public EntrenadorRestController(EntrenadorMongoService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public ResponseEntity<List<Entrenador>> listar() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return entrenadorService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Entrenador no encontrado con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping
    public ResponseEntity<Entrenador> crear(@Valid @RequestBody Entrenador entrenador) {
        Entrenador nuevo = entrenadorService.guardar(entrenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Entrenador entrenador) {
        return entrenadorService.actualizar(id, entrenador)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "No se encontró el entrenador con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (entrenadorService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Entrenador no encontrado con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
