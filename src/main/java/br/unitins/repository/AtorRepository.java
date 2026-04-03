package br.unitins.repository;

import br.unitins.model.Ator;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AtorRepository implements PanacheRepository<Ator> {
    
    public PanacheQuery<Ator> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public PanacheQuery<Ator> findByPremio(String premio) {
        return find("SELECT a FROM Ator a JOIN a.premios p WHERE UPPER(p.nome) LIKE UPPER(?1)", "%" + premio + "%");
    }
}