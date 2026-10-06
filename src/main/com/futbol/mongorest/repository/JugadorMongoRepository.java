package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Jugador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JugadorMongoRepository extends MongoRepository<Jugador, String> {
    List<Jugador> findByPosicionIgnoreCase(String posicion);
}
