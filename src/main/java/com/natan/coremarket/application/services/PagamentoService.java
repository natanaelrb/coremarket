package com.natan.coremarket.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.pagamento.PagamentoRequestDTO;
import com.natan.coremarket.application.dtos.pagamento.PagamentoResponseDTO;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.domain.entities.Pagamento;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
import com.natan.coremarket.infrastructure.repositories.PagamentoRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

import jakarta.transaction.Transactional;

@Service
public class PagamentoService {

        private final PagamentoRepository pagamentoRepository;
        private final EmpresaRepository empresaRepository;
        private final CompraRepository compraRepository;
        private final VendaRepository vendaRepository;

        public PagamentoService(
                        PagamentoRepository pagamentoRepository, 
                        EmpresaRepository empresaRepository,
                        CompraRepository compraRepository, 
                        VendaRepository vendaRepository) {
                this.pagamentoRepository = pagamentoRepository;
                this.empresaRepository = empresaRepository;
                this.compraRepository = compraRepository;
                this.vendaRepository = vendaRepository;
        }

        @Transactional
        public PagamentoResponseDTO salvar(PagamentoRequestDTO dto) {

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException( "Empresa não encontrada"));

        validarOrigemPagamento(dto);

        Pagamento pagamento = new Pagamento();
        pagamento.setEmpresa(empresa);
        pagamento.setValorPago(dto.getValorPago());
        pagamento.setObservacao(dto.getObservacao());

        if (dto.getCompraId() != null) {
            processarPagamentoCompra(
                    pagamento,
                    dto.getCompraId());
                }

        if (dto.getVendaId() != null) {
            processarPagamentoVenda(
                    pagamento,
                    dto.getVendaId());
                }

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        return new PagamentoResponseDTO(
                pagamentoSalvo.getId(),
                pagamentoSalvo.getValorPago(),
                pagamentoSalvo.getObservacao());
        }

    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoRepository.findAll()
                .stream()
                .map(p -> new PagamentoResponseDTO(
                        p.getId(),
                        p.getValorPago(),
                        p.getObservacao()))
                .toList();
        }

    public Optional<PagamentoResponseDTO> buscarPorId(Long id) {

        return pagamentoRepository.findById(id)
                .map(p -> new PagamentoResponseDTO(
                        p.getId(),
                        p.getValorPago(),
                        p.getObservacao()));
        }

    @Transactional
    public void deletar(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));

        if (pagamento.getCompra() != null) {

            Compra compra = pagamento.getCompra();

            BigDecimal novoValorPago =
                    compra.getValorPago()
                            .subtract(
                                    pagamento.getValorPago());

            compra.setValorPago(novoValorPago);

            atualizarStatusCompra(compra);

            compraRepository.save(compra);
        }

        if (pagamento.getVenda() != null) {

            Venda venda = pagamento.getVenda();

            BigDecimal novoValorPago =
                    venda.getValorPago()
                            .subtract(
                                    pagamento.getValorPago());

            venda.setValorPago(novoValorPago);

            atualizarStatusVenda(venda);

            vendaRepository.save(venda);
        }

        pagamentoRepository.delete(pagamento);

        }

    @Transactional
    public Optional<PagamentoResponseDTO> atualizar(Long id, PagamentoRequestDTO dto) {

        return pagamentoRepository.findById(id)
                .map(pagamento -> {

                    if (pagamento.getCompra() != null) {

                        Compra compra = pagamento.getCompra();

                        compra.setValorPago(
                                compra.getValorPago()
                                        .subtract(
                                                pagamento.getValorPago()));

                        atualizarStatusCompra(compra);
                        compraRepository.save(compra);
                }

                    if (pagamento.getVenda() != null) {

                        Venda venda = pagamento.getVenda();

                        venda.setValorPago(
                                venda.getValorPago()
                                        .subtract(
                                                pagamento.getValorPago()));

                        atualizarStatusVenda(venda);
                        vendaRepository.save(venda);
                }
                    pagamento.setValorPago(dto.getValorPago());

                    pagamento.setObservacao(dto.getObservacao());

                    pagamento.setCompra(null);
                    pagamento.setFornecedor(null);
                    pagamento.setVenda(null);
                    pagamento.setCliente(null);

                    validarOrigemPagamento(dto);

                    if (dto.getCompraId() != null) {
                        processarPagamentoCompra(
                                pagamento,
                                dto.getCompraId());
                        }

                    if (dto.getVendaId() != null) {
                        processarPagamentoVenda(
                                pagamento,
                                dto.getVendaId());
                        }

                    Pagamento atualizado = pagamentoRepository.save(pagamento);

                    return new PagamentoResponseDTO(
                            atualizado.getId(),
                            atualizado.getValorPago(),
                            atualizado.getObservacao());
                });
        }

    private void validarOrigemPagamento(PagamentoRequestDTO dto) {

        if (dto.getCompraId() == null
                && dto.getVendaId() == null) {

            throw new IllegalArgumentException(
                    "Informe uma compra ou uma venda.");
                }

        if (dto.getCompraId() != null
                && dto.getVendaId() != null) {

            throw new IllegalArgumentException(
                    "O pagamento deve pertencer apenas a uma compra ou uma venda.");
                }
        }

    private void processarPagamentoCompra(
            Pagamento pagamento,
            Long compraId) {

        Compra compra =
                compraRepository.findById(compraId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Compra não encontrada"));

        BigDecimal novoValorPago =
                compra.getValorPago()
                        .add(
                                pagamento.getValorPago());

        if (novoValorPago.compareTo(
                compra.getValorTotal()) > 0) {

            throw new IllegalArgumentException(
                    "Pagamento maior que o saldo devedor.");
                }

        compra.setValorPago(novoValorPago);

        atualizarStatusCompra(compra);

        compraRepository.save(compra);

        pagamento.setCompra(compra);
        pagamento.setFornecedor(
                compra.getFornecedor());
        }

    private void processarPagamentoVenda(
            Pagamento pagamento,
            Long vendaId) {

        Venda venda =
                vendaRepository.findById(vendaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Venda não encontrada"));

        BigDecimal novoValorPago =
                venda.getValorPago()
                        .add(
                                pagamento.getValorPago());

        if (novoValorPago.compareTo(
                venda.getValorTotal()) > 0) {

            throw new IllegalArgumentException(
                    "Pagamento maior que o saldo devedor.");
                }

        venda.setValorPago(novoValorPago);

        atualizarStatusVenda(venda);

        vendaRepository.save(venda);

        pagamento.setVenda(venda);
        pagamento.setCliente(
                venda.getCliente());
        }

    private void atualizarStatusCompra(
            Compra compra) {

        if (compra.getValorPago()
                .compareTo(BigDecimal.ZERO) == 0) {

            compra.setStatusPagamento(
                    StatusPagamento.PENDENTE);

        } else if (compra.getValorPago()
                .compareTo(
                        compra.getValorTotal()) >= 0) {

            compra.setStatusPagamento(
                    StatusPagamento.PAGO);

        } else {

            compra.setStatusPagamento(
                    StatusPagamento.PARCIAL);
                }
        }

    private void atualizarStatusVenda(
            Venda venda) {

        if (venda.getValorPago()
                .compareTo(BigDecimal.ZERO) == 0) {

            venda.setStatusPagamento(
                    StatusPagamento.PENDENTE);

        } else if (venda.getValorPago()
                .compareTo(
                        venda.getValorTotal()) >= 0) {

            venda.setStatusPagamento(
                    StatusPagamento.PAGO);

        } else {

            venda.setStatusPagamento(
                    StatusPagamento.PARCIAL);
                }
        }
}