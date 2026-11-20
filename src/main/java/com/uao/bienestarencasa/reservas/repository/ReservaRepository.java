package com.uao.bienestarencasa.reservas.repository;

import com.uao.bienestarencasa.reservas.model.EstadoReserva;
import com.uao.bienestarencasa.reservas.model.Reserva;
import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    List<Reserva> findByClienteId(String clienteId);
    List<Reserva> findByProveedorId(String proveedorId);
    List<Reserva> findByEstado(EstadoReserva estado);
    List<Reserva> findByProveedorIdAndEstadoIn(String proveedorId, Collection<EstadoReserva> estados);
}