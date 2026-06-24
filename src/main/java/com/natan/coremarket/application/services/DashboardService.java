package com.natan.coremarket.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.AlertasDashboardDTO;
import com.natan.coremarket.application.dtos.dashboard.ComparacaoMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.ComprasVsVendasDTO;
import com.natan.coremarket.application.dtos.dashboard.DashboardResumoDTO;
import com.natan.coremarket.application.dtos.dashboard.EstoqueCriticoDTO;
import com.natan.coremarket.application.dtos.dashboard.FaturamentoMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.HeatmapVendaDTO;
import com.natan.coremarket.application.dtos.dashboard.LucroMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.MetaMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.ProdutoMaisVendidoDTO;
import com.natan.coremarket.application.dtos.dashboard.ResumoFinanceiroDTO;
import com.natan.coremarket.application.dtos.dashboard.StatusPagamentoDTO;
import com.natan.coremarket.application.dtos.dashboard.TopClienteDTO;
import com.natan.coremarket.application.dtos.dashboard.VendasPorDiaDTO;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.domain.enums.StatusCompra;
import com.natan.coremarket.domain.enums.StatusPagamento;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.ItemVendaRepository;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

@Service
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;
    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;

    public DashboardService(
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            CompraRepository compraRepository,
            VendaRepository vendaRepository,
            ItemVendaRepository itemVendaRepository) {

        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.compraRepository = compraRepository;
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
    }

    public DashboardResumoDTO buscarResumo() {

    long totalClientes = clienteRepository.count();

    long totalProdutos = produtoRepository.count();

    BigDecimal totalCompras = compraRepository.findAll()
            .stream()
            .map(compra -> compra.getValorTotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalVendas = vendaRepository.findAll()
            .stream()
            .map(venda -> venda.getValorTotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal lucroLiquido =
            totalVendas.subtract(totalCompras);

    BigDecimal contaReceber = vendaRepository.findAll()
        .stream()
        .map(venda -> venda.getSaldoDevedor())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    long produtosBaixoEstoque = produtoRepository.findAll()
            .stream()
            .filter(produto ->
                    produto.getQuantidadeEstoque() <= 10)
            .count();

    long comprasPendentes = compraRepository.findAll()
            .stream()
            .filter(compra ->
                    compra.getSaldoDevedor()
                            .compareTo(BigDecimal.ZERO) > 0)
            .count();

    long vendasPendentes = vendaRepository.findAll()
            .stream()
            .filter(venda ->
                    venda.getSaldoDevedor()
                            .compareTo(BigDecimal.ZERO) > 0)
            .count();

    return new DashboardResumoDTO(
            totalClientes,
            totalProdutos,
            totalCompras,
            totalVendas,
            lucroLiquido,
            contaReceber,
            produtosBaixoEstoque,
            comprasPendentes,
            vendasPendentes
            
        );
    }

public List<FaturamentoMensalDTO> buscarFaturamentoMensal() {

    Map<String, BigDecimal> vendasPorMes =
            vendaRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(
                            venda -> traduzirMes(venda.getDataVenda()
                                    .getMonth()),                 
                            Collectors.mapping(
                                    Venda::getValorTotal,
                                    Collectors.reducing(
                                            BigDecimal.ZERO,
                                            BigDecimal::add
                                        )
                                )
                        ));

    Map<String, BigDecimal> comprasPorMes =
            compraRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(
                            compra -> traduzirMes(compra.getDataCompra()
                                    .getMonth()),
                            Collectors.mapping(
                                    Compra::getValorTotal,
                                    Collectors.reducing(
                                            BigDecimal.ZERO,
                                            BigDecimal::add
                                        )
                                )
                        ));

    Set<String> meses = new HashSet<>();
    meses.addAll(vendasPorMes.keySet());
    meses.addAll(comprasPorMes.keySet());

    return meses.stream()
            .map(mes -> {

                BigDecimal vendas =
                        vendasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );

                BigDecimal compras =
                        comprasPorMes.getOrDefault(
                                mes,
                                BigDecimal.ZERO
                        );

                BigDecimal lucro =
                        vendas.subtract(compras);

                return new FaturamentoMensalDTO(
                        mes,
                        vendas,
                        compras,
                        lucro
                );
        })
        .toList();

        }

        private String traduzirMes(Month mes) {
                return switch (mes) {
                        case JANUARY -> "Jan";
                        case FEBRUARY -> "Fev";
                        case MARCH -> "Mar";
                        case APRIL -> "Abr";
                        case MAY -> "Mai";
                        case JUNE -> "Jun";
                        case JULY -> "Jul";
                        case AUGUST -> "Ago";
                        case SEPTEMBER -> "Set";
                        case OCTOBER -> "Out";
                        case NOVEMBER -> "Nov";
                        case DECEMBER -> "Dez";
                };
        }

public List<ProdutoMaisVendidoDTO> buscarProdutosMaisVendidos() {
        return itemVendaRepository.buscarProdutosMaisVendidos();

        }

public List<TopClienteDTO> buscarTopClientes() {

    Map<String, BigDecimal> clientes = vendaRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    venda -> venda.getCliente().getNome(),
                    Collectors.reducing(
                            BigDecimal.ZERO,
                            Venda::getValorTotal,
                            BigDecimal::add)));

    return clientes.entrySet()
            .stream()
            .sorted(
                    Map.Entry
                            .<String, BigDecimal>comparingByValue()
                            .reversed())
            .limit(5)
            .map(cliente ->
                    new TopClienteDTO(
                            cliente.getKey(),
                            cliente.getValue()))
            .toList();
        }

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

public List<ComprasVsVendasDTO> buscarComprasVsVendas() {

    BigDecimal totalCompras = compraRepository.findAll()
            .stream()
            .map(Compra::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalVendas = vendaRepository.findAll()
            .stream()
            .map(Venda::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return List.of(
            new ComprasVsVendasDTO(
                    "Atual",
                    totalCompras,
                    totalVendas
                )
        );
        }

public List<EstoqueCriticoDTO> buscarEstoqueCritico() {

    return produtoRepository.findAll()
            .stream()
            .filter(p -> p.getQuantidadeEstoque() <= 10)
            .map(p -> new EstoqueCriticoDTO(
                    p.getNome(),
                    p.getQuantidadeEstoque()))
            .toList();
        }

        public List<VendasPorDiaDTO> buscarVendasPorDia() {

    Map<String, BigDecimal> vendasPorDia =
            vendaRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(
                            venda -> venda.getDataVenda()
                                    .toLocalDate()
                                    .toString(),
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    Venda::getValorTotal,
                                    BigDecimal::add
                            )
                    ));

    return vendasPorDia.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> new VendasPorDiaDTO(
                    entry.getKey(),
                    entry.getValue()
            ))
            .toList();
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

        public List<HeatmapVendaDTO> buscarHeatmapVendas() {

    return vendaRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    venda ->
                            venda.getDataVenda()
                                    .getDayOfWeek()
                                    .getDisplayName(
                                            TextStyle.SHORT,
                                            new Locale("pt", "BR"))
                            + "-"
                            + venda.getDataVenda().getHour(),
                    Collectors.counting()))
            .entrySet()
            .stream()
            .map(entry -> {

                String[] partes =
                        entry.getKey().split("-");

                return new HeatmapVendaDTO(
                        partes[0],
                        partes[1] + "h",
                        entry.getValue()
                );
            })
            .toList();
        }

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