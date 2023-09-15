package br.luahr.topicos1.dto;

import br.luahr.topicos1.model.Telefone;

public record TelefoneResponseDTO(
    Long id,
    String codigoArea,
    String numero
) {
    public TelefoneResponseDTO(Telefone telefone) {
        this(
            telefone.getId(),
            telefone.getCodigoArea(),
            telefone.getNumero());
    }
}