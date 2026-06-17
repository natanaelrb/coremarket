package com.natan.coremarket.application.dtos.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.natan.coremarket.application.dtos.itemVenda.ItemVendaRequestDTO;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusVenda;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID da empresa é obrigatório")
    private Long empresaId;

    @NotNull(message = "O valor total da compra é obrigatório")
    private BigDecimal valorTotal;

    @NotNull(message = "O status da compra é obrigatório")
    private StatusVenda status;

    @Valid
    @NotEmpty(message = "A compra deve ter pelo menos um item")
    private List<ItemVendaRequestDTO> itens;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private LocalDate dataVencimento;
}
