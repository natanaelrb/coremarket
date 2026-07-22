package com.natan.coremarket.application.services;

import java.util.List;

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
import com.natan.coremarket.application.services.dashboard.DashboardAnaliseService;
import com.natan.coremarket.application.services.dashboard.DashboardFinanceiroService;
import com.natan.coremarket.application.services.dashboard.DashboardProdutoService;
import com.natan.coremarket.application.services.dashboard.DashboardResumoService;
import com.natan.coremarket.application.services.dashboard.DashboardVendaService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardFinanceiroService dashboardFinanceiroService;
    private final DashboardResumoService dashboardResumoService;
    private final DashboardProdutoService dashboardProdutoService;
    private final DashboardVendaService dashboardVendaService;
    private final DashboardAnaliseService dashboardAnaliseService;


        public DashboardResumoDTO buscarResumo() {
                return dashboardResumoService.buscarResumo();
        }

        public StatusPagamentoDTO buscarStatusPagamentos() {
                return dashboardFinanceiroService.buscarStatusPagamentos();
        }

        public MetaMensalDTO buscarMetaMensal() {
                return dashboardFinanceiroService.buscarMetaMensal();
        }

        public List<LucroMensalDTO> buscarLucroMensal() {
                return dashboardFinanceiroService.buscarLucroMensal();
        }

        public ResumoFinanceiroDTO buscarResumoFinanceiro() {
                return dashboardFinanceiroService.buscarResumoFinanceiro();
        }

        public List<ProdutoMaisVendidoDTO> buscarProdutosMaisVendidos() {
                return dashboardProdutoService.buscarProdutosMaisVendidos();
        }

        public List<EstoqueCriticoDTO> buscarEstoqueCritico() {
                return dashboardProdutoService.buscarEstoqueCritico();
        }

        public List<FaturamentoMensalDTO> buscarFaturamentoMensal() {
                return dashboardVendaService.buscarFaturamentoMensal();
        }

        public List<TopClienteDTO> buscarTopClientes() {
                return dashboardVendaService.buscarTopClientes();
        }

        public List<ComprasVsVendasDTO> buscarComprasVsVendas() {
                return dashboardVendaService.buscarComprasVsVendas();
        }

        public List<VendasPorDiaDTO> buscarVendasPorDia() {
                return dashboardVendaService.buscarVendasPorDia();
        }
        
        public List<HeatmapVendaDTO> buscarHeatmapVendas() {
                return dashboardVendaService.buscarHeatmapVendas();
        }

        public AlertasDashboardDTO buscarAlertasDashboard() {
                return dashboardAnaliseService.buscarAlertasDashboard();
        }

        public ComparacaoMensalDTO buscarComparacaoMensal() {
                return dashboardAnaliseService.buscarComparacaoMensal();
        }
}