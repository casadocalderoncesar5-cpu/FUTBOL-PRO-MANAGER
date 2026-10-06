package com.futbol.mongorest.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "clubes")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "El nombre del club es obligatorio")
    private String nombre;

    /**
     * Referencia no relacional hacia el documento Entrenador.
     * En MongoDB almacena únicamente el identificador (o DBRef) resolviendo
     * la asociación de forma desacoplada y transparente.
     */
    @DocumentReference
    private Entrenador entrenador;

    /**
     * Referencia no relacional hacia la colección de Jugadores.
     * Almacena un array de IDs de la colección 'jugadores'.
     */
    @DocumentReference
    private List<Jugador> jugadores = new ArrayList<>();

    /**
     * Referencia no relacional a la Asociación a la que pertenece el club.
     */
    @DocumentReference
    private Asociacion asociacion;

    /**
     * Referencia no relacional a las Competiciones en las que compite el club.
     */
    @DocumentReference
    private List<Competicion> competiciones = new ArrayList<>();

    public Club() {
    }

    public Club(String id, String nombre, Entrenador entrenador, Asociacion asociacion) {
        this.id = id;
        this.nombre = nombre;
        this.entrenador = entrenador;
        this.asociacion = asociacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public List<Competicion> getCompeticiones() {
        return competiciones;
    }

    public void setCompeticiones(List<Competicion> competiciones) {
        this.competiciones = competiciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return Objects.equals(id, club.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
