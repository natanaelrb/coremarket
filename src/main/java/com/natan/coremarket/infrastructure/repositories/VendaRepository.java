package com.natan.coremarket.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.natan.coremarket.domain.entities.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("""
        SELECT c
        FROM Venda c
        WHERE c.valorPago < c.valorTotal
    """)
    List<Venda> buscarVendasPendentes();
    
}
