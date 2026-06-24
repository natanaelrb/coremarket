package com.natan.coremarket.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.natan.coremarket.application.dtos.dashboard.ProdutoMaisVendidoDTO;
import com.natan.coremarket.domain.entities.ItemVenda;


public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
    @Query("""
    SELECT new com.natan.coremarket.application.dtos.dashboard.ProdutoMaisVendidoDTO(
        p.nome,
        SUM(iv.quantidade)
    )
    FROM ItemVenda iv
    JOIN iv.produto p
    GROUP BY p.nome
    ORDER BY SUM(iv.quantidade) DESC
""")
List<ProdutoMaisVendidoDTO> buscarProdutosMaisVendidos();

}
