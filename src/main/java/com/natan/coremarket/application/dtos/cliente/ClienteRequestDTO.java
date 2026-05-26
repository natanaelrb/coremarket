package com.natan.coremarket.application.dtos.cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {
    
    private String nome;
    private String telefone;
    private String observacao;
    private Boolean ativo;
    private Long empresaId;
}
