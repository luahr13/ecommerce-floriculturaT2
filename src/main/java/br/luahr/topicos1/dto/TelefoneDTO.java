package br.luahr.topicos1.dto;

import jakarta.validation.constraints.NotBlank;

public record TelefoneDTO(
    @NotBlank(message = "O campo precisa ser preenchido.")
    String codigoArea,
    @NotBlank(message = "O campo precisa ser preenchido.")
    String numero
) {

}