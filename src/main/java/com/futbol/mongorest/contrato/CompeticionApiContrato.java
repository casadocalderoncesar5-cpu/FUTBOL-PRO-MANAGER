package com.futbol.mongorest.contrato;

import com.futbol.mongorest.model.Competicion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompeticionApiContrato {
    ResponseEntity<List<Competicion>> listar();
    ResponseEntity<?> buscarPorId(String id);
    ResponseEntity<Competicion> crear(Competicion competicion);
    ResponseEntity<?> actualizar(String id, Competicion competicion);
    ResponseEntity<?> eliminar(String id);
}
