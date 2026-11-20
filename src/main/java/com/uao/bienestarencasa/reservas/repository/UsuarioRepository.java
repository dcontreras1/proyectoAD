package com.uao.bienestarencasa.reservas.repository;

import com.uao.bienestarencasa.reservas.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}