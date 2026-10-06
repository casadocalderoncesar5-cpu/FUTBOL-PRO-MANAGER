package com.futbol.mongorest.service;

import com.futbol.mongorest.model.Club;
import com.futbol.mongorest.model.Jugador;
import com.futbol.mongorest.repository.ClubMongoRepository;
import com.futbol.mongorest.repository.JugadorMongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JugadorMongoService {

    private final JugadorMongoRepository jugadorRepository;
    private final ClubMongoRepository clubRepository;

    public JugadorMongoService(JugadorMongoRepository jugadorRepository, ClubMongoRepository clubRepository) {
        this.jugadorRepository = jugadorRepository;
        this.clubRepository = clubRepository;
    }

    public List<Jugador> listarTodos() {
        return jugadorRepository.findAll();
    }

    public Optional<Jugador> buscarPorId(String id) {
        return jugadorRepository.findById(id);
    }

    public Jugador guardar(Jugador jugador, String clubId) {
        if (jugador.getId() != null && jugador.getId().trim().isEmpty()) {
            jugador.setId(null);
        }
        if (jugador.getApellido() == null || jugador.getApellido().trim().isEmpty()) {
            jugador.setApellido("-");
        }
        Jugador guardado = jugadorRepository.save(jugador);
        actualizarVinculacionClub(guardado, clubId != null ? clubId : jugador.getClubId());
        return guardado;
    }

    public Jugador guardar(Jugador jugador) {
        String cid = jugador.getClubIdSeleccionado() != null ? jugador.getClubIdSeleccionado() : jugador.getClubId();
        return guardar(jugador, cid);
    }

    public Optional<Jugador> actualizar(String id, Jugador jugador) {
        return jugadorRepository.findById(id).map(existente -> {
            existente.setNombre(jugador.getNombre());
            existente.setApellido(jugador.getApellido());
            existente.setNumero(jugador.getNumero());
            existente.setPosicion(jugador.getPosicion());
            Jugador guardado = jugadorRepository.save(existente);
            actualizarVinculacionClub(guardado, jugador.getClubId());
            return guardado;
        });
    }

    public boolean eliminar(String id) {
        if (jugadorRepository.existsById(id)) {
            List<Club> clubes = clubRepository.findAll();
            for (Club c : clubes) {
                if (c.getJugadores() != null) {
                    boolean removed = c.getJugadores().removeIf(j -> j != null && id.equals(j.getId()));
                    if (removed) {
                        clubRepository.save(c);
                    }
                }
            }
            jugadorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void actualizarVinculacionClub(Jugador jugador, String nuevoClubId) {
        if (jugador == null || jugador.getId() == null) return;
        List<Club> clubes = clubRepository.findAll();
        for (Club c : clubes) {
            boolean modificado = false;
            if (c.getJugadores() != null) {
                if (nuevoClubId == null || nuevoClubId.trim().isEmpty() || !c.getId().equals(nuevoClubId)) {
                    if (c.getJugadores().removeIf(j -> j != null && jugador.getId().equals(j.getId()))) {
                        modificado = true;
                    }
                }
            }
            if (nuevoClubId != null && !nuevoClubId.trim().isEmpty() && c.getId().equals(nuevoClubId)) {
                if (c.getJugadores() == null) {
                    c.setJugadores(new ArrayList<>());
                }
                boolean yaEsta = c.getJugadores().stream().anyMatch(j -> j != null && jugador.getId().equals(j.getId()));
                if (!yaEsta) {
                    c.getJugadores().add(jugador);
                    modificado = true;
                }
            }
            if (modificado) {
                clubRepository.save(c);
            }
        }
    }
}
