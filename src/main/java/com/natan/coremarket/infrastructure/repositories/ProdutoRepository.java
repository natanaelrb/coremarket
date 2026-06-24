package com.natan.coremarket.infrastructure.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.coremarket.domain.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    List<Produto> findByQuantidadeEstoqueLessThanEqual(Integer quantidade);

    long countByUltimaVendaIsNull();
    long countByUltimaVendaBefore(LocalDateTime data);
}
