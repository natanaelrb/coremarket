package com.natan.coremarket.application.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.natan.coremarket.application.dtos.produto.ProdutoRequestDTO;
import com.natan.coremarket.application.dtos.produto.ProdutoResponseDTO;
import com.natan.coremarket.domain.entities.Produto;
import com.natan.coremarket.infrastructure.repositories.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {

        Produto produto = new Produto();

        produto.setNome(produtoRequestDTO.getNome());
        produto.setDescricao(produtoRequestDTO.getDescricao());
        produto.setPreco(produtoRequestDTO.getPreco());
        produto.setQuantidadeEstoque(produtoRequestDTO.getQuantidadeEstoque());

        Produto produtoSalvo = produtoRepository.save(produto);

        return new ProdutoResponseDTO(
                produtoSalvo.getId(),
                produtoSalvo.getNome(),
                produtoSalvo.getDescricao(),
                produtoSalvo.getPreco(),
                produtoSalvo.getQuantidadeEstoque()
        );
    }

    public List<ProdutoResponseDTO> listarTodos() {

        return produtoRepository.findAll()
                .stream()
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getQuantidadeEstoque()
                ))
                .collect(Collectors.toList());
    }

    public Optional<ProdutoResponseDTO> buscarPorId(Long id) {

        return produtoRepository.findById(id)
                .map(produto -> new ProdutoResponseDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getQuantidadeEstoque()
                ));
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }

    public Optional<ProdutoResponseDTO> atualizar(Long id,
                                                  ProdutoRequestDTO produtoRequestDTO) {

        return produtoRepository.findById(id)
                .map(produtoExistente -> {

                    produtoExistente.setNome(produtoRequestDTO.getNome());
                    produtoExistente.setDescricao(produtoRequestDTO.getDescricao());
                    produtoExistente.setPreco(produtoRequestDTO.getPreco());
                    produtoExistente.setQuantidadeEstoque(
                            produtoRequestDTO.getQuantidadeEstoque()
                    );

                    Produto produtoAtualizado =
                            produtoRepository.save(produtoExistente);

                    return new ProdutoResponseDTO(
                            produtoAtualizado.getId(),
                            produtoAtualizado.getNome(),
                            produtoAtualizado.getDescricao(),
                            produtoAtualizado.getPreco(),
                            produtoAtualizado.getQuantidadeEstoque()
                    );
                });
    }
}