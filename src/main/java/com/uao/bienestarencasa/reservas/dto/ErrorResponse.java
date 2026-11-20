package com.uao.bienestarencasa.reservas.dto;

import java.time.Instant;

public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String mensaje;

    public ErrorResponse(Instant timestamp, int status, String error, String mensaje) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }
}