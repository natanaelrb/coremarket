package com.natan.coremarket.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.coremarket.domain.entities.ItemCompra;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {
    
}
