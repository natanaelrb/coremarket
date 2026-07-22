package com.natan.coremarket.application.services.dashboard;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.EstoqueCriticoDTO;
import com.natan.coremarket.application.dtos.dashboard.ProdutoMaisVendidoDTO;
import com.natan.coremarket.infrastructure.repositories.ItemVendaRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final ItemVendaRepository itemVendaRepository;

public List<ProdutoMaisVendidoDTO> buscarProdutosMaisVendidos() {
    return itemVendaRepository.buscarProdutosMaisVendidos();
    
    }

public List<EstoqueCriticoDTO> buscarEstoqueCritico() {

    return produtoRepository.findAll()
            .stream()
            .filter(p -> p.getQuantidadeEstoque() <= 10)
            .map(p -> new EstoqueCriticoDTO(
                    p.getNome(),
                    p.getQuantidadeEstoque()))
            .toList();
    }
}
