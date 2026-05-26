package com.natan.coremarket.application.dtos.compra;

import java.math.BigDecimal;
import java.util.List;

import com.natan.coremarket.application.dtos.itemCompra.ItemCompraRequestDTO;
import com.natan.coremarket.domain.enums.StatusCompra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraRequestDTO {
    
    private Long clienteId;
    private Long empresaId;
    private BigDecimal valorTotal;
    private StatusCompra status;
    private List<ItemCompraRequestDTO> itens;
}
