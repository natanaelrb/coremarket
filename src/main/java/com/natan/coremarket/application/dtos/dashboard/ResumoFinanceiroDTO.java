package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumoFinanceiroDTO {
    
    private BigDecimal receitas;
    private BigDecimal despesas;
    private BigDecimal lucro;
    private Double margem;
}
