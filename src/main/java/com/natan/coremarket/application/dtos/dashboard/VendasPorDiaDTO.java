package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VendasPorDiaDTO {
    
    private String dia;
    private BigDecimal total;

}
