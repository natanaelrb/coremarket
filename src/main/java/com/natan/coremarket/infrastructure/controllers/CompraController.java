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

import com.natan.coremarket.application.services.CompraService;
import com.natan.coremarket.domain.entities.Compra;

@RestController
@RequestMapping("/compras")
public class CompraController {
    
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public Compra salvar(@RequestBody Compra compra) {
        return compraService.salvar(compra);
    }

    @GetMapping
    public List<Compra> listarTodos() {
        return compraService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Compra> buscarPorId(@PathVariable Long id) {
        return compraService.buscarPorId(id)
            .map(compra -> ResponseEntity.ok().body(compra))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> atualizar(@PathVariable Long id, @RequestBody Compra compra) {
        return compraService.atualizar(id, compra)
            .map(compraAtualizada -> ResponseEntity.ok().body(compraAtualizada))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        compraService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
