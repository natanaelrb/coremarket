package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MetaMensalDTO {
    
    private BigDecimal meta;
    private BigDecimal faturamentoAtual;
    private double percentual;
}
