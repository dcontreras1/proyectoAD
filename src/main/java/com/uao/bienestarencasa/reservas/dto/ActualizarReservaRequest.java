package com.uao.bienestarencasa.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public class ActualizarReservaRequest {
    @NotBlank
    private String actor;

    @NotBlank
    private String accion;

    private Instant nuevaFechaHora;
    private String motivo;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Instant getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    public void setNuevaFechaHora(Instant nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}