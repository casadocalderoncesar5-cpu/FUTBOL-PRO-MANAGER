package com.futbol.mongorest.controller;

import com.futbol.mongorest.model.Arbitro;
import com.futbol.mongorest.repository.ArbitroMongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/arbitros", "/api/clubes/arbitros"})
@CrossOrigin(origins = "*")
public class ArbitroRestController {

    private final ArbitroMongoRepository arbitroRepo;

    public ArbitroRestController(ArbitroMongoRepository arbitroRepo) {
        this.arbitroRepo = arbitroRepo;
    }

    @GetMapping
    public ResponseEntity<List<Arbitro>> listarTodos() {
        return ResponseEntity.ok(arbitroRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<Arbitro> crear(@RequestBody Arbitro arbitro) {
        Arbitro guardado = arbitroRepo.save(arbitro);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
