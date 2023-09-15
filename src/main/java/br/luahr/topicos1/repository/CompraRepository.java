package br.luahr.topicos1.repository;

import java.util.List;

import br.luahr.topicos1.model.Compra;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompraRepository implements PanacheRepository<Compra>{
    public List<Compra> findByNome(String nome){
        if (nome == null)
            return null;
        return find("UPPER(nome) LIKE ?1 ", "%"+nome.toUpperCase()+"%").list();
    }
}
