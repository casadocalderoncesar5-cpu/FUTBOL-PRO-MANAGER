package com.futbol.mongorest.service;

import com.futbol.mongorest.model.Jugador;
import com.futbol.mongorest.repository.JugadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JugadorMongoService {

    private final JugadorMongoRepository jugadorRepository;

    public JugadorMongoService(JugadorMongoRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public List<Jugador> listarTodos() {
        return jugadorRepository.findAll();
    }

    public Optional<Jugador> buscarPorId(String id) {
        return jugadorRepository.findById(id);
    }

    public Jugador guardar(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public Optional<Jugador> actualizar(String id, Jugador jugador) {
        return jugadorRepository.findById(id).map(existente -> {
            existente.setNombre(jugador.getNombre());
            existente.setApellido(jugador.getApellido());
            existente.setNumero(jugador.getNumero());
            existente.setPosicion(jugador.getPosicion());
            return jugadorRepository.save(existente);
        });
    }

    public boolean eliminar(String id) {
        if (jugadorRepository.existsById(id)) {
            jugadorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
