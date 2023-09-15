package br.luahr.topicos1.dto;

import br.luahr.topicos1.model.Compra;

public record CompraResponseDTO(
    Long id,
    UsuarioResponseDTO usuario,
    FlorResponseDTO produto,
    Integer quantidadeProduto,
    Double totalCompra
){
    public CompraResponseDTO(Compra compra){
        this(
            compra.getId(),
            new UsuarioResponseDTO(compra.getUsuario()),
            new FlorResponseDTO(compra.getItemProduto()),
            compra.getQuantidadeProduto(),
            compra.getTotalCompra());
    }
}