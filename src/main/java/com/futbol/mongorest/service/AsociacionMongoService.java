package com.futbol.mongorest.service;

import com.futbol.mongorest.model.Asociacion;
import com.futbol.mongorest.repository.AsociacionMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsociacionMongoService {

    private final AsociacionMongoRepository asociacionRepository;

    public AsociacionMongoService(AsociacionMongoRepository asociacionRepository) {
        this.asociacionRepository = asociacionRepository;
    }

    public List<Asociacion> listarTodas() {
        return asociacionRepository.findAll();
    }

    public Optional<Asociacion> buscarPorId(String id) {
        return asociacionRepository.findById(id);
    }

    public Asociacion guardar(Asociacion asociacion) {
        return asociacionRepository.save(asociacion);
    }

    public Optional<Asociacion> actualizar(String id, Asociacion asociacion) {
        return asociacionRepository.findById(id).map(existente -> {
            existente.setNombre(asociacion.getNombre());
            existente.setPais(asociacion.getPais());
            existente.setPresidente(asociacion.getPresidente());
            existente.setSiglas(asociacion.getSiglas());
            return asociacionRepository.save(existente);
        });
    }

    public boolean eliminar(String id) {
        if (asociacionRepository.existsById(id)) {
            asociacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
