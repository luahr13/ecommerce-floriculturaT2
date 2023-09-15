package br.luahr.topicos1.service;

import java.util.List;

import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.EstadoResponseDTO;

public interface EstadoService {
    // recursos basicos
    List<EstadoResponseDTO> getAll();

    EstadoResponseDTO findById(Long id);

    EstadoResponseDTO create(EstadoDTO productDTO);

    EstadoResponseDTO update(Long id, EstadoDTO productDTO);

    void delete(Long id);

    // recursos extras

    List<EstadoResponseDTO> findByNome(String nome);

    long count();
}
