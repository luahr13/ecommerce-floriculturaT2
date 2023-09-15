package br.luahr.topicos1.service;

import java.util.List;

import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EnderecoResponseDTO;

public interface EnderecoService {
    // recursos basicos
    List<EnderecoResponseDTO> getAll();

    EnderecoResponseDTO findById(Long id);

    EnderecoResponseDTO create(EnderecoDTO productDTO);

    EnderecoResponseDTO update(Long id, EnderecoDTO productDTO);

    void delete(Long id);

    // recursos extras

    List<EnderecoResponseDTO> findByNome(String nome);

    long count();
}
