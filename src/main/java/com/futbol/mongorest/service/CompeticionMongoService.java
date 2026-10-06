package com.futbol.mongorest.service;

import com.futbol.mongorest.model.Competicion;
import com.futbol.mongorest.repository.CompeticionMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompeticionMongoService {

    private final CompeticionMongoRepository competicionRepository;

    public CompeticionMongoService(CompeticionMongoRepository competicionRepository) {
        this.competicionRepository = competicionRepository;
    }

    public List<Competicion> listarTodas() {
        return competicionRepository.findByOrderByFechaInicioAsc();
    }

    public Optional<Competicion> buscarPorId(String id) {
        return competicionRepository.findById(id);
    }

    public Competicion guardar(Competicion competicion) {
        return competicionRepository.save(competicion);
    }

    public Optional<Competicion> actualizar(String id, Competicion competicion) {
        return competicionRepository.findById(id).map(existente -> {
            existente.setNombre(competicion.getNombre());
            existente.setMontoPremio(competicion.getMontoPremio());
            existente.setFechaInicio(competicion.getFechaInicio());
            existente.setFechaFin(competicion.getFechaFin());
            return competicionRepository.save(existente);
        });
    }

    public boolean eliminar(String id) {
        if (competicionRepository.existsById(id)) {
            competicionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
