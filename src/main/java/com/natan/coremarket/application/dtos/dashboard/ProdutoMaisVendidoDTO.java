package com.natan.coremarket.application.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProdutoMaisVendidoDTO {

    private String nomeProduto;
    private Long quantidadeVendida;
    
}
