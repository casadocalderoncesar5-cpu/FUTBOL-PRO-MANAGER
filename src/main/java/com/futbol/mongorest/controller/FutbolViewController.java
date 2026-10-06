package com.futbol.mongorest.controller;

import com.futbol.mongorest.model.Club;
import com.futbol.mongorest.model.Jugador;
import com.futbol.mongorest.repository.*;
import com.futbol.mongorest.service.JugadorMongoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class FutbolViewController {

    private final ClubMongoRepository clubRepo;
    private final EntrenadorMongoRepository entrenadorRepo;
    private final JugadorMongoRepository jugadorRepo;
    private final AsociacionMongoRepository asociacionRepo;
    private final CompeticionMongoRepository competicionRepo;
    private final ArbitroMongoRepository arbitroRepo;
    private final JugadorMongoService jugadorService;

    public FutbolViewController(ClubMongoRepository clubRepo,
                                EntrenadorMongoRepository entrenadorRepo,
                                JugadorMongoRepository jugadorRepo,
                                AsociacionMongoRepository asociacionRepo,
                                CompeticionMongoRepository competicionRepo,
                                ArbitroMongoRepository arbitroRepo,
                                JugadorMongoService jugadorService) {
        this.clubRepo = clubRepo;
        this.entrenadorRepo = entrenadorRepo;
        this.jugadorRepo = jugadorRepo;
        this.asociacionRepo = asociacionRepo;
        this.competicionRepo = competicionRepo;
        this.arbitroRepo = arbitroRepo;
        this.jugadorService = jugadorService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalClubes", clubRepo.count());
        model.addAttribute("totalEntrenadores", entrenadorRepo.count());
        model.addAttribute("totalJugadores", jugadorRepo.count());
        model.addAttribute("totalAsociaciones", asociacionRepo.count());
        model.addAttribute("totalCompeticiones", competicionRepo.count());
        model.addAttribute("totalArbitros", arbitroRepo.count());
        return "index";
    }

    @GetMapping("/club-rest")
    public String clubRest(Model model) {
        model.addAttribute("entrenadores", entrenadorRepo.findAll());
        model.addAttribute("asociaciones", asociacionRepo.findAll());
        model.addAttribute("competiciones", competicionRepo.findAll());
        model.addAttribute("jugadores", jugadorRepo.findAll());
        return "club_rest";
    }

    @GetMapping("/listar-rest")
    public String listarRest() {
        return "listar_rest";
    }

    @GetMapping("/competiciones")
    public String competiciones(Model model) {
        model.addAttribute("competiciones", competicionRepo.findAll());
        return "competiciones";
    }

    @GetMapping("/asociaciones")
    public String asociaciones(Model model) {
        model.addAttribute("asociaciones", asociacionRepo.findAll());
        return "asociaciones";
    }

    @GetMapping("/entrenadores")
    public String entrenadores(Model model) {
        model.addAttribute("entrenadores", entrenadorRepo.findAll());
        return "entrenadores";
    }

    @GetMapping("/jugadores")
    public String jugadores(Model model) {
        List<Jugador> jugadores = jugadorRepo.findAll();
        List<Club> clubes = clubRepo.findAll();
        Map<String, String> jugadorClubMap = new HashMap<>();
        Map<String, String> jugadorClubIdMap = new HashMap<>();
        for (Club c : clubes) {
            if (c.getJugadores() != null) {
                for (Jugador j : c.getJugadores()) {
                    if (j != null && j.getId() != null) {
                        jugadorClubMap.put(j.getId(), c.getNombre());
                        jugadorClubIdMap.put(j.getId(), c.getId());
                    }
                }
            }
        }
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("jugadorClubMap", jugadorClubMap);
        model.addAttribute("jugadorClubIdMap", jugadorClubIdMap);
        model.addAttribute("clubes", clubes);
        return "jugadores";
    }

    @GetMapping("/jugadores/nuevo")
    public String jugadorNuevo(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("clubes", clubRepo.findAll());
        return "jugadores/form";
    }

    @GetMapping("/jugadores/editar/{id}")
    public String jugadorEditar(@PathVariable("id") String id, Model model) {
        Optional<Jugador> jug = jugadorRepo.findById(id);
        if (jug.isPresent()) {
            Jugador jugador = jug.get();
            for (Club c : clubRepo.findAll()) {
                if (c.getJugadores() != null && c.getJugadores().stream().anyMatch(j -> j != null && id.equals(j.getId()))) {
                    jugador.setClubIdSeleccionado(c.getId());
                    break;
                }
            }
            model.addAttribute("jugador", jugador);
            model.addAttribute("clubes", clubRepo.findAll());
            return "jugadores/form";
        }
        return "redirect:/jugadores";
    }

    @PostMapping("/jugadores/guardar")
    public String jugadorGuardar(@ModelAttribute("jugador") Jugador jugador,
                                 @RequestParam(value = "clubId", required = false) String clubId) {
        jugadorService.guardar(jugador, clubId);
        return "redirect:/jugadores";
    }

    @GetMapping("/jugadores/eliminar/{id}")
    public String jugadorEliminar(@PathVariable("id") String id) {
        jugadorService.eliminar(id);
        return "redirect:/jugadores";
    }

    @GetMapping("/arbitros")
    public String arbitros(Model model) {
        model.addAttribute("arbitros", arbitroRepo.findAll());
        return "arbitros";
    }
}
