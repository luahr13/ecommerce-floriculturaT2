package br.luahr.topicos1.dto;

import br.luahr.topicos1.model.Endereco;

public record EnderecoResponseDTO(
    Long id,
    String bairro,
    String numero,
    String complemento,
    String cep,
    MunicipioResponseDTO municipio
) {
    public EnderecoResponseDTO(Endereco endereco) {
        this(
            endereco.getId(),
            endereco.getBairro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getCep(),
            new MunicipioResponseDTO(endereco.getMunicipio())
        );
    }
}
