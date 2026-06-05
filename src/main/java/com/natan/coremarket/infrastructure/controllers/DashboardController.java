package com.natan.coremarket.infrastructure.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.coremarket.application.dtos.dashboard.DashboardResumoDTO;
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
}
