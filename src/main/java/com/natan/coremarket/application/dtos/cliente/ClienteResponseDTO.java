package com.natan.coremarket.application.dtos.cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClienteResponseDTO {
    
    private Long id;
    private String nome;
    private String telefone;
    private Boolean ativo;

}
