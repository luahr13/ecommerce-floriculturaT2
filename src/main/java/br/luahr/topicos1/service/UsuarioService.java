package br.luahr.topicos1.service;

import java.util.List;

import br.luahr.topicos1.dto.UsuarioDTO;
import br.luahr.topicos1.dto.UsuarioResponseDTO;
import br.luahr.topicos1.model.Usuario;

public interface UsuarioService {
    // recursos basicos
    List<UsuarioResponseDTO> getAll();

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO create(UsuarioDTO productDTO);

    UsuarioResponseDTO update(Long id, UsuarioDTO productDTO);

    Usuario findByLoginAndSenha(String login, String senha);

    UsuarioResponseDTO update(Long id, String nomeImagem);

    UsuarioResponseDTO findByLogin(String login);

    void delete(Long id);

    // recursos extras

    List<UsuarioResponseDTO> findByNome(String nome);

    long count();
}
