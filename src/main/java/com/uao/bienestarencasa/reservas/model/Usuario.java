package com.uao.bienestarencasa.reservas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private RolUsuario rol;
    private String direccionPrincipal;
    private PerfilProfesional perfilProfesional;
    private List<FranjaDisponibilidad> disponibilidadHoraria = new ArrayList<>();
    private List<PeriodoNoDisponibilidad> noDisponibilidad = new ArrayList<>();

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getDireccionPrincipal() {
        return direccionPrincipal;
    }

    public void setDireccionPrincipal(String direccionPrincipal) {
        this.direccionPrincipal = direccionPrincipal;
    }

    public PerfilProfesional getPerfilProfesional() {
        return perfilProfesional;
    }

    public void setPerfilProfesional(PerfilProfesional perfilProfesional) {
        this.perfilProfesional = perfilProfesional;
    }

    public List<FranjaDisponibilidad> getDisponibilidadHoraria() {
        return disponibilidadHoraria;
    }

    public void setDisponibilidadHoraria(List<FranjaDisponibilidad> disponibilidadHoraria) {
        this.disponibilidadHoraria = disponibilidadHoraria;
    }

    public List<PeriodoNoDisponibilidad> getNoDisponibilidad() {
        return noDisponibilidad;
    }

    public void setNoDisponibilidad(List<PeriodoNoDisponibilidad> noDisponibilidad) {
        this.noDisponibilidad = noDisponibilidad;
    }
}