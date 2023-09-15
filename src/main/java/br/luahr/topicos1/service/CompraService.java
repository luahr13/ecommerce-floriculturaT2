package br.luahr.topicos1.service;

import java.util.List;
import br.luahr.topicos1.dto.CompraDTO;
import br.luahr.topicos1.dto.CompraResponseDTO;

public interface CompraService {
    List<CompraResponseDTO> getAll();

    CompraResponseDTO findById(Long id);

    CompraResponseDTO create(CompraDTO compradto);

    CompraResponseDTO update(Long id, CompraDTO compradto);

    void delete(Long id);

    Long count();

    List<CompraResponseDTO> findByNome(String nome);
}
