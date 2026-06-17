package com.natan.coremarket.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natan.coremarket.domain.entities.Fornecedor;
import com.natan.coremarket.infrastructure.repositories.FornecedorRepository;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(
            FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor atualizar(Long id,
            Fornecedor fornecedorAtualizado) {

        Fornecedor fornecedor = buscarPorId(id);

        fornecedor.setNome(fornecedorAtualizado.getNome());
        fornecedor.setTelefone(fornecedorAtualizado.getTelefone());
        fornecedor.setEmail(fornecedorAtualizado.getEmail());
        fornecedor.setObservacao(
                fornecedorAtualizado.getObservacao());

        return fornecedorRepository.save(fornecedor);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        fornecedorRepository.deleteById(id);
    }
}
