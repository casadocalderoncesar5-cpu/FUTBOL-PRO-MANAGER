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
        };
    }
}
