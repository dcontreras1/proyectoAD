package com.uao.bienestarencasa.reservas.controller;

import com.uao.bienestarencasa.reservas.dto.ActualizarReservaRequest;
import com.uao.bienestarencasa.reservas.dto.CrearReservaRequest;
import com.uao.bienestarencasa.reservas.model.EstadoReserva;
import com.uao.bienestarencasa.reservas.model.Reserva;
import com.uao.bienestarencasa.reservas.service.ReservaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@Valid @RequestBody CrearReservaRequest request) {
        Reserva creada = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(
            @PathVariable String id,
            @Valid @RequestBody ActualizarReservaRequest request) {
        Reserva actualizada = reservaService.actualizarReserva(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> consultarPorId(@PathVariable String id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> consultar(
            @RequestParam(required = false) String clienteId,
            @RequestParam(required = false) String proveedorId,
            @RequestParam(required = false) EstadoReserva estado) {
        if (clienteId != null) {
            return ResponseEntity.ok(reservaService.consultarPorCliente(clienteId));
        }
        if (proveedorId != null) {
            return ResponseEntity.ok(reservaService.consultarPorProveedor(proveedorId));
        }
        if (estado != null) {
            return ResponseEntity.ok(reservaService.consultarPorEstado(estado));
        }
        return ResponseEntity.badRequest().build();
    }
}