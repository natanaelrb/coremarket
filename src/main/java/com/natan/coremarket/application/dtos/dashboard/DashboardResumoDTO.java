package com.natan.coremarket.application.dtos.dashboard;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResumoDTO {

    private Long totalClientes;
    private Long totalCompras;
    private BigDecimal totalReceber;
    private Long comprasPendentes;
    
}
