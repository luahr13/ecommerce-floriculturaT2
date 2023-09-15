package br.luahr.topicos1.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import br.luahr.topicos1.model.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TelefoneRepository implements PanacheRepository<Telefone>{
    public List<Telefone> findByNome(String nome){
        if (nome == null)
            return null;
        return find("UPPER(nome) LIKE ?1 ", "%"+nome.toUpperCase()+"%").list();
    }
}
