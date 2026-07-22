package com.natan.coremarket.application.services.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.AlertasDashboardDTO;
import com.natan.coremarket.application.dtos.dashboard.ComparacaoMensalDTO;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.domain.enums.StatusCompra;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardAnaliseService {
    
    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;
    private final VendaRepository vendaRepository;

public AlertasDashboardDTO buscarAlertasDashboard() {

    long produtosSemEstoque =
            produtoRepository.findAll()
                    .stream()
                    .filter(p -> p.getQuantidadeEstoque() <= 0)
                    .count();

    long comprasPendentes =
            compraRepository.findAll()
                    .stream()
                    .filter(c -> c.getStatus() == StatusCompra.PENDENTE)
                    .count();

    long contasAtrasadas =
            vendaRepository.findAll()
                    .stream()
                    .filter(Venda::isAtrasado)
                    .count();

    long produtosSemVenda =
        produtoRepository.countByUltimaVendaIsNull()
        +
        produtoRepository.countByUltimaVendaBefore(
                LocalDateTime.now().minusDays(30));

    return new AlertasDashboardDTO(
            produtosSemEstoque,
            comprasPendentes,
            contasAtrasadas,
            produtosSemVenda
        );
        }
        
        private double calcularVariacao(
        BigDecimal atual,
        BigDecimal anterior) {

        if (anterior.compareTo(BigDecimal.ZERO) == 0) {
                return atual.compareTo(BigDecimal.ZERO) > 0 ? 100 : 0;
        }

        return atual
                .subtract(anterior)
                .divide(anterior, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        }

public ComparacaoMensalDTO buscarComparacaoMensal() {

    YearMonth mesAtual = YearMonth.now();
    YearMonth mesAnterior = mesAtual.minusMonths(1);

    BigDecimal vendasAtual = vendaRepository.findAll()
            .stream()
            .filter(v -> YearMonth.from(
                    v.getDataVenda().toLocalDate())
                    .equals(mesAtual))
            .map(Venda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal vendasAnterior = vendaRepository.findAll()
            .stream()
            .filter(v -> YearMonth.from(
                    v.getDataVenda().toLocalDate())
                    .equals(mesAnterior))
            .map(Venda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal comprasAtual = compraRepository.findAll()
            .stream()
            .filter(c -> YearMonth.from(
                    c.getDataCompra().toLocalDate())
                    .equals(mesAtual))
            .map(Compra::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal comprasAnterior = compraRepository.findAll()
            .stream()
            .filter(c -> YearMonth.from(
                    c.getDataCompra().toLocalDate())
                    .equals(mesAnterior))
            .map(Compra::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal lucroAtual =
            vendasAtual.subtract(comprasAtual);

    BigDecimal lucroAnterior =
            vendasAnterior.subtract(comprasAnterior);

    return new ComparacaoMensalDTO(
            calcularVariacao(vendasAtual, vendasAnterior),
            calcularVariacao(comprasAtual, comprasAnterior),
            calcularVariacao(lucroAtual, lucroAnterior)
        );
        }
}
