package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FaturamentoMensalDTO {

    private String mes;
    private BigDecimal vendas;
    private BigDecimal compras;
    private BigDecimal lucro;
    
}
