package br.unitins.repository;

import br.unitins.model.Diretor;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiretorRepository implements PanacheRepository<Diretor> {
    
    public PanacheQuery<Diretor> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }
    
    public PanacheQuery<Diretor> findByNacionalidade(String nacionalidade) {
        return find("UPPER(nacionalidade) LIKE UPPER(?1)", "%" + nacionalidade + "%");
    }
}