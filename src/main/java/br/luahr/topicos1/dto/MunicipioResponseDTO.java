package br.luahr.topicos1.dto;

import br.luahr.topicos1.model.Municipio;

public record MunicipioResponseDTO(
    Long id,
    String nome,
    EstadoResponseDTO estado
) {

    public MunicipioResponseDTO(Municipio municipio) {
        
        this(municipio.getId(),
            municipio.getNome(),
            new EstadoResponseDTO(municipio.getEstado())
        );
    }
}
