package com.natan.coremarket.infrastructure.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.coremarket.application.dtos.dashboard.AlertasDashboardDTO;
import com.natan.coremarket.application.dtos.dashboard.ComparacaoMensalDTO;
import com.natan.coremarket.application.dtos.dashboard.ComprasVsVendasDTO;
import com.natan.coremarket.application.dtos.dashboard.ContaReceberDTO;
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
import com.natan.coremarket.application.services.DashboardService;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public DashboardResumoDTO buscarResumo() {
        return dashboardService.buscarResumo();
    }

    @GetMapping("/produtos-mais-vendidos")
    public List<ProdutoMaisVendidoDTO> buscarProdutosMaisVendidos() {
        return dashboardService.buscarProdutosMaisVendidos();
    }

    @GetMapping("/top-clientes")
    public List<TopClienteDTO> buscarTopClientes() {
        return dashboardService.buscarTopClientes();
    }

    @GetMapping("/status-pagamentos")
    public StatusPagamentoDTO buscarStatusPagamentos() {
        return dashboardService.buscarStatusPagamentos();
    }

    @GetMapping("/alertas")
    public ResponseEntity<AlertasDashboardDTO> buscarAlertas() {
        return ResponseEntity.ok(
                dashboardService.buscarAlertasDashboard()
        );
    }

    @GetMapping("/faturamento-mensal")
    public List<FaturamentoMensalDTO> buscarFaturamentoMensal() {
        return dashboardService.buscarFaturamentoMensal();
    }

    @GetMapping("/compras-vs-vendas")
    public List<ComprasVsVendasDTO> buscarComprasVsVendas() {
        return dashboardService.buscarComprasVsVendas();

    }

    @GetMapping("/contas-receber")
    public List<ContaReceberDTO> buscarContaReceber() {
        return List.of();

    }

    @GetMapping("/estoque-critico")
    public List<EstoqueCriticoDTO> buscarEstoqueCritico() {
        return dashboardService.buscarEstoqueCritico();
    }

    @GetMapping("/vendas-por-dia")
    public List<VendasPorDiaDTO> buscarVendasPorDia() {
        return dashboardService.buscarVendasPorDia();
    }

    @GetMapping("/meta-mensal")
    public ResponseEntity<MetaMensalDTO> buscarMetaMensal() {
        return ResponseEntity.ok(
                dashboardService.buscarMetaMensal()
        );
    }

    @GetMapping("/lucro-mensal")
    public ResponseEntity<List<LucroMensalDTO>>
            buscarLucroMensal() {

        return ResponseEntity.ok(
                dashboardService.buscarLucroMensal());
    }

    @GetMapping("/heatmap-vendas")
    public ResponseEntity<List<HeatmapVendaDTO>>
            buscarHeatmapVendas() {

        return ResponseEntity.ok(
                dashboardService.buscarHeatmapVendas());
    }

    @GetMapping("/comparacao-mensal")
    public ResponseEntity<ComparacaoMensalDTO> buscarComparacaoMensal() {

        return ResponseEntity.ok(
                dashboardService.buscarComparacaoMensal()
        );
    }

    @GetMapping("/resumo-financeiro")
    public ResponseEntity<ResumoFinanceiroDTO>
    buscarResumoFinanceiro() {

        return ResponseEntity.ok(
                dashboardService.buscarResumoFinanceiro()
        );
    }
}