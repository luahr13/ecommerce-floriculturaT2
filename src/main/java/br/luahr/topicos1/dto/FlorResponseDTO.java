package br.luahr.topicos1.dto;

import br.luahr.topicos1.model.Flor;

public record FlorResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double valorUnidade,
    String corPetalas,
    Float alturaCaule,
    String tipoFlor,
    FornecedorResponseDTO fornecedor,
    String nomeImagem
) {
    public FlorResponseDTO(Flor flor) {
        this(
            flor.getId(),
            flor.getNome(),
            flor.getDescricao(),
            flor.getValorUnidade(),
            flor.getCorPetalas(),
            flor.getAlturaCaule(),
            flor.getTipoFlor().getLabel(),
            new FornecedorResponseDTO(flor.getFornecedor()),
            flor.getNomeImagem()
        );
    }
}
