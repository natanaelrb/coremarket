package com.natan.coremarket.application.dtos.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String nome;

    @NotBlank(message = "O telefone do cliente é obrigatório")
    private String telefone;

    @NotBlank(message = "A observação do cliente é obrigatória")
    private String observacao;

    @NotNull(message = "O status do cliente é obrigatório")
    private Boolean ativo;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;
}