package com.natan.coremarket.application.dtos.compra;

import java.math.BigDecimal;
import java.util.List;

import com.natan.coremarket.application.dtos.itemCompra.ItemCompraRequestDTO;
import com.natan.coremarket.domain.enums.StatusCompra;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraRequestDTO {
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;

    @NotNull(message = "O valor total da compra é obrigatório")
    private BigDecimal valorTotal;

    @NotNull(message = "O status da compra é obrigatório")
    private StatusCompra status;
    
    @Valid
    @NotEmpty(message = "A compra deve ter pelo menos um item")
    private List<ItemCompraRequestDTO> itens;
}
