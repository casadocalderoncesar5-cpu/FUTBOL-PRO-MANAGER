package com.futbol.mongorest.contrato;

import com.futbol.mongorest.dto.ClubRequestDTO;
import com.futbol.mongorest.model.Club;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClubApiContrato {

    ResponseEntity<List<Club>> listarTodos();

    ResponseEntity<?> buscarPorId(String id);

    ResponseEntity<Club> crear(ClubRequestDTO dto);

    ResponseEntity<?> actualizar(String id, ClubRequestDTO dto);

    ResponseEntity<?> eliminar(String id);
}
