package com.natan.coremarket.application.dtos.compra;

import java.math.BigDecimal;
import java.util.List;

import com.natan.coremarket.application.dtos.itemCompra.ItemCompraResponseDTO;
import com.natan.coremarket.domain.enums.StatusCompra;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompraResponseDTO {

    private Long id;
    private BigDecimal valorTotal;
    private StatusCompra status;
    private List<ItemCompraResponseDTO> itens;
    
}
