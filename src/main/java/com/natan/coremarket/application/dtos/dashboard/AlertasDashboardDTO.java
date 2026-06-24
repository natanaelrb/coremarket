package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertasDashboardDTO {

    private long produtosSemEstoque;
    private long comprasPendentes;
    private long contasAtrasadas;
    private long produtosSemVenda;
    
}
