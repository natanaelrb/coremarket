package com.natan.coremarket.application.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.DashboardResumoDTO;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final CompraRepository compraRepository;
    private final ProdutoRepository produtoRepository;

    public DashboardService(
            ClienteRepository clienteRepository,
            CompraRepository compraRepository,
            ProdutoRepository produtoRepository) {
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
        this.produtoRepository = produtoRepository;
    }

    public DashboardResumoDTO buscarResumo() {

        Long totalClientes = clienteRepository.count();

        Long totalCompras = compraRepository.count();

        BigDecimal totalReceber = compraRepository.findAll()
                .stream()
                .map(compra -> compra.getSaldoDevedor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long comprasPendentes = compraRepository.findAll()
                .stream()
                .filter(compra -> compra.getSaldoDevedor().compareTo(BigDecimal.ZERO) > 0)
                .count();

        return new DashboardResumoDTO(
                totalClientes,
                totalCompras,
                totalReceber,
                comprasPendentes);
    }
}