package br.luahr.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadoDTO(
    @NotBlank(message = "O campo precisa ser preenchido.")
    @Size(max = 50, message = "O nome deve possuir no máximo 50 caracteres.")
    String nome,

    @NotBlank(message = "O campo precisa ser preenchido.")
    @Size(max = 2, message = "O nome deve possuir no máximo 2 caracteres.")
    String sigla
) {
    
}
