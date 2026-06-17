package com.natan.coremarket.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.coremarket.domain.entities.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

}