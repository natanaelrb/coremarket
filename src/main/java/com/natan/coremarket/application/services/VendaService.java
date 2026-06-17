package com.natan.coremarket.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.venda.VendaRequestDTO;
import com.natan.coremarket.application.dtos.venda.VendaResponseDTO;
import com.natan.coremarket.application.dtos.itemVenda.ItemVendaResponseDTO;
import com.natan.coremarket.domain.entities.Cliente;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.domain.entities.ItemVenda;
import com.natan.coremarket.domain.entities.Produto;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.domain.enums.FormaPagamento;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(
                VendaRepository vendaRepository, 
                ClienteRepository clienteRepository,
                EmpresaRepository empresaRepository, 
                ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.produtoRepository = produtoRepository;
    }

    public VendaResponseDTO salvar(VendaRequestDTO vendaRequestDTO) {

        Cliente cliente = clienteRepository.findById(vendaRequestDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findById(vendaRequestDTO.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Venda venda = new Venda();

        if (vendaRequestDTO.getFormaPagamento() == FormaPagamento.PRAZO
                && vendaRequestDTO.getDataVencimento() == null) {

            throw new IllegalArgumentException(
                    "Data de vencimento é obrigatória para vendas a prazo");
        }

        venda.setCliente(cliente);
        venda.setEmpresa(empresa);
        venda.setStatus(vendaRequestDTO.getStatus());
        venda.setFormaPagamento(vendaRequestDTO.getFormaPagamento());
        venda.setDataVencimento(vendaRequestDTO.getDataVencimento());

        if (vendaRequestDTO.getFormaPagamento() == FormaPagamento.PRAZO) {

            venda.setValorPago(BigDecimal.ZERO);
            venda.setStatusPagamento(StatusPagamento.PENDENTE);

        } else {

            venda.setValorPago(vendaRequestDTO.getValorTotal());
            venda.setStatusPagamento(StatusPagamento.PAGO);

        }

        List<ItemVenda> novosItens = vendaRequestDTO.getItens().stream()
                .map(itemDTO -> {

                    Produto produto = produtoRepository
                            .findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Produto não encontrado"));

                    if (produto.getQuantidadeEstoque() < itemDTO.getQuantidade()) {

                        throw new IllegalArgumentException(
                                "Estoque insuficiente para "
                                        + produto.getNome());
                        }

                    produto.setQuantidadeEstoque(
                            produto.getQuantidadeEstoque()
                                    - itemDTO.getQuantidade());

                    produtoRepository.save(produto);

                    ItemVenda item = new ItemVenda();

                    item.setProduto(produto);
                    item.setNomeProduto(produto.getNome());
                    item.setQuantidade(itemDTO.getQuantidade());
                    item.setPrecoUnitario(produto.getPreco());

                    BigDecimal subTotal = produto.getPreco()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemDTO.getQuantidade()));

                    item.setSubTotal(subTotal);
                    item.setVenda(venda);

                    return item;

                })
                .collect(Collectors.toList());

        venda.getItens().addAll(novosItens);

        BigDecimal valorTotal = novosItens.stream()
                .map(ItemVenda::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setValorTotal(valorTotal);

        Venda vendaSalva = vendaRepository.save(venda);

        return new VendaResponseDTO(
                vendaSalva.getId(),
                vendaSalva.getCliente().getNome(),
                vendaSalva.getValorTotal(),
                vendaSalva.getStatus(),
                vendaSalva.getItens().stream()
                        .map(item -> new ItemVendaResponseDTO(
                                item.getId(),
                                item.getNomeProduto(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubTotal()))
                        .collect(Collectors.toList()),
                vendaSalva.getValorPago(),
                vendaSalva.getSaldoDevedor(),
                vendaSalva.getFormaPagamento(),
                vendaSalva.getStatusPagamento(),
                vendaSalva.getDataVencimento());
        }

    public List<VendaResponseDTO> listarTodos() {
        return vendaRepository.findAll().stream()
                .map(venda -> new VendaResponseDTO(
                        venda.getId(),
                        venda.getCliente().getNome(),
                        venda.getValorTotal(),
                        venda.getStatus(),
                        venda.getItens().stream()
                                .map(item -> new ItemVendaResponseDTO(
                                        item.getId(),
                                        item.getNomeProduto(),
                                        item.getQuantidade(),
                                        item.getPrecoUnitario(),
                                        item.getSubTotal()))
                                .collect(Collectors.toList()),
                        venda.getValorPago(),
                        venda.getSaldoDevedor(),
                        venda.getFormaPagamento(),
                        venda.getStatusPagamento(),
                        venda.getDataVencimento()))
                .collect(Collectors.toList());
        }

    private VendaResponseDTO converterParaDTO(Venda venda) {

        return new VendaResponseDTO(
                venda.getId(),
                venda.getCliente().getNome(),
                venda.getValorTotal(),
                venda.getStatus(),
                venda.getItens().stream()
                        .map(item -> new ItemVendaResponseDTO(
                                item.getId(),
                                item.getNomeProduto(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubTotal()))
                        .collect(Collectors.toList()),
                venda.getValorPago(),
                venda.getSaldoDevedor(),
                venda.getFormaPagamento(),
                venda.getStatusPagamento(),
                venda.getDataVencimento());
        }

    public List<VendaResponseDTO> listarPendentes() {
        return vendaRepository.buscarVendasPendentes()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        }

    public Optional<VendaResponseDTO> buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .map(venda -> new VendaResponseDTO(
                        venda.getId(),
                        venda.getCliente().getNome(),
                        venda.getValorTotal(),
                        venda.getStatus(),
                        venda.getItens().stream()
                                .map(item -> new ItemVendaResponseDTO(
                                        item.getId(),
                                        item.getNomeProduto(),
                                        item.getQuantidade(),
                                        item.getPrecoUnitario(),
                                        item.getSubTotal()))
                                .collect(Collectors.toList()),
                        venda.getValorPago(),
                        venda.getSaldoDevedor(),
                        venda.getFormaPagamento(),
                        venda.getStatusPagamento(),
                        venda.getDataVencimento()));
        }

    public void deletar(Long id) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
                
        if (!venda.getPagamentos().isEmpty()) {
                throw new IllegalArgumentException(
                        "Não é possível excluir uma venda que possui pagamentos.");
        }

        for (ItemVenda item : venda.getItens()) {

            Produto produto = item.getProduto();

            produto.setQuantidadeEstoque(
                    produto.getQuantidadeEstoque()
                            + item.getQuantidade());

            produtoRepository.save(produto);
        }

        vendaRepository.delete(venda);
    }

    public Optional<VendaResponseDTO> atualizar(Long id, VendaRequestDTO vendaRequestDTO) {

        return vendaRepository.findById(id)
                .map(vendaExistente -> {

                    for (ItemVenda item : vendaExistente.getItens()) {
                        Produto produto = item.getProduto();

                        produto.setQuantidadeEstoque(
                                produto.getQuantidadeEstoque()
                                        + item.getQuantidade());

                        produtoRepository.save(produto);
                }

                    Cliente cliente = clienteRepository.findById(vendaRequestDTO.getClienteId())
                            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

                    Empresa empresa = empresaRepository.findById(vendaRequestDTO.getEmpresaId())
                            .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

                    vendaExistente.setCliente(cliente);
                    vendaExistente.setEmpresa(empresa);
                    vendaExistente.setStatus(vendaRequestDTO.getStatus());

                    vendaExistente.setFormaPagamento(
                        vendaRequestDTO.getFormaPagamento());

                    vendaExistente.setDataVencimento(
                        vendaRequestDTO.getDataVencimento());

                    List<ItemVenda> itensAtualizados = vendaRequestDTO.getItens().stream()
                            .map(itemDTO -> {

                                Produto produto = produtoRepository.findById(
                                        itemDTO.getProdutoId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "Produto não encontrado"));

                                if (produto.getQuantidadeEstoque() < itemDTO.getQuantidade()) {

                                    throw new IllegalArgumentException(
                                            "Estoque insuficiente para "
                                                    + produto.getNome());
                                }

                                produto.setQuantidadeEstoque(
                                        produto.getQuantidadeEstoque()
                                                - itemDTO.getQuantidade());

                                produtoRepository.save(produto);

                                ItemVenda item = new ItemVenda();

                                item.setProduto(produto);
                                item.setNomeProduto(produto.getNome());
                                item.setQuantidade(itemDTO.getQuantidade());
                                item.setPrecoUnitario(produto.getPreco());

                                BigDecimal subTotal = produto.getPreco()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        itemDTO.getQuantidade()));

                                item.setSubTotal(subTotal);
                                item.setVenda(vendaExistente);

                                return item;

                                })
                        .toList();

                    vendaExistente.getItens().clear();
                    vendaExistente.getItens().addAll(itensAtualizados);

                    BigDecimal valorTotal = itensAtualizados.stream()
                        .map(ItemVenda::getSubTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                    vendaExistente.setValorTotal(valorTotal);

                    if (vendaExistente.getFormaPagamento()
                                == FormaPagamento.PRAZO) {

                    if (vendaExistente.getValorPago()
                                .compareTo(BigDecimal.ZERO) == 0) {

                        vendaExistente.setStatusPagamento(
                                StatusPagamento.PENDENTE);

                } else if (vendaExistente.getValorPago()
                                .compareTo(valorTotal) < 0) {

                        vendaExistente.setStatusPagamento(
                                StatusPagamento.PARCIAL);

                } else {
                        vendaExistente.setStatusPagamento(
                                StatusPagamento.PAGO);
                }

                } else {

                        vendaExistente.setValorPago(valorTotal);
                        vendaExistente.setStatusPagamento(
                                StatusPagamento.PAGO);
                        }

                    Venda vendaAtualizada = vendaRepository.save(vendaExistente);

                    return new VendaResponseDTO(
                            vendaAtualizada.getId(),
                            vendaAtualizada.getCliente().getNome(),
                            vendaAtualizada.getValorTotal(),
                            vendaAtualizada.getStatus(),
                            vendaAtualizada.getItens().stream()
                                    .map(item -> new ItemVendaResponseDTO(
                                            item.getId(),
                                            item.getNomeProduto(),
                                            item.getQuantidade(),
                                            item.getPrecoUnitario(),
                                            item.getSubTotal()))
                                    .collect(Collectors.toList()),
                            vendaAtualizada.getValorPago(),
                            vendaAtualizada.getSaldoDevedor(),
                            vendaAtualizada.getFormaPagamento(),
                            vendaAtualizada.getStatusPagamento(),
                            vendaAtualizada.getDataVencimento());
                });
        }
}
