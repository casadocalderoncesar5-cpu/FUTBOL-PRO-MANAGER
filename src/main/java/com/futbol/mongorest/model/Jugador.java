package com.futbol.mongorest.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "jugadores")
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Min(value = 1, message = "El dorsal debe ser mayor a 0")
    private int numero;

    @NotBlank(message = "La posición es obligatoria")
    private String posicion;

    public Jugador() {
    }

    public Jugador(String id, String nombre, String apellido, int numero, String posicion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numero = numero;
        this.posicion = posicion;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @org.springframework.data.annotation.Transient
    private String clubId;

    @org.springframework.data.annotation.Transient
    private String clubIdSeleccionado;

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
        this.clubIdSeleccionado = clubId;
    }

    public String getClubIdSeleccionado() {
        if (clubIdSeleccionado != null) return clubIdSeleccionado;
        return clubId;
    }

    public void setClubIdSeleccionado(String clubIdSeleccionado) {
        this.clubIdSeleccionado = clubIdSeleccionado;
        this.clubId = clubIdSeleccionado;
    }

    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
