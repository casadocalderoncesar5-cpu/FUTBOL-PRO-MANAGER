package com.futbol.mongorest.controller;

import com.futbol.mongorest.contrato.CompeticionApiContrato;
import com.futbol.mongorest.model.Competicion;
import com.futbol.mongorest.service.CompeticionMongoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/competiciones", "/api/clubes/competiciones"})
@CrossOrigin(origins = "*")
public class CompeticionRestController implements CompeticionApiContrato {

    private final CompeticionMongoService competicionService;

    public CompeticionRestController(CompeticionMongoService competicionService) {
        this.competicionService = competicionService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Competicion>> listar() {
        return ResponseEntity.ok(competicionService.listarTodas());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return competicionService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "Competición no encontrada con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @PostMapping
    public ResponseEntity<Competicion> crear(@Valid @RequestBody Competicion competicion) {
        Competicion nueva = competicionService.guardar(competicion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody Competicion competicion) {
        return competicionService.actualizar(id, competicion)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No encontrado");
                    error.put("mensaje", "No se encontró la competición con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (competicionService.eliminar(id)) {
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Competición eliminada con éxito.");
            return ResponseEntity.ok(res);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", "Competición no encontrada con ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
