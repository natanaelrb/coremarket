package com.natan.coremarket.application.services.dashboard;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.dashboard.ComprasVsVendasDTO;
import com.natan.coremarket.application.dtos.dashboard.FaturamentoMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.HeatmapVendaDTO;
import com.natan.coremarket.application.dtos.dashboard.TopClienteDTO;
import com.natan.coremarket.application.dtos.dashboard.VendasPorDiaDTO;
import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.domain.entities.Venda;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;
import com.natan.coremarket.infrastructure.repositories.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardVendaService {
    
    private final VendaRepository vendaRepository;
    private final CompraRepository compraRepository;

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
}
