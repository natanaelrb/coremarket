package com.natan.coremarket.application.dtos.empresa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDTO {

    private String nome;
    private String email;
    private String telefone;
    private String cnpj;
    
}
