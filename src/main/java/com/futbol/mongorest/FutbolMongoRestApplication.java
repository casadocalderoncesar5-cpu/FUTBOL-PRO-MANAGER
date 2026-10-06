package com.futbol.mongorest;

import com.futbol.mongorest.model.*;
import com.futbol.mongorest.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class FutbolMongoRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FutbolMongoRestApplication.class, args);
    }

    @Bean
    public CommandLineRunner initMongoData(
            ClubMongoRepository clubRepo,
            EntrenadorMongoRepository entrenadorRepo,
            JugadorMongoRepository jugadorRepo,
            AsociacionMongoRepository asociacionRepo,
            CompeticionMongoRepository competicionRepo) {
        return args -> {
            try {
                // Solo sembrar si no hay datos
                if (clubRepo.count() == 0) {
                    // 1. Asociaciones
                    Asociacion fcf = asociacionRepo.save(new Asociacion(null, "Federación Colombiana de Fútbol", "Colombia", "Ramón Jesurún", "FCF"));
                    Asociacion afa = asociacionRepo.save(new Asociacion(null, "Asociación del Fútbol Argentino", "Argentina", "Claudio Tapia", "AFA"));

                    // 2. Entrenadores
                    Entrenador gamero = entrenadorRepo.save(new Entrenador(null, "Alberto", "Gamero", 60, "Colombiana"));
                    Entrenador peirano = entrenadorRepo.save(new Entrenador(null, "Pablo", "Peirano", 49, "Uruguaya"));

                    // 3. Competiciones
                    Competicion liga = competicionRepo.save(new Competicion(null, "Liga BetPlay", 1000000, LocalDate.of(2026, 1, 20), LocalDate.of(2026, 6, 15)));
                    Competicion libertadores = competicionRepo.save(new Competicion(null, "Copa Libertadores", 18000000, LocalDate.of(2026, 2, 10), LocalDate.of(2026, 11, 28)));

                    // 4. Jugadores
                    Jugador j1 = jugadorRepo.save(new Jugador(null, "David", "Mackalister Silva", 14, "Mediocampista"));
                    Jugador j2 = jugadorRepo.save(new Jugador(null, "Álvaro", "Montero", 31, "Portero"));

                    // 5. Club con @DocumentReference
                    Club club = new Club();
                    club.setNombre("Millonarios F.C.");
                    club.setEntrenador(gamero);
                    club.setAsociacion(fcf);
                    club.setJugadores(new ArrayList<>(Arrays.asList(j1, j2)));
                    club.setCompeticiones(new ArrayList<>(Arrays.asList(liga, libertadores)));
                    clubRepo.save(club);

                    System.out.println(">>> Datos iniciales sembrados con éxito en MongoDB <<<");
                }

                // Asegurar que los futbolistas adicionales del pantallazo estén disponibles
                if (jugadorRepo.count() < 6) {
                    boolean hasRodallega = jugadorRepo.findAll().stream().anyMatch(j -> "Rodallega".equalsIgnoreCase(j.getApellido()));
                    if (!hasRodallega) {
                        Jugador rodallega = jugadorRepo.save(new Jugador(null, "Hugo", "Rodallega", 11, "Delantero"));
                        Jugador torres = jugadorRepo.save(new Jugador(null, "Daniel", "Torres", 16, "Mediocampista"));
                        Jugador falcao = jugadorRepo.save(new Jugador(null, "Radamel", "Falcao García", 9, "Delantero"));
                        Jugador cuadrado = jugadorRepo.save(new Jugador(null, "Juan", "Cuadrado", 11, "Extremo"));

                        clubRepo.findByNombreIgnoreCase("Millonarios F.C.").ifPresent(m -> {
                            if (m.getJugadores() == null) m.setJugadores(new ArrayList<>());
                            if (m.getJugadores().stream().noneMatch(j -> j != null && "Cuadrado".equalsIgnoreCase(j.getApellido()))) {
                                m.getJugadores().add(cuadrado);
                                clubRepo.save(m);
                            }
                        });

                        Optional<Club> santaFeOpt = clubRepo.findByNombreIgnoreCase("Independiente Santa Fe");
                        if (santaFeOpt.isPresent()) {
                            Club sf = santaFeOpt.get();
                            if (sf.getJugadores() == null) sf.setJugadores(new ArrayList<>());
                            if (sf.getJugadores().stream().noneMatch(j -> j != null && "Rodallega".equalsIgnoreCase(j.getApellido()))) {
                                sf.getJugadores().add(rodallega);
                            }
                            if (sf.getJugadores().stream().noneMatch(j -> j != null && "Torres".equalsIgnoreCase(j.getApellido()))) {
                                sf.getJugadores().add(torres);
                            }
                            clubRepo.save(sf);
                        } else {
                            Entrenador peirano = entrenadorRepo.findAll().stream()
                                    .filter(e -> "Peirano".equalsIgnoreCase(e.getApellido()))
                                    .findFirst()
                                    .orElseGet(() -> entrenadorRepo.save(new Entrenador(null, "Pablo", "Peirano", 49, "Uruguaya")));
                            Asociacion fcf = asociacionRepo.findAll().stream().findFirst().orElse(null);
                            Competicion liga = competicionRepo.findAll().stream().findFirst().orElse(null);

                            Club santaFe = new Club();
                            santaFe.setId("3");
                            santaFe.setNombre("Independiente Santa Fe");
                            santaFe.setEntrenador(peirano);
                            santaFe.setAsociacion(fcf);
                            santaFe.setJugadores(new ArrayList<>(Arrays.asList(rodallega, torres)));
                            if (liga != null) {
                                santaFe.setCompeticiones(new ArrayList<>(Arrays.asList(liga)));
                            }
                            clubRepo.save(santaFe);
                        }
                    }
                }

                // Normalizar IDs de clubes existentes para garantizar orden correlativo (#1, #2, #3, ...)
                List<Club> todosLosClubes = clubRepo.findAll();
                long correlativo = 1;
                for (Club c : todosLosClubes) {
                    boolean necesitaNuevoId = false;
                    if (c.getId() == null) {
                        necesitaNuevoId = true;
                    } else {
                        try {
                            long val = Long.parseLong(c.getId().trim());
                            if (val > 100 || c.getId().length() > 5) {
                                necesitaNuevoId = true;
                            }
                        } catch (Exception e) {
                            necesitaNuevoId = true;
                        }
                    }
                    if (necesitaNuevoId) {
                        String oldId = c.getId();
                        if (oldId != null) {
                            clubRepo.deleteById(oldId);
                        }
                        c.setId(String.valueOf(correlativo));
                        clubRepo.save(c);
                    }
                    correlativo++;
                }
            } catch (Exception e) {
                System.err.println(">>> Advertencia de inicio MongoDB: " + e.getMessage() + " (El servidor continuará activo)");
            }
        };
    }
}
