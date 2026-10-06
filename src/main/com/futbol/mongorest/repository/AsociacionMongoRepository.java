package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Asociacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsociacionMongoRepository extends MongoRepository<Asociacion, String> {
    Optional<Asociacion> findBySiglasIgnoreCase(String siglas);
}
