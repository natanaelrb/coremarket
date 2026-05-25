package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.natan.coremarket.domain.entities.Compra;
import com.natan.coremarket.infrastructure.repositories.CompraRepository;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public Compra salvar(Compra compra) {

        compra.getItens().forEach(item -> {
            item.setCompra(compra);
        });

        return compraRepository.save(compra);
    }

    public List<Compra> listarTodos() {
        return compraRepository.findAll();
    }

    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findById(id);
    }

    public void deletar(Long id) {
        compraRepository.deleteById(id);
    }

    public Optional<Compra> atualizar(Long id, Compra compra) {
        return compraRepository.findById(id)
            .map(compraExistente -> {
                compraExistente.setCliente(compra.getCliente());
                compraExistente.setEmpresa(compra.getEmpresa());
                compraExistente.setValorTotal(compra.getValorTotal());
                compraExistente.setStatus(compra.getStatus());
                return compraRepository.save(compraExistente);
            });
    }
    
}
