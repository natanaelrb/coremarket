package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.cliente.ClienteRequestDTO;
import com.natan.coremarket.application.dtos.cliente.ClienteResponseDTO;
import com.natan.coremarket.domain.entities.Cliente;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;

    public ClienteService(ClienteRepository clienteRepository, EmpresaRepository empresaRepository) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
    }
    
    public ClienteResponseDTO salvar(ClienteRequestDTO clienteRequestDTO) {

        Empresa empresa = empresaRepository.findById(clienteRequestDTO.getEmpresaId())
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Cliente cliente = new Cliente();

        cliente.setNome(clienteRequestDTO.getNome());
        cliente.setTelefone(clienteRequestDTO.getTelefone());
        cliente.setObservacao(clienteRequestDTO.getObservacao());
        cliente.setAtivo(clienteRequestDTO.getAtivo());
        cliente.setEmpresa(empresa);

        Cliente clienteSalvo = clienteRepository.save(cliente);


        return new ClienteResponseDTO(
            clienteSalvo.getId(),
            clienteSalvo.getNome(),
            clienteSalvo.getTelefone(),
            clienteSalvo.isAtivo()
        );
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
            .map(clienteSalvo -> new ClienteResponseDTO(
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                clienteSalvo.getTelefone(),
                clienteSalvo.isAtivo()
            ))
            .collect(Collectors.toList());
    }

    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(cliente -> new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefone(),
            cliente.isAtivo()
        ));
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Optional<ClienteResponseDTO> atualizar(Long id, ClienteRequestDTO clienteRequestDTO) {
        return clienteRepository.findById(id)
            .map(clienteExistente -> {
                clienteExistente.setNome(clienteRequestDTO.getNome());
                clienteExistente.setTelefone(clienteRequestDTO.getTelefone());
                clienteExistente.setObservacao(clienteRequestDTO.getObservacao());
                clienteExistente.setAtivo(clienteRequestDTO.getAtivo());
                Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
                return new ClienteResponseDTO(
                    clienteAtualizado.getId(),
                    clienteAtualizado.getNome(),
                    clienteAtualizado.getTelefone(),
                    clienteAtualizado.isAtivo()
                );
            });
    }
}