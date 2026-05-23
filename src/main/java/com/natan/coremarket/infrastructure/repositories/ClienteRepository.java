package com.natan.coremarket.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natan.coremarket.domain.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEmpresaId(Long empresaId);
    
}
