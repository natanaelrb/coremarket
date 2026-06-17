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

import com.natan.coremarket.application.dtos.venda.VendaRequestDTO;
import com.natan.coremarket.application.dtos.venda.VendaResponseDTO;
import com.natan.coremarket.application.services.VendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public VendaResponseDTO salvar(@RequestBody @Valid VendaRequestDTO vendaRequestDTO) {
        return vendaService.salvar(vendaRequestDTO);
    }

    @GetMapping
    public List<VendaResponseDTO> listarTodos() {
        return vendaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable Long id) {
        return vendaService.buscarPorId(id)
                .map(venda -> ResponseEntity.ok().body(venda))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pendentes")
    public List<VendaResponseDTO> listarPendentes() {
        return vendaService.listarPendentes();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody VendaRequestDTO vendaRequestDTO) {
        return vendaService.atualizar(id, vendaRequestDTO)
                .map(vendaAtualizada -> ResponseEntity.ok().body(vendaAtualizada))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
