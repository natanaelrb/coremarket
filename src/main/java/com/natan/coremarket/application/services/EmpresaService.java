package com.natan.coremarket.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natan.coremarket.domain.entities.Empresa;
import com.natan.coremarket.infrastructure.repositories.EmpresaRepository;

@Service
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Empresa salvar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    
}
