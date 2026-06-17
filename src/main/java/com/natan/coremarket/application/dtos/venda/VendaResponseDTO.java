package com.natan.coremarket.application.dtos.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.natan.coremarket.application.dtos.itemVenda.ItemVendaResponseDTO;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.domain.enums.StatusVenda;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VendaResponseDTO {

    private Long id;
    private String nomeClientes;
    private BigDecimal valorTotal;
    private StatusVenda status;
    private List<ItemVendaResponseDTO> itens;
    private BigDecimal valorPago;
    private BigDecimal saldoDevedor;
    private FormaPagamento formaPagamento;
    private StatusPagamento statusPagamento;
    private LocalDate dataVencimento;

}
