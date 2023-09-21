package br.luahr.topicos1.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.luahr.topicos1.model.Usuario;
import br.luahr.topicos1.model.Endereco;
import br.luahr.topicos1.model.Sexo;
import br.luahr.topicos1.model.Telefone;

public record UsuarioResponseDTO(
    Long id,
    String nome,

    String login,
    String cpf,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Sexo sexo,
    List<Telefone> telefones,
    Endereco endereco,

    String nomeImagem
) {

    public UsuarioResponseDTO (Usuario usuario){
            this(usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getCpf(),
                usuario.getSexo(),
                usuario.getTelefone(),
                usuario.getEndereco(),
                usuario.getNomeImagem()
                );
    }
}