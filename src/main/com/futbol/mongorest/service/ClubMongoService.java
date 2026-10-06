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
        return clubRepository.findAll();
    }

    public Optional<Club> buscarPorId(String id) {
        return clubRepository.findById(id);
    }

    public Club crearDesdeDto(ClubRequestDTO dto) {
        Club club = new Club();
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
