package com.uao.bienestarencasa.reservas.model;

import java.util.List;

public class PerfilProfesional {
    private String titulo;
    private Integer aniosExperiencia;
    private List<String> zonasOrientativas;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(Integer aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public List<String> getZonasOrientativas() {
        return zonasOrientativas;
    }

    public void setZonasOrientativas(List<String> zonasOrientativas) {
        this.zonasOrientativas = zonasOrientativas;
    }
}