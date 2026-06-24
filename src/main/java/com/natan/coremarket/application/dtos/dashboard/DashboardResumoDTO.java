package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class DashboardResumoDTO {

    private Long totalClientes;
    private Long totalProdutos;

    private BigDecimal totalCompras;
    private BigDecimal totalVendas;
    private BigDecimal lucroLiquido;
    private BigDecimal contaReceber;

    private Long produtosBaixoEstoque;
    private Long comprasPendentes;
    private Long vendasPendentes;
    
    public DashboardResumoDTO(
        Long totalClientes,
        Long totalProdutos,
        BigDecimal totalCompras,
        BigDecimal totalVendas,
        BigDecimal lucroLiquido,
        BigDecimal contaReceber,
        Long produtosBaixoEstoque,
        Long comprasPendentes,
        Long vendasPendentes) {

    this.totalClientes = totalClientes;
    this.totalProdutos = totalProdutos;
    this.totalCompras = totalCompras;
    this.totalVendas = totalVendas;
    this.lucroLiquido = lucroLiquido;
    this.produtosBaixoEstoque = produtosBaixoEstoque;
    this.comprasPendentes = comprasPendentes;
    this.vendasPendentes = vendasPendentes;
    this.contaReceber =contaReceber;
}
}