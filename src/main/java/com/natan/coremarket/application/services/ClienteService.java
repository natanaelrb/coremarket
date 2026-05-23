package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.natan.coremarket.domain.entities.Cliente;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
    
    public Optional<Cliente> atualizar(Long id, Cliente cliente) {
        return clienteRepository.findById(id)
            .map(clienteExistente -> {
                clienteExistente.setNome(cliente.getNome());
                clienteExistente.setTelefone(cliente.getTelefone());
                clienteExistente.setObservacao(cliente.getObservacao());
                clienteExistente.setAtivo(cliente.isAtivo());
                return clienteRepository.save(clienteExistente);
            });
    }
}
