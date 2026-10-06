package com.futbol.mongorest.controller;

import com.futbol.mongorest.contrato.EntrenadorApiContrato;
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
@RequestMapping({"/api/entrenadores", "/api/clubes/entrenadores"})
@CrossOrigin(origins = "*")
public class EntrenadorRestController implements EntrenadorApiContrato {

    private final EntrenadorMongoService entrenadorService;

    public EntrenadorRestController(EntrenadorMongoService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Entrenador>> listar() {
        return ResponseEntity.ok(entrenadorService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return entrenadorService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "No existe entrenador con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @PostMapping
    public ResponseEntity<Entrenador> crear(@Valid @RequestBody Entrenador entrenador) {
        Entrenador nuevo = entrenadorService.guardar(entrenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Entrenador entrenador) {
        return entrenadorService.actualizar(id, entrenador)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "No se encontró el entrenador con ID: " + id + " para actualizar.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (entrenadorService.eliminar(id)) {
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Entrenador eliminado con éxito.");
            return ResponseEntity.ok(res);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", "No se encontró el entrenador con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
