package com.natan.coremarket.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natan.coremarket.domain.entities.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByClienteId(Long clienteId);
    
    
}
