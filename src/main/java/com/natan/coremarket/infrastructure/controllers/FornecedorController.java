package com.natan.coremarket.infrastructure.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natan.coremarket.application.services.FornecedorService;
import com.natan.coremarket.domain.entities.Fornecedor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping
    public List<Fornecedor> listarTodos() {
        return fornecedorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Fornecedor buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor salvar(
            @RequestBody Fornecedor fornecedor) {

        return fornecedorService.salvar(fornecedor);
    }

    @PutMapping("/{id}")
    public Fornecedor atualizar(
            @PathVariable Long id,
            @RequestBody Fornecedor fornecedor) {

        return fornecedorService.atualizar(
                id,
                fornecedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
    }
}