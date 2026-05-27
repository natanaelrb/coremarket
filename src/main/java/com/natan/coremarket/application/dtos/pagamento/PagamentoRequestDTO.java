package com.natan.coremarket.application.dtos.pagamento;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoRequestDTO {
    
    @NotNull(message = "O valor pago é obrigatório")
    private BigDecimal valorPago;

    @NotBlank(message = "A observação do pagamento é obrigatória")
    private String observacao;

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;
    
}
