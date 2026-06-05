package com.natan.coremarket.application.dtos.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.natan.coremarket.application.dtos.itemCompra.ItemCompraResponseDTO;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusCompra;
import com.natan.coremarket.domain.enums.StatusPagamento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompraResponseDTO {

    private Long id;
    private String nomeCliente;
    private BigDecimal valorTotal;
    private StatusCompra status;
    private List<ItemCompraResponseDTO> itens;
    private BigDecimal valorPago;
    private BigDecimal saldoDevedor;
    private FormaPagamento formaPagamento;
    private StatusPagamento statusPagamento;
    private LocalDate dataVencimento;
    
}
