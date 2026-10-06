package com.futbol.mongorest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ClubRequestDTO {

    @NotBlank(message = "El nombre del club es obligatorio")
    private String nombre;

    private String entrenadorId;

    private String asociacionId;

    private List<String> jugadoresIds;

    private List<String> competicionesIds;

    public ClubRequestDTO() {
    }

    public ClubRequestDTO(String nombre, String entrenadorId, String asociacionId, List<String> jugadoresIds, List<String> competicionesIds) {
        this.nombre = nombre;
        this.entrenadorId = entrenadorId;
        this.asociacionId = asociacionId;
        this.jugadoresIds = jugadoresIds;
        this.competicionesIds = competicionesIds;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }

    public String getAsociacionId() {
        return asociacionId;
    }

    public void setAsociacionId(String asociacionId) {
        this.asociacionId = asociacionId;
    }

    public List<String> getJugadoresIds() {
        return jugadoresIds;
    }

    public void setJugadoresIds(List<String> jugadoresIds) {
        this.jugadoresIds = jugadoresIds;
    }

    public List<String> getCompeticionesIds() {
        return competicionesIds;
    }

    public void setCompeticionesIds(List<String> competicionesIds) {
        this.competicionesIds = competicionesIds;
    }
}
