package com.natan.coremarket.application.dtos.pagamento;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagamentoResponseDTO {
    
    private Long id;
    private BigDecimal valorPago;
    private String observacao;

}
