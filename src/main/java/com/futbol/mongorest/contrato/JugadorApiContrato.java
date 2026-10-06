package com.futbol.mongorest.contrato;

import com.futbol.mongorest.model.Jugador;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JugadorApiContrato {
    ResponseEntity<List<Jugador>> listar();
    ResponseEntity<?> buscarPorId(String id);
    ResponseEntity<Jugador> crear(Jugador jugador);
    ResponseEntity<?> actualizar(String id, Jugador jugador);
    ResponseEntity<?> eliminar(String id);
}
