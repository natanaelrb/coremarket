package com.natan.coremarket.application.services.dashboard;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.DashboardResumoDTO;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardResumoService {
    
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;
    private final VendaRepository vendaRepository;

    public DashboardResumoDTO buscarResumo() {

        long totalClientes = clienteRepository.count();

        long totalProdutos = produtoRepository.count();

        BigDecimal totalCompras = compraRepository.findAll()
                .stream()
                .map(compra -> compra.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVendas = vendaRepository.findAll()
                .stream()
                .map(venda -> venda.getValorTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lucroLiquido =
                totalVendas.subtract(totalCompras);

        BigDecimal contaReceber = vendaRepository.findAll()
                .stream()
                .map(venda -> venda.getSaldoDevedor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long produtosBaixoEstoque = produtoRepository.findAll()
                .stream()
                .filter(produto -> produto.getQuantidadeEstoque() <= 10)
                .count();

        long comprasPendentes = compraRepository.findAll()
                .stream()
                .filter(compra -> compra.getSaldoDevedor().compareTo(BigDecimal.ZERO) > 0)
                .count();

        long vendasPendentes = vendaRepository.findAll()
                .stream()
                .filter(venda -> venda.getSaldoDevedor().compareTo(BigDecimal.ZERO) > 0)
                .count();

        return new DashboardResumoDTO(
                totalClientes,
                totalProdutos,
                totalCompras,
                totalVendas,
                lucroLiquido,
                contaReceber,
                produtosBaixoEstoque,
                comprasPendentes,
                vendasPendentes
        );
    }
}
