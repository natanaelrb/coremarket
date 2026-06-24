package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.natan.coremarket.domain.enums.StatusPagamento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContaReceberDTO {
    
    private Long vendaId;
    private String cliente;
    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private BigDecimal saldoDevedor;
    private LocalDate dataVencimento;
    private StatusPagamento statusPagamento;

}
