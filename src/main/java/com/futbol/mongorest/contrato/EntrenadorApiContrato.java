package com.futbol.mongorest.contrato;

import com.futbol.mongorest.model.Entrenador;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EntrenadorApiContrato {
    ResponseEntity<List<Entrenador>> listar();
    ResponseEntity<?> buscarPorId(String id);
    ResponseEntity<Entrenador> crear(Entrenador entrenador);
    ResponseEntity<?> actualizar(String id, Entrenador entrenador);
    ResponseEntity<?> eliminar(String id);
}
