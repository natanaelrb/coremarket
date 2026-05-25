package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.natan.coremarket.domain.entities.Pagamento;
import com.natan.coremarket.infrastructure.repositories.PagamentoRepository;

@Service
public class PagamentoService {
    
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }
    
    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    public void deletar(Long id) {
        pagamentoRepository.deleteById(id);
    }
    
    public Optional<Pagamento> atualizar(Long id, Pagamento pagamento) {
        return pagamentoRepository.findById(id)
            .map(pagamentoExistente -> {
                pagamentoExistente.setValorPago(pagamento.getValorPago());
                pagamentoExistente.setObservacao(pagamento.getObservacao());
                pagamentoExistente.setCliente(pagamento.getCliente());
                pagamentoExistente.setEmpresa(pagamento.getEmpresa());            
                return pagamentoRepository.save(pagamentoExistente);
            });
    }

}
