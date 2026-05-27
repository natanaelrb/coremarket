package com.natan.coremarket.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.coremarket.application.dtos.empresa.EmpresaRequestDTO;
import com.natan.coremarket.application.dtos.empresa.EmpresaResponseDTO;
import com.natan.coremarket.application.services.EmpresaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public EmpresaResponseDTO salvar(@RequestBody @Valid EmpresaRequestDTO empresarRequestDTO) {
        return empresaService.salvar(empresarRequestDTO);
    }

    @GetMapping
    public List<EmpresaResponseDTO> listarTodas() {
        return empresaService.listarTodas();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id)
            .map(empresa -> ResponseEntity.ok().body(empresa))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EmpresaRequestDTO empresaRequestDTO) {
        return empresaService.atualizar(id, empresaRequestDTO)
            .map(empresaAtualizada -> ResponseEntity.ok().body(empresaAtualizada))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        empresaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}