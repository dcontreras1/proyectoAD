package com.uao.bienestarencasa.reservas.model;

import java.math.BigDecimal;

public class ServicioSnapshot {
    private String nombre;
    private BigDecimal precio;
    private Integer duracionMinutos;

    public ServicioSnapshot() {
    }

    public ServicioSnapshot(String nombre, BigDecimal precio, Integer duracionMinutos) {
        this.nombre = nombre;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
}