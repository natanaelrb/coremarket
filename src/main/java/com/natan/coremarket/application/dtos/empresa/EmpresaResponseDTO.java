package com.natan.coremarket.application.dtos.empresa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmpresaResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    
}
