package com.uao.bienestarencasa.reservas.service;

import com.uao.bienestarencasa.reservas.dto.ActualizarReservaRequest;
import com.uao.bienestarencasa.reservas.dto.CrearReservaRequest;
import com.uao.bienestarencasa.reservas.exception.RecursoNoEncontradoException;
import com.uao.bienestarencasa.reservas.exception.ReglaNegocioException;
import com.uao.bienestarencasa.reservas.model.EstadoReserva;
import com.uao.bienestarencasa.reservas.model.PeriodoNoDisponibilidad;
import com.uao.bienestarencasa.reservas.model.Reserva;
import com.uao.bienestarencasa.reservas.model.RolUsuario;
import com.uao.bienestarencasa.reservas.model.Servicio;
import com.uao.bienestarencasa.reservas.model.ServicioSnapshot;
import com.uao.bienestarencasa.reservas.model.Usuario;
import com.uao.bienestarencasa.reservas.repository.ReservaRepository;
import com.uao.bienestarencasa.reservas.repository.ServicioRepository;
import com.uao.bienestarencasa.reservas.repository.UsuarioRepository;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ReservaService {

    private static final ZoneId ZONA_COLOMBIA = ZoneId.of("America/Bogota");

    private static final Set<EstadoReserva> ESTADOS_QUE_OCUPAN_FRANJA =
            EnumSet.of(EstadoReserva.PENDIENTE, EstadoReserva.SUGERIDA, EstadoReserva.ACEPTADA);

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            ServicioRepository servicioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
    }

    public Reserva crearReserva(CrearReservaRequest request) {
        Usuario cliente = usuarioRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        if (cliente.getRol() != RolUsuario.CLIENTE) {
            throw new ReglaNegocioException("El usuario indicado como cliente no tiene rol CLIENTE");
        }

        Usuario proveedor = usuarioRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));
        if (proveedor.getRol() != RolUsuario.PROVEEDOR) {
            throw new ReglaNegocioException("El usuario indicado como proveedor no tiene rol PROVEEDOR");
        }

        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado"));
        if (!servicio.getProveedorId().equals(proveedor.getId())) {
            throw new ReglaNegocioException("El servicio no pertenece al proveedor indicado");
        }
        if (!"ACTIVO".equalsIgnoreCase(servicio.getEstado())) {
            throw new ReglaNegocioException("El servicio no está activo");
        }

        String direccionFinal = StringUtils.hasText(request.getDireccion())
                ? request.getDireccion().trim()
                : cliente.getDireccionPrincipal();
        if (!StringUtils.hasText(direccionFinal)) {
            throw new ReglaNegocioException("Debe suministrarse una dirección o contar con dirección principal");
        }

        Instant inicio = request.getFechaHoraDeseada();
        Instant fin = inicio.plusSeconds(servicio.getDuracionMinutos().longValue() * 60L);

        validarSinCruces(proveedor.getId(), null, inicio, fin);

        Instant ahora = Instant.now();
        Reserva reserva = new Reserva();
        reserva.setClienteId(cliente.getId());
        reserva.setProveedorId(proveedor.getId());
        reserva.setServicioId(servicio.getId());
        reserva.setServicioSnapshot(new ServicioSnapshot(
                servicio.getNombre(),
                servicio.getPrecio(),
                servicio.getDuracionMinutos()
        ));
        reserva.setFechaHoraInicio(inicio);
        reserva.setFechaHoraFin(fin);
        reserva.setDireccion(direccionFinal);
        reserva.setBarrioSector(request.getBarrioSector());
        reserva.setInfoComplementaria(request.getInfoComplementaria());
        reserva.setObservaciones(request.getObservaciones());
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setMotivo(null);
        reserva.setFechaCreacion(ahora);
        reserva.setFechaActualizacion(ahora);

        return reservaRepository.save(reserva);
    }

    public Reserva actualizarReserva(String id, ActualizarReservaRequest request) {
        Reserva reserva = obtenerPorId(id);
        Usuario proveedor = usuarioRepository.findById(reserva.getProveedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));

        String actor = request.getActor().trim().toUpperCase();
        String accion = request.getAccion().trim().toUpperCase();
        EstadoReserva estadoActual = reserva.getEstado();

        switch (accion) {
            case "ACEPTAR" -> {
                if (!"PROVEEDOR".equals(actor) || estadoActual != EstadoReserva.PENDIENTE) {
                    throw new ReglaNegocioException("Transición no permitida para el actor o estado actual");
                }
                validarDisponibilidadProveedor(proveedor, reserva.getFechaHoraInicio(), reserva.getFechaHoraFin());
                validarSinCruces(proveedor.getId(), reserva.getId(), reserva.getFechaHoraInicio(), reserva.getFechaHoraFin());
                reserva.setEstado(EstadoReserva.ACEPTADA);
            }
            case "RECHAZAR" -> {
                if (!"PROVEEDOR".equals(actor) || estadoActual != EstadoReserva.PENDIENTE) {
                    throw new ReglaNegocioException("Solo el proveedor puede rechazar una reserva en estado Pendiente");
                }
                exigirMotivo(request.getMotivo());
                reserva.setMotivo(request.getMotivo().trim());
                reserva.setEstado(EstadoReserva.RECHAZADA);
            }
            case "PROPONER_OTRA_FECHA_Y_HORA" -> {
                if (!"PROVEEDOR".equals(actor) || estadoActual != EstadoReserva.PENDIENTE) {
                    throw new ReglaNegocioException("Solo el proveedor puede proponer otra fecha y hora en estado Pendiente");
                }
                if (request.getNuevaFechaHora() == null) {
                    throw new ReglaNegocioException("Debe indicar nuevaFechaHora para proponer un horario alternativo");
                }
                int duracionMin = reserva.getServicioSnapshot().getDuracionMinutos();
                Instant nuevoInicio = request.getNuevaFechaHora();
                Instant nuevoFin = nuevoInicio.plusSeconds(duracionMin * 60L);
                validarSinCruces(proveedor.getId(), reserva.getId(), nuevoInicio, nuevoFin);
                reserva.setFechaHoraInicio(nuevoInicio);
                reserva.setFechaHoraFin(nuevoFin);
                reserva.setEstado(EstadoReserva.SUGERIDA);
            }
            case "ACEPTAR_PROPUESTA" -> {
                if (!"CLIENTE".equals(actor) || estadoActual != EstadoReserva.SUGERIDA) {
                    throw new ReglaNegocioException("Solo el cliente puede aceptar una propuesta en estado Sugerida");
                }
                validarDisponibilidadProveedor(proveedor, reserva.getFechaHoraInicio(), reserva.getFechaHoraFin());
                validarSinCruces(proveedor.getId(), reserva.getId(), reserva.getFechaHoraInicio(), reserva.getFechaHoraFin());
                reserva.setEstado(EstadoReserva.ACEPTADA);
            }
            case "RECHAZAR_PROPUESTA" -> {
                if (!"CLIENTE".equals(actor) || estadoActual != EstadoReserva.SUGERIDA) {
                    throw new ReglaNegocioException("Solo el cliente puede rechazar una propuesta en estado Sugerida");
                }
                exigirMotivo(request.getMotivo());
                reserva.setMotivo(request.getMotivo().trim());
                reserva.setEstado(EstadoReserva.CANCELADA);
            }
            case "CANCELAR" -> {
                if ((!"CLIENTE".equals(actor) && !"PROVEEDOR".equals(actor)) || estadoActual != EstadoReserva.ACEPTADA) {
                    throw new ReglaNegocioException("Solo cliente o proveedor pueden cancelar una reserva en estado Aceptada");
                }
                exigirMotivo(request.getMotivo());
                reserva.setMotivo(request.getMotivo().trim());
                reserva.setEstado(EstadoReserva.CANCELADA);
            }
            case "MARCAR_COMO_PRESTADA" -> {
                if (!"PROVEEDOR".equals(actor) || estadoActual != EstadoReserva.ACEPTADA) {
                    throw new ReglaNegocioException("Solo el proveedor puede marcar como prestada una reserva Aceptada");
                }
                reserva.setEstado(EstadoReserva.COMPLETADA);
            }
            default -> throw new ReglaNegocioException("Acción no reconocida: " + request.getAccion());
        }

        reserva.setFechaActualizacion(Instant.now());
        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(String id) {
        Reserva reserva = obtenerPorId(id);
        if (reserva.getEstado() == EstadoReserva.ACEPTADA || reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new ReglaNegocioException(
                    "No se puede eliminar una reserva en estado " + reserva.getEstado()
            );
        }
        reservaRepository.deleteById(id);
    }

    public Reserva obtenerPorId(String id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con id: " + id));
    }

    public List<Reserva> consultarPorCliente(String clienteId) {
        if (!ObjectId.isValid(clienteId)) {
            return List.of();
        }
        return reservaRepository.findByClienteId(clienteId);
    }

    public List<Reserva> consultarPorProveedor(String proveedorId) {
        if (!ObjectId.isValid(proveedorId)) {
            return List.of();
        }
        return reservaRepository.findByProveedorId(proveedorId);
    }

    public List<Reserva> consultarPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    private void exigirMotivo(String motivo) {
        if (!StringUtils.hasText(motivo)) {
            throw new ReglaNegocioException("El motivo es obligatorio para rechazar o cancelar");
        }
    }

    private void validarSinCruces(String proveedorId, String reservaExcluirId, Instant inicio, Instant fin) {
        if (!inicio.isBefore(fin)) {
            throw new ReglaNegocioException("La hora de inicio debe ser anterior a la hora de fin");
        }
        List<Reserva> existentes = reservaRepository.findByProveedorIdAndEstadoIn(
                proveedorId, ESTADOS_QUE_OCUPAN_FRANJA
        );
        for (Reserva otra : existentes) {
            if (reservaExcluirId != null && reservaExcluirId.equals(otra.getId())) {
                continue;
            }
            boolean seCruzan = inicio.isBefore(otra.getFechaHoraFin()) && fin.isAfter(otra.getFechaHoraInicio());
            if (seCruzan) {
                throw new ReglaNegocioException("La franja horaria se cruza con otra reserva del proveedor");
            }
        }
    }

    private void validarDisponibilidadProveedor(Usuario proveedor, Instant inicio, Instant fin) {
        if (proveedor.getNoDisponibilidad() != null) {
            for (PeriodoNoDisponibilidad periodo : proveedor.getNoDisponibilidad()) {
                boolean coincideBloqueo = inicio.isBefore(periodo.getFechaFin()) && fin.isAfter(periodo.getFechaInicio());
                if (coincideBloqueo) {
                    throw new ReglaNegocioException("El proveedor no está disponible en el periodo solicitado");
                }
            }
        }

        LocalDateTime inicioLocal = LocalDateTime.ofInstant(inicio, ZONA_COLOMBIA);
        LocalDateTime finLocal = LocalDateTime.ofInstant(fin, ZONA_COLOMBIA);

        if (!inicioLocal.toLocalDate().equals(finLocal.toLocalDate())) {
            throw new ReglaNegocioException("La reserva debe iniciar y finalizar el mismo día dentro de la disponibilidad");
        }

        DayOfWeek dia = inicioLocal.getDayOfWeek();
        LocalTime horaInicio = inicioLocal.toLocalTime();
        LocalTime horaFin = finLocal.toLocalTime();

        boolean dentroHorarioGeneral = proveedor.getDisponibilidadHoraria() != null
                && proveedor.getDisponibilidadHoraria().stream().anyMatch(franja ->
                        franja.getDiaSemana() == dia
                                && !horaInicio.isBefore(franja.getHoraInicio())
                                && !horaFin.isAfter(franja.getHoraFin())
                );

        if (!dentroHorarioGeneral) {
            throw new ReglaNegocioException("El horario solicitado está fuera de la disponibilidad general del proveedor");
        }
    }
}