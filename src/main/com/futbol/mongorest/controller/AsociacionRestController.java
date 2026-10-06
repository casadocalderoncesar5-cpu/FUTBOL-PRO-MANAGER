package com.futbol.mongorest.controller;

import com.futbol.mongorest.model.Asociacion;
import com.futbol.mongorest.service.AsociacionMongoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asociaciones")
@CrossOrigin(origins = "*")
public class AsociacionRestController {

    private final AsociacionMongoService asociacionService;

    public AsociacionRestController(AsociacionMongoService asociacionService) {
        this.asociacionService = asociacionService;
    }

    @GetMapping
    public ResponseEntity<List<Asociacion>> listar() {
        return ResponseEntity.ok(asociacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return asociacionService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Asociación no encontrada con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping
    public ResponseEntity<Asociacion> crear(@Valid @RequestBody Asociacion asociacion) {
        Asociacion nueva = asociacionService.guardar(asociacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Asociacion asociacion) {
        return asociacionService.actualizar(id, asociacion)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "No se encontró la asociación con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (asociacionService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Asociación no encontrada con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
