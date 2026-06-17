package com.natan.coremarket.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.compra.CompraRequestDTO;
import com.natan.coremarket.application.dtos.compra.CompraResponseDTO;
import com.natan.coremarket.application.dtos.itemCompra.ItemCompraResponseDTO;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.domain.entities.Fornecedor;
import com.natan.coremarket.domain.entities.ItemCompra;
import com.natan.coremarket.domain.entities.Produto;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
import com.natan.coremarket.infrastructure.repositories.FornecedorRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final EmpresaRepository empresaRepository;
    private final ProdutoRepository produtoRepository;

    public CompraService(
                CompraRepository compraRepository, 
                FornecedorRepository fornecedorRepository,
                EmpresaRepository empresaRepository, 
                ProdutoRepository produtoRepository) {
        this.compraRepository = compraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.empresaRepository = empresaRepository;
        this.produtoRepository = produtoRepository;
    }

    public CompraResponseDTO salvar(CompraRequestDTO compraRequestDTO) {

        Fornecedor fornecedor = fornecedorRepository.findById(compraRequestDTO.getFornecedorId())
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado"));

        Empresa empresa = empresaRepository.findById(compraRequestDTO.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Compra compra = new Compra();

        if (compraRequestDTO.getFormaPagamento() == FormaPagamento.PRAZO
                && compraRequestDTO.getDataVencimento() == null) {

            throw new IllegalArgumentException(
                    "Data de vencimento é obrigatória para compras a prazo");
        }

        compra.setFornecedor(fornecedor);
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

                    Produto produto = produtoRepository
                            .findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Produto não encontrado"));

                    produto.setQuantidadeEstoque(
                            produto.getQuantidadeEstoque()
                                    + itemDTO.getQuantidade());

                    produtoRepository.save(produto);

                    ItemCompra item = new ItemCompra();

                    item.setProduto(produto);
                    item.setNomeProduto(produto.getNome());
                    item.setQuantidade(itemDTO.getQuantidade());
                    item.setPrecoUnitario(produto.getPreco());

                    BigDecimal subTotal = produto.getPreco()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemDTO.getQuantidade()));

                    item.setSubTotal(subTotal);
                    item.setCompra(compra);

                    return item;

                })
                .collect(Collectors.toList());

        compra.getItens().addAll(novosItens);

        Compra compraSalva = compraRepository.save(compra);

        return new CompraResponseDTO(
                compraSalva.getId(),
                compraSalva.getFornecedor().getNome(),
                compraSalva.getValorTotal(),
                compraSalva.getStatus(),
                compraSalva.getItens().stream()
                        .map(item -> new ItemCompraResponseDTO(
                                item.getId(),
                                item.getNomeProduto(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubTotal()))
                        .collect(Collectors.toList()),
                compraSalva.getValorPago(),
                compraSalva.getSaldoDevedor(),
                compraSalva.getFormaPagamento(),
                compraSalva.getStatusPagamento(),
                compraSalva.getDataVencimento());
        }

    public List<CompraResponseDTO> listarTodos() {
        return compraRepository.findAll().stream()
                .map(compra -> new CompraResponseDTO(
                        compra.getId(),
                        compra.getFornecedor().getNome(),
                        compra.getValorTotal(),
                        compra.getStatus(),
                        compra.getItens().stream()
                                .map(item -> new ItemCompraResponseDTO(
                                        item.getId(),
                                        item.getNomeProduto(),
                                        item.getQuantidade(),
                                        item.getPrecoUnitario(),
                                        item.getSubTotal()))
                                .collect(Collectors.toList()),
                        compra.getValorPago(),
                        compra.getSaldoDevedor(),
                        compra.getFormaPagamento(),
                        compra.getStatusPagamento(),
                        compra.getDataVencimento()))
                .collect(Collectors.toList());
        }

    private CompraResponseDTO converterParaDTO(Compra compra) {

        return new CompraResponseDTO(
                compra.getId(),
                compra.getFornecedor().getNome(),
                compra.getValorTotal(),
                compra.getStatus(),
                compra.getItens().stream()
                        .map(item -> new ItemCompraResponseDTO(
                                item.getId(),
                                item.getNomeProduto(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubTotal()))
                        .collect(Collectors.toList()),
                compra.getValorPago(),
                compra.getSaldoDevedor(),
                compra.getFormaPagamento(),
                compra.getStatusPagamento(),
                compra.getDataVencimento());
        }

    public List<CompraResponseDTO> listarPendentes() {
        return compraRepository.buscarComprasPendentes()
                .stream()
                .map(this::converterParaDTO)
                .toList();
        }

    public Optional<CompraResponseDTO> buscarPorId(Long id) {
        return compraRepository.findById(id)
                .map(compra -> new CompraResponseDTO(
                        compra.getId(),
                        compra.getFornecedor().getNome(),
                        compra.getValorTotal(),
                        compra.getStatus(),
                        compra.getItens().stream()
                                .map(item -> new ItemCompraResponseDTO(
                                        item.getId(),
                                        item.getNomeProduto(),
                                        item.getQuantidade(),
                                        item.getPrecoUnitario(),
                                        item.getSubTotal()))
                                .collect(Collectors.toList()),
                        compra.getValorPago(),
                        compra.getSaldoDevedor(),
                        compra.getFormaPagamento(),
                        compra.getStatusPagamento(),
                        compra.getDataVencimento()));
        }

    public void deletar(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException( "Compra não encontrada"));
                
        if (!compra.getPagamentos().isEmpty()) {
                throw new IllegalArgumentException(
                        "Não é possível excluir uma compra que possui pagamentos.");
        }

        for (ItemCompra item : compra.getItens()) {

        Produto produto = item.getProduto();

        produto.setQuantidadeEstoque(
                produto.getQuantidadeEstoque()
                        - item.getQuantidade());

                produtoRepository.save(produto);
        }

        compraRepository.delete(compra);

        }

    public Optional<CompraResponseDTO> atualizar(Long id, CompraRequestDTO compraRequestDTO) {

        return compraRepository.findById(id)
                .map(compraExistente -> {

                    Fornecedor fornecedor = fornecedorRepository.findById(compraRequestDTO.getFornecedorId())
                                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado"));

                    Empresa empresa = empresaRepository.findById(compraRequestDTO.getEmpresaId())
                                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

                    compraExistente.setFornecedor(fornecedor);
                    compraExistente.setEmpresa(empresa);
                    compraExistente.setStatus(compraRequestDTO.getStatus());

                    for (ItemCompra item : compraExistente.getItens()) {

                        Produto produto = item.getProduto();

                        produto.setQuantidadeEstoque(
                                produto.getQuantidadeEstoque()
                                        - item.getQuantidade());

                        produtoRepository.save(produto);
                }

                    List<ItemCompra> itensAtualizados = compraRequestDTO.getItens().stream()
                            .map(itemDTO -> {

                                Produto produto = produtoRepository.findById(
                                        itemDTO.getProdutoId())
                                        .orElseThrow(() ->
                                                new IllegalArgumentException(
                                                        "Produto não encontrado"));

                                produto.setQuantidadeEstoque(
                                        produto.getQuantidadeEstoque()
                                                + itemDTO.getQuantidade());

                                produtoRepository.save(produto);

                                ItemCompra item = new ItemCompra();

                                item.setProduto(produto);
                                item.setNomeProduto(produto.getNome());
                                item.setQuantidade(itemDTO.getQuantidade());
                                item.setPrecoUnitario(produto.getPreco());

                                BigDecimal subTotal =
                                        produto.getPreco()
                                                .multiply(
                                                        BigDecimal.valueOf(
                                                                itemDTO.getQuantidade()));

                                item.setSubTotal(subTotal);
                                item.setCompra(compraExistente);

                                return item;

                                })

                        .collect(Collectors.toList());

                    compraExistente.getItens().clear();
                    compraExistente.getItens().addAll(itensAtualizados);

                    BigDecimal valorTotal =
                        itensAtualizados.stream()
                                .map(ItemCompra::getSubTotal)
                                .reduce(
                                        BigDecimal.ZERO,
                                        BigDecimal::add);

                        compraExistente.setValorTotal(valorTotal);

                    Compra compraAtualizada = compraRepository.save(compraExistente);

                    return new CompraResponseDTO(
                            compraAtualizada.getId(),
                            compraAtualizada.getFornecedor().getNome(),
                            compraAtualizada.getValorTotal(),
                            compraAtualizada.getStatus(),
                            compraAtualizada.getItens().stream()
                                    .map(item -> new ItemCompraResponseDTO(
                                            item.getId(),
                                            item.getNomeProduto(),
                                            item.getQuantidade(),
                                            item.getPrecoUnitario(),
                                            item.getSubTotal()))
                                    .collect(Collectors.toList()),
                            compraAtualizada.getValorPago(),
                            compraAtualizada.getSaldoDevedor(),
                            compraAtualizada.getFormaPagamento(),
                            compraAtualizada.getStatusPagamento(),
                            compraAtualizada.getDataVencimento());
                });
        }
}