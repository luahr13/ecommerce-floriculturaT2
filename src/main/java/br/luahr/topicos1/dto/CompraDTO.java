package br.luahr.topicos1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CompraDTO(
    @NotNull
    @Positive
    Long idCliente,
    @NotNull
    @Positive
    Long idProduto,
    Integer quantidaProduto,
    Double totalCompra
)
{

}
