package br.luahr.topicos1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.luahr.topicos1.model.Usuario;
import br.luahr.topicos1.model.Sexo;

public record UsuarioResponseDTO(
    Long id,
    String nome,

    String login,
    String cpf,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Sexo sexo,
    TelefoneResponseDTO telefone,
    EnderecoResponseDTO endereco,

    String nomeImagem
) {
    public UsuarioResponseDTO (Usuario usuario){
            this(usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getCpf(),
                usuario.getSexo(),
                new TelefoneResponseDTO(usuario.getTelefone()),
                new EnderecoResponseDTO(usuario.getEndereco()),
                usuario.getNomeImagem()
                );
    }
}