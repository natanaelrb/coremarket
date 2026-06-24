package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HeatmapVendaDTO {
    
    private String dia;
    private String hora;
    private long quantidade;
}
