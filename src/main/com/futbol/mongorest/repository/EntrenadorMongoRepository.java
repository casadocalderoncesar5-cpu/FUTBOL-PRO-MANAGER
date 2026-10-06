package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Entrenador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrenadorMongoRepository extends MongoRepository<Entrenador, String> {
    List<Entrenador> findByNacionalidadIgnoreCase(String nacionalidad);
}
