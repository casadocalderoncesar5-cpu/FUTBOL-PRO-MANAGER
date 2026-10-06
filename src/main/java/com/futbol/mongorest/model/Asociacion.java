package com.futbol.mongorest.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "asociaciones")
public class Asociacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "El nombre de la asociación es obligatorio")
    private String nombre;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "El presidente es obligatorio")
    private String presidente;

    @NotBlank(message = "Las siglas son obligatorias")
    private String siglas;

    public Asociacion() {
    }

    public Asociacion(String id, String nombre, String pais, String presidente, String siglas) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.presidente = presidente;
        this.siglas = siglas;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPresidente() {
        return presidente;
    }

    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asociacion that = (Asociacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
