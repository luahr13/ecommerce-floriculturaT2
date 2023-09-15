package br.luahr.topicos1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Compra extends DefaultEntity{
    private Double totalCompra;

    @OneToOne
    private Usuario usuario;

    @OneToOne
    private Flor itemProduto;

    private Integer quantidadeProduto;

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(Integer quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public Flor getItemProduto() {
        return itemProduto;
    }

    public void setItemProduto(Flor itemProduto) {
        this.itemProduto = itemProduto;
    }

}
