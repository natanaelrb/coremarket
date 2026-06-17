package com.natan.coremarket.application.dtos.pagamento;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoRequestDTO {

    private Long compraId;

    private Long vendaId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;

    @NotNull(message = "O valor pago é obrigatório")
    private BigDecimal valorPago;

    @NotBlank(message = "A observação do pagamento é obrigatória")
    private String observacao;

}
