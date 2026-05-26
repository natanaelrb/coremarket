package com.natan.coremarket.application.dtos.itemCompra;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCompraRequestDTO {

    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    
}
