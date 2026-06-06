package com.natan.coremarket.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.coremarket.domain.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}
