package com.natan.coremarket.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.compra.CompraRequestDTO;
import com.natan.coremarket.application.dtos.compra.CompraResponseDTO;
import com.natan.coremarket.application.dtos.itemCompra.ItemCompraResponseDTO;
import com.natan.coremarket.domain.entities.Cliente;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.domain.entities.ItemCompra;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;

    public CompraService(CompraRepository compraRepository, ClienteRepository clienteRepository, EmpresaRepository empresaRepository) {
        this.compraRepository = compraRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
    }

    public CompraResponseDTO salvar(CompraRequestDTO compraRequestDTO) {

        Cliente cliente = clienteRepository.findById(compraRequestDTO.getClienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findById(compraRequestDTO.getEmpresaId())
            .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Compra compra = new Compra();

        if (compraRequestDTO.getFormaPagamento() == FormaPagamento.PRAZO
                && compraRequestDTO.getDataVencimento() == null) {

            throw new IllegalArgumentException(
                    "Data de vencimento é obrigatória para compras a prazo");
        }

        compra.setCliente(cliente);
        compra.setEmpresa(empresa);
        compra.setValorTotal(compraRequestDTO.getValorTotal());
        compra.setStatus(compraRequestDTO.getStatus());
        compra.setFormaPagamento(compraRequestDTO.getFormaPagamento());
        compra.setDataVencimento(compraRequestDTO.getDataVencimento());

        if (compraRequestDTO.getFormaPagamento() == FormaPagamento.PRAZO) {

            compra.setValorPago(BigDecimal.ZERO);
            compra.setStatusPagamento(StatusPagamento.PENDENTE);

        } else {

            compra.setValorPago(compraRequestDTO.getValorTotal());
            compra.setStatusPagamento(StatusPagamento.PAGO);

        }

        List<ItemCompra> novosItens = compraRequestDTO.getItens().stream()
            .map(itemDTO -> {

                ItemCompra item = new ItemCompra();

                item.setNomeProduto(itemDTO.getNomeProduto());
                item.setQuantidade(itemDTO.getQuantidade());
                item.setPrecoUnitario(itemDTO.getPrecoUnitario());

                BigDecimal subTotal = itemDTO.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

                item.setSubTotal(subTotal);

                item.setCompra(compra);

                return item;

            })
            .collect(Collectors.toList());

        compra.getItens().addAll(novosItens);

        Compra compraSalva = compraRepository.save(compra);

        return new CompraResponseDTO(
            compraSalva.getId(),
            compraSalva.getValorTotal(),
            compraSalva.getStatus(),
            compraSalva.getItens().stream()
                .map(item -> new ItemCompraResponseDTO(
                    item.getId(),
                    item.getNomeProduto(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    item.getSubTotal()
                ))
                .collect(Collectors.toList()),
            compraSalva.getValorPago(),
            compraSalva.getSaldoDevedor(),
            compraSalva.getFormaPagamento(),
            compraSalva.getStatusPagamento(),
            compraSalva.getDataVencimento()
        );
    }
    

    public List<CompraResponseDTO> listarTodos() {
        return compraRepository.findAll().stream()
            .map(compra -> new CompraResponseDTO(
                compra.getId(),
                compra.getValorTotal(),
                compra.getStatus(),
                compra.getItens().stream()
                    .map(item -> new ItemCompraResponseDTO(
                        item.getId(),
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubTotal()
                    ))
                    .collect(Collectors.toList()),
                compra.getValorPago(),
                compra.getSaldoDevedor(),
                compra.getFormaPagamento(),
                compra.getStatusPagamento(),
                compra.getDataVencimento()
            ))
            .collect(Collectors.toList());
    }

    public Optional<CompraResponseDTO> buscarPorId(Long id) {
        return compraRepository.findById(id)
            .map(compra -> new CompraResponseDTO(
                compra.getId(),
                compra.getValorTotal(),
                compra.getStatus(),
                compra.getItens().stream()
                    .map(item -> new ItemCompraResponseDTO(
                        item.getId(),
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubTotal()
                    ))
                    .collect(Collectors.toList()),
                compra.getValorPago(),
                compra.getSaldoDevedor(),
                compra.getFormaPagamento(),
                compra.getStatusPagamento(),
                compra.getDataVencimento()
            ));
    }

    public void deletar(Long id) {
        compraRepository.deleteById(id);
    }

    public Optional<CompraResponseDTO> atualizar(Long id, CompraRequestDTO compraRequestDTO) {

        return compraRepository.findById(id)
            .map(compraExistente -> {

                Cliente cliente = clienteRepository.findById(compraRequestDTO.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

                Empresa empresa = empresaRepository.findById(compraRequestDTO.getEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

                compraExistente.setCliente(cliente);
                compraExistente.setEmpresa(empresa);
                compraExistente.setValorTotal(compraRequestDTO.getValorTotal());
                compraExistente.setStatus(compraRequestDTO.getStatus());

                List<ItemCompra> itensAtualizados = compraRequestDTO.getItens().stream()
                    .map(itemDTO -> {

                        ItemCompra item = new ItemCompra();

                        item.setNomeProduto(itemDTO.getNomeProduto());
                        item.setQuantidade(itemDTO.getQuantidade());
                        item.setPrecoUnitario(itemDTO.getPrecoUnitario());

                        BigDecimal subTotal = itemDTO.getPrecoUnitario()
                            .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

                        item.setSubTotal(subTotal);

                        item.setCompra(compraExistente);

                        return item;
                    })
                    .collect(Collectors.toList());

                compraExistente.getItens().clear();
                compraExistente.getItens().addAll(itensAtualizados);

                Compra compraAtualizada = compraRepository.save(compraExistente);

                return new CompraResponseDTO(
                    compraAtualizada.getId(),
                    compraAtualizada.getValorTotal(),
                    compraAtualizada.getStatus(),
                    compraAtualizada.getItens().stream()
                        .map(item -> new ItemCompraResponseDTO(
                            item.getId(),
                            item.getNomeProduto(),
                            item.getQuantidade(),
                            item.getPrecoUnitario(),
                            item.getSubTotal()
                        ))
                        .collect(Collectors.toList()),
                    compraAtualizada.getValorPago(),
                    compraAtualizada.getSaldoDevedor(),
                    compraAtualizada.getFormaPagamento(),
                    compraAtualizada.getStatusPagamento(),
                    compraAtualizada.getDataVencimento()
                );
            });
    }
}