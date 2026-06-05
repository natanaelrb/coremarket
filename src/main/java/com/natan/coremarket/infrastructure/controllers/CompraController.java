package com.natan.coremarket.infrastructure.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.coremarket.application.dtos.compra.CompraRequestDTO;
import com.natan.coremarket.application.dtos.compra.CompraResponseDTO;
import com.natan.coremarket.application.services.CompraService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/compras")
public class CompraController {
    
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public CompraResponseDTO salvar(@RequestBody @Valid CompraRequestDTO compraRequestDTO) {
        return compraService.salvar(compraRequestDTO);
    }

    @GetMapping
    public List<CompraResponseDTO> listarTodos() {
        return compraService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Long id) {
        return compraService.buscarPorId(id)
            .map(compra -> ResponseEntity.ok().body(compra))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pendentes")
    public List<CompraResponseDTO> listarPendentes() {
        return compraService.listarPendentes();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO compraRequestDTO) {
        return compraService.atualizar(id, compraRequestDTO)
            .map(compraAtualizada -> ResponseEntity.ok().body(compraAtualizada))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        compraService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}