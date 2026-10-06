package com.futbol.mongorest.service;

import com.futbol.mongorest.dto.ClubRequestDTO;
import com.futbol.mongorest.model.*;
import com.futbol.mongorest.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubMongoService {

    private final ClubMongoRepository clubRepository;
    private final EntrenadorMongoRepository entrenadorRepository;
    private final AsociacionMongoRepository asociacionRepository;
    private final JugadorMongoRepository jugadorRepository;
    private final CompeticionMongoRepository competicionRepository;

    public ClubMongoService(ClubMongoRepository clubRepository,
                            EntrenadorMongoRepository entrenadorRepository,
                            AsociacionMongoRepository asociacionRepository,
                            JugadorMongoRepository jugadorRepository,
                            CompeticionMongoRepository competicionRepository) {
        this.clubRepository = clubRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.asociacionRepository = asociacionRepository;
        this.jugadorRepository = jugadorRepository;
        this.competicionRepository = competicionRepository;
    }

    public List<Club> listarTodos() {
        List<Club> todos = clubRepository.findAll();
        todos.sort((a, b) -> {
            try {
                long idA = Long.parseLong(a.getId() != null ? a.getId().trim() : "0");
                long idB = Long.parseLong(b.getId() != null ? b.getId().trim() : "0");
                return Long.compare(idA, idB);
            } catch (Exception e) {
                return (a.getId() != null ? a.getId() : "").compareTo(b.getId() != null ? b.getId() : "");
            }
        });
        return todos;
    }

    public Optional<Club> buscarPorId(String id) {
        return clubRepository.findById(id);
    }

    public synchronized String generarSiguienteId() {
        long maxId = 0;
        List<Club> todos = clubRepository.findAll();
        for (Club c : todos) {
            if (c.getId() != null) {
                try {
                    long val = Long.parseLong(c.getId().trim());
                    if (val > maxId) {
                        maxId = val;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        if (maxId == 0) {
            maxId = todos.size();
        }
        return String.valueOf(maxId + 1);
    }

    public Club crearDesdeDto(ClubRequestDTO dto) {
        Club club = new Club();
        club.setId(generarSiguienteId());
        club.setNombre(dto.getNombre());
        mapearReferencias(club, dto);
        return clubRepository.save(club);
    }

    public Optional<Club> actualizarDesdeDto(String id, ClubRequestDTO dto) {
        return clubRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            mapearReferencias(existente, dto);
            return clubRepository.save(existente);
        });
    }

    public Club guardar(Club club) {
        if (club.getId() == null || club.getId().trim().isEmpty()) {
            club.setId(generarSiguienteId());
        }
        return clubRepository.save(club);
    }

    public boolean eliminar(String id) {
        if (clubRepository.existsById(id)) {
            clubRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void mapearReferencias(Club club, ClubRequestDTO dto) {
        if (dto.getEntrenadorId() != null) {
            entrenadorRepository.findById(dto.getEntrenadorId()).ifPresent(club::setEntrenador);
        } else {
            club.setEntrenador(null);
        }

        if (dto.getAsociacionId() != null) {
            asociacionRepository.findById(dto.getAsociacionId()).ifPresent(club::setAsociacion);
        } else {
            club.setAsociacion(null);
        }

        if (dto.getJugadoresIds() != null && !dto.getJugadoresIds().isEmpty()) {
            List<Jugador> jugadores = jugadorRepository.findAllById(dto.getJugadoresIds());
            club.setJugadores(jugadores);
        } else {
            club.setJugadores(new ArrayList<>());
        }

        if (dto.getCompeticionesIds() != null && !dto.getCompeticionesIds().isEmpty()) {
            List<Competicion> competiciones = competicionRepository.findAllById(dto.getCompeticionesIds());
            club.setCompeticiones(competiciones);
        } else {
            club.setCompeticiones(new ArrayList<>());
        }
    }
}
