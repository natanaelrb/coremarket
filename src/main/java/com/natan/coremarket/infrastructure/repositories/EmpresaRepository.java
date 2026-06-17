package com.natan.coremarket.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.coremarket.domain.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);
}
