package br.luahr.topicos1.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Estado extends DefaultEntity{
    @Column(length = 50)
    private String nome;
    
    @Column(length = 2)
    private String sigla;

    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

}
