package br.unitins.repository;

import br.unitins.model.Cinema;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CinemaRepository implements PanacheRepository<Cinema> {
    
    public PanacheQuery<Cinema> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }
    
    public PanacheQuery<Cinema> findByCidade(String cidade) {
        return find("endereco.cidade = ?1", cidade);
    }
}