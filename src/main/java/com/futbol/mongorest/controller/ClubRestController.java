package com.futbol.mongorest.controller;

import com.futbol.mongorest.contrato.ClubApiContrato;
import com.futbol.mongorest.dto.ClubRequestDTO;
import com.futbol.mongorest.model.Club;
import com.futbol.mongorest.service.ClubMongoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubes")
@CrossOrigin(origins = "*")
public class ClubRestController implements ClubApiContrato {

    private final ClubMongoService clubService;

    public ClubRestController(ClubMongoService clubService) {
        this.clubService = clubService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Club>> listarTodos() {
        return ResponseEntity.ok(clubService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return clubService.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Recurso no encontrado");
                    error.put("mensaje", "No se encontró ningún club registrado con el ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @PostMapping
    public ResponseEntity<Club> crear(@Valid @RequestBody ClubRequestDTO dto) {
        Club creado = clubService.crearDesdeDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @Valid @RequestBody ClubRequestDTO dto) {
        return clubService.actualizarDesdeDto(id, dto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Conflicto o no encontrado");
                    error.put("mensaje", "No es posible actualizar. No existe el club con ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (clubService.eliminar(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Club con ID " + id + " eliminado con éxito de MongoDB.");
            return ResponseEntity.ok(response);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", "No se encontró el club con ID: " + id + " para eliminar.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
