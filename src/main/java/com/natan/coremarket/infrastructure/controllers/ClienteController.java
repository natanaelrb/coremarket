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

import com.natan.coremarket.application.dtos.cliente.ClienteRequestDTO;
import com.natan.coremarket.application.dtos.cliente.ClienteResponseDTO;
import com.natan.coremarket.application.services.ClienteService;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ClienteResponseDTO salvar(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        return clienteService.salvar(clienteRequestDTO);
    }

    @GetMapping
    public List<ClienteResponseDTO> listarTodas() {
        return clienteService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
            .map(cliente -> ResponseEntity.ok().body(cliente))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return clienteService.atualizar(id, clienteRequestDTO)
            .map(clienteAtualizado -> ResponseEntity.ok().body(clienteAtualizado))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}