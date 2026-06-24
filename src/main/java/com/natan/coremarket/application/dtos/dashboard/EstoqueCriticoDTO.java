package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EstoqueCriticoDTO {
    
    private String nomeProduto;
    private Integer quantidadeEstoque;
}
