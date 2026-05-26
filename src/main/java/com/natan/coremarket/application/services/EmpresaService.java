package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.empresa.EmpresaRequestDTO;
import com.natan.coremarket.application.dtos.empresa.EmpresaResponseDTO;
import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;
@Service
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public EmpresaResponseDTO salvar(EmpresaRequestDTO empresaRequestDTO) {

        Empresa empresa = new Empresa();

        empresa.setNome(empresaRequestDTO.getNome());
        empresa.setEmail(empresaRequestDTO.getEmail());
        empresa.setTelefone(empresaRequestDTO.getTelefone());
        empresa.setCnpj(empresaRequestDTO.getCnpj());

        Empresa empresaSalva = empresaRepository.save(empresa);

        return new EmpresaResponseDTO(
            empresaSalva.getId(),
            empresaSalva.getNome(),
            empresaSalva.getEmail(),
            empresaSalva.getTelefone()
        );
    }

   public List<EmpresaResponseDTO> listarTodas() {
        return empresaRepository.findAll().stream()
            .map(empresa -> new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getTelefone()
            ))
            .collect(Collectors.toList());
    }

    public Optional<EmpresaResponseDTO> buscarPorId(Long id) {
        return empresaRepository.findById(id)
            .map(empresa -> new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getTelefone()
            ));
    }

    public void deletar(Long id) {
        empresaRepository.deleteById(id);
    }

    public Optional<EmpresaResponseDTO> atualizar(Long id, EmpresaRequestDTO empresaRequestDTO) {
        return empresaRepository.findById(id)
            .map(empresaExistente -> {

                empresaExistente.setNome(empresaRequestDTO.getNome());
                empresaExistente.setEmail(empresaRequestDTO.getEmail());
                empresaExistente.setTelefone(empresaRequestDTO.getTelefone());
                empresaExistente.setCnpj(empresaRequestDTO.getCnpj());

                Empresa empresaAtualizada = empresaRepository.save(empresaExistente);

                return new EmpresaResponseDTO(
                    empresaAtualizada.getId(),
                    empresaAtualizada.getNome(),
                    empresaAtualizada.getEmail(),
                    empresaAtualizada.getTelefone()
                );
            });
    }
}