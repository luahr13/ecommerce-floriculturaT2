package br.luahr.topicos1.service;

import java.util.List;

import br.luahr.topicos1.dto.TelefoneDTO;
import br.luahr.topicos1.dto.TelefoneResponseDTO;

public interface TelefoneService {
    // recursos basicos
    List<TelefoneResponseDTO> getAll();

    TelefoneResponseDTO findById(Long id);

    TelefoneResponseDTO create(TelefoneDTO productDTO);

    TelefoneResponseDTO update(Long id, TelefoneDTO productDTO);

    void delete(Long id);

    // recursos extras

    List<TelefoneResponseDTO> findByNome(String nome);

    long count();
}
