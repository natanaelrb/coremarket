package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusPagamentoDTO {
    
    private long pagos;
    private long pendentes;
    private long parciais;
    private long atrasados;
}
