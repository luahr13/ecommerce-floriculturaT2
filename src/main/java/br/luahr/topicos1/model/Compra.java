package br.luahr.topicos1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Compra extends DefaultEntity{
    private Double totalCompra;

    @OneToOne
    private Cliente cliente;

    @OneToOne
    private Flor itemProduto;

    private Integer quantidadeProduto;

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
