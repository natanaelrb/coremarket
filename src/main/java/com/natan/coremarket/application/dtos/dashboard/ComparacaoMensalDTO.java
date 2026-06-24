package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComparacaoMensalDTO {
    
    private double percentualVendas;
    private double percentualCompras;
    private double percentualLucro;
}
