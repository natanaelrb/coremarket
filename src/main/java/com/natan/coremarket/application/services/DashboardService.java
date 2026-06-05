package com.natan.coremarket.application.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.DashboardResumoDTO;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final CompraRepository compraRepository;

    public DashboardService(
        ClienteRepository clienteRepository,
        CompraRepository compraRepository
    ) {
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
    }

    public DashboardResumoDTO buscarResumo() {

    Long totalClientes = clienteRepository.count();

    Long totalCompras = compraRepository.count();

    return new DashboardResumoDTO(
        totalClientes,
        totalCompras,
        BigDecimal.ZERO,
        0L
    );
}
}