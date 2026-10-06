package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Competicion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompeticionMongoRepository extends MongoRepository<Competicion, String> {
    List<Competicion> findByOrderByFechaInicioAsc();
}
