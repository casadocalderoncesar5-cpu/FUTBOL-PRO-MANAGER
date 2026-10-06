package com.futbol.mongorest.service;

import com.futbol.mongorest.model.Entrenador;
import com.futbol.mongorest.repository.EntrenadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntrenadorMongoService {

    private final EntrenadorMongoRepository entrenadorRepository;

    public EntrenadorMongoService(EntrenadorMongoRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    public List<Entrenador> listarTodos() {
        return entrenadorRepository.findAll();
    }

    public Optional<Entrenador> buscarPorId(String id) {
        return entrenadorRepository.findById(id);
    }

    public Entrenador guardar(Entrenador entrenador) {
        if (entrenador.getId() != null && entrenador.getId().trim().isEmpty()) {
            entrenador.setId(null);
        }
        return entrenadorRepository.save(entrenador);
    }

    public Optional<Entrenador> actualizar(String id, Entrenador entrenador) {
        return entrenadorRepository.findById(id).map(existente -> {
            existente.setNombre(entrenador.getNombre());
            existente.setApellido(entrenador.getApellido());
            existente.setEdad(entrenador.getEdad());
            existente.setNacionalidad(entrenador.getNacionalidad());
            return entrenadorRepository.save(existente);
        });
    }

    public boolean eliminar(String id) {
        if (entrenadorRepository.existsById(id)) {
            entrenadorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
