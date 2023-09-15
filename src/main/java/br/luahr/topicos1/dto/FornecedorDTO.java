package br.luahr.topicos1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FornecedorDTO(
    @NotBlank(message = "O campo precisa ser preenchido.")
    String nome,
    @NotBlank(message = "O campo precisa ser preenchido.")
    String pais,
    @NotBlank(message = "O campo precisa ser preenchido.")
    String safra,
    @NotNull(message = "O campo precisa ser preenchido.")
    Float volume
) {

}
