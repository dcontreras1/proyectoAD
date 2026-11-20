package com.uao.bienestarencasa.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CrearReservaRequest {
    @NotBlank
    private String clienteId;

    @NotBlank
    private String proveedorId;

    @NotBlank
    private String servicioId;

    @NotNull
    private Instant fechaHoraDeseada;

    private String direccion;
    private String barrioSector;
    private String infoComplementaria;
    private String observaciones;

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(String proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public Instant getFechaHoraDeseada() {
        return fechaHoraDeseada;
    }

    public void setFechaHoraDeseada(Instant fechaHoraDeseada) {
        this.fechaHoraDeseada = fechaHoraDeseada;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrioSector() {
        return barrioSector;
    }

    public void setBarrioSector(String barrioSector) {
        this.barrioSector = barrioSector;
    }

    public String getInfoComplementaria() {
        return infoComplementaria;
    }

    public void setInfoComplementaria(String infoComplementaria) {
        this.infoComplementaria = infoComplementaria;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}