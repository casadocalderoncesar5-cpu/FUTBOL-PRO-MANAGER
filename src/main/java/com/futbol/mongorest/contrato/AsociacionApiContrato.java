package com.futbol.mongorest.contrato;

import com.futbol.mongorest.model.Asociacion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AsociacionApiContrato {
    ResponseEntity<List<Asociacion>> listar();
    ResponseEntity<?> buscarPorId(String id);
    ResponseEntity<Asociacion> crear(Asociacion asociacion);
    ResponseEntity<?> actualizar(String id, Asociacion asociacion);
    ResponseEntity<?> eliminar(String id);
}
