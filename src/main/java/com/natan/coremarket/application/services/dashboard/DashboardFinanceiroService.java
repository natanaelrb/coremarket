package com.natan.coremarket.application.services.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.LucroMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.MetaMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.ResumoFinanceiroDTO;
import com.natan.coremarket.application.dtos.dashboard.StatusPagamentoDTO;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardFinanceiroService {
    
    private final VendaRepository vendaRepository;
    private final CompraRepository compraRepository;

public StatusPagamentoDTO buscarStatusPagamentos() {

    List<Venda> vendas = vendaRepository.findAll();

    long pagos = vendas.stream()
            .filter(v -> v.getStatusPagamento() == StatusPagamento.PAGO)
            .count();

    long pendentes = vendas.stream()
            .filter(v -> v.getStatusPagamento() == StatusPagamento.PENDENTE)
            .count();

    long parciais = vendas.stream()
            .filter(v -> v.getStatusPagamento() == StatusPagamento.PARCIAL)
            .count();

    long atrasados = vendas.stream()
            .filter(Venda::isAtrasado)
            .count();

    return new StatusPagamentoDTO(
            pagos,
            pendentes,
            parciais,
            atrasados
        );
    }

public MetaMensalDTO buscarMetaMensal() {

    BigDecimal meta = new BigDecimal("10000");

    BigDecimal faturamentoAtual =
            vendaRepository.findAll()
                    .stream()
                    .map(Venda::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

    double percentual = 0;

    if (meta.compareTo(BigDecimal.ZERO) > 0) {
        percentual = faturamentoAtual
                .multiply(BigDecimal.valueOf(100))
                .divide(meta, 2, RoundingMode.HALF_UP)
                .doubleValue();
        }

    return new MetaMensalDTO(
            meta,
            faturamentoAtual,
            percentual
        );
    }

public List<LucroMensalDTO> buscarLucroMensal() {

    BigDecimal vendas = vendaRepository.findAll()
            .stream()
            .map(Venda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal compras = compraRepository.findAll()
            .stream()
            .map(Compra::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal lucro = vendas.subtract(compras);

    String mesAtual = LocalDate.now()
            .getMonth()
            .getDisplayName(
                    TextStyle.SHORT,
                    new Locale("pt", "BR"));

    return List.of(
            new LucroMensalDTO(
                    mesAtual,
                    lucro));
        }

public ResumoFinanceiroDTO buscarResumoFinanceiro() {

    BigDecimal receitas =
            vendaRepository.findAll()
                    .stream()
                    .map(Venda::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal despesas =
            compraRepository.findAll()
                    .stream()
                    .map(Compra::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal lucro =
            receitas.subtract(despesas);

    double margem =
            receitas.compareTo(BigDecimal.ZERO) > 0
                    ? lucro.divide(
                            receitas,
                            4,
                            RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue()
                        : 0;

    return new ResumoFinanceiroDTO(
            receitas,
            despesas,
            lucro,
            margem
        );
    }
}
