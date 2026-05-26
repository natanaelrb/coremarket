package com.natan.coremarket.application.dtos.pagamento;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoRequestDTO {
    
    private BigDecimal valorPago;
    private String observacao;
    private Long clienteId;
    private Long empresaId;
    
}
