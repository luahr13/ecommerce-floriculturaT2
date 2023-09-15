package br.luahr.topicos1.service;

import java.util.List;

import br.luahr.topicos1.dto.ClienteDTO;
import br.luahr.topicos1.dto.ClienteResponseDTO;
import br.luahr.topicos1.model.Cliente;

public interface ClienteService {
    // recursos basicos
    List<ClienteResponseDTO> getAll();

    ClienteResponseDTO findById(Long id);

    ClienteResponseDTO create(ClienteDTO productDTO);

    ClienteResponseDTO update(Long id, ClienteDTO productDTO);

    Cliente findByLoginAndSenha(String login, String senha);

    ClienteResponseDTO update(Long id, String nomeImagem);

    ClienteResponseDTO findByLogin(String login);

    void delete(Long id);

    // recursos extras

    List<ClienteResponseDTO> findByNome(String nome);

    long count();
}
