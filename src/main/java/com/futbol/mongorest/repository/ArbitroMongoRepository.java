package com.futbol.mongorest.repository;

import com.futbol.mongorest.model.Arbitro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbitroMongoRepository extends MongoRepository<Arbitro, String> {
}
