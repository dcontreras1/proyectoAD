package com.uao.bienestarencasa.reservas.repository;

import com.uao.bienestarencasa.reservas.model.Servicio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServicioRepository extends MongoRepository<Servicio, String> {
}