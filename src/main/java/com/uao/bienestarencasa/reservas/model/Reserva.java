package com.uao.bienestarencasa.reservas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "reservas")
public class Reserva {
    @Id
    private String id;
    private String clienteId;
    private String proveedorId;
    private String servicioId;
    private ServicioSnapshot servicioSnapshot;
    private Instant fechaHoraInicio;
    private Instant fechaHoraFin;
    private String direccion;
    private String barrioSector;
    private String infoComplementaria;
    private String observaciones;
    private EstadoReserva estado;
    private String motivo;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public ServicioSnapshot getServicioSnapshot() {
        return servicioSnapshot;
    }

    public void setServicioSnapshot(ServicioSnapshot servicioSnapshot) {
        this.servicioSnapshot = servicioSnapshot;
    }

    public Instant getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Instant fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Instant getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Instant fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
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

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Instant fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}