package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubMongoRepository extends MongoRepository<Club, String> {
    Optional<Club> findByNombreIgnoreCase(String nombre);
}
