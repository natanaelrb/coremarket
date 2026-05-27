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

import com.natan.coremarket.application.dtos.pagamento.PagamentoRequestDTO;
import com.natan.coremarket.application.dtos.pagamento.PagamentoResponseDTO;
import com.natan.coremarket.application.services.PagamentoService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public PagamentoResponseDTO salvar(@RequestBody @Valid PagamentoRequestDTO pagamentoRequestDTO) {
        return pagamentoService.salvar(pagamentoRequestDTO);
    }

    @GetMapping
    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
            .map(pagamento -> ResponseEntity.ok().body(pagamento))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        return pagamentoService.atualizar(id, pagamentoRequestDTO)
            .map(pagamento -> ResponseEntity.ok().body(pagamento))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pagamentoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}