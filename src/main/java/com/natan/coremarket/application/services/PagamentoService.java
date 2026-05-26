package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.pagamento.PagamentoRequestDTO;
import com.natan.coremarket.application.dtos.pagamento.PagamentoResponseDTO;
import com.natan.coremarket.domain.entities.Cliente;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.domain.entities.Pagamento;
import com.natan.coremarket.infrastructure.repositories.ClienteRepository;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
import com.natan.coremarket.infrastructure.repositories.PagamentoRepository;
@Service
public class PagamentoService {
    
    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    

    public PagamentoService(PagamentoRepository pagamentoRepository, ClienteRepository clienteRepository, EmpresaRepository empresaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
    }
    
    public PagamentoResponseDTO salvar(PagamentoRequestDTO pagamentoRequestDTO) {

        Cliente cliente = clienteRepository.findById(pagamentoRequestDTO.getClienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findById(pagamentoRequestDTO.getEmpresaId())
            .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Pagamento pagamento = new Pagamento();

        pagamento.setValorPago(pagamentoRequestDTO.getValorPago());
        pagamento.setObservacao(pagamentoRequestDTO.getObservacao());
        pagamento.setCliente(cliente);
        pagamento.setEmpresa(empresa);

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);
        
        return new PagamentoResponseDTO(
            pagamentoSalvo.getId(),
            pagamentoSalvo.getValorPago(),
            pagamentoSalvo.getObservacao()
        );
    }

    public List<PagamentoResponseDTO> listarTodos() {
        return pagamentoRepository.findAll().stream()
            .map(pagamento -> new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getValorPago(),
                pagamento.getObservacao()
            ))
            .collect(Collectors.toList());
    }

    public Optional<PagamentoResponseDTO> buscarPorId(Long id) {
        return pagamentoRepository.findById(id).map(pagamento -> new PagamentoResponseDTO(
            pagamento.getId(),
            pagamento.getValorPago(),
            pagamento.getObservacao()
        ));
    }

    public void deletar(Long id) {
        pagamentoRepository.deleteById(id);
    }
    
    public Optional<PagamentoResponseDTO> atualizar(Long id, PagamentoRequestDTO pagamentoRequestDTO) {
        return pagamentoRepository.findById(id)
            .map(pagamentoExistente -> {
                pagamentoExistente.setValorPago(pagamentoRequestDTO.getValorPago());
                pagamentoExistente.setObservacao(pagamentoRequestDTO.getObservacao());

                Cliente cliente = clienteRepository.findById(pagamentoRequestDTO.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
                pagamentoExistente.setCliente(cliente);

                Empresa empresa = empresaRepository.findById(pagamentoRequestDTO.getEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));
                pagamentoExistente.setEmpresa(empresa);

                Pagamento pagamentoAtualizado = pagamentoRepository.save(pagamentoExistente);
                
                return new PagamentoResponseDTO(
                    pagamentoAtualizado.getId(),
                    pagamentoAtualizado.getValorPago(),
                    pagamentoAtualizado.getObservacao()
                );
            });
    }

}