package com.natan.coremarket.application.dtos.itemCompra;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCompraRequestDTO {

    @NotNull(message = "O produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "A quantidade do item é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

}
