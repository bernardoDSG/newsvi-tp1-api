package br.unitins.repository;

import br.unitins.model.Filme;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FilmeRepository implements PanacheRepository<Filme> {
    
    public PanacheQuery<Filme> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public PanacheQuery<Filme> findByGenero(String genero) {
        return find("SELECT f FROM Filme f JOIN f.generos g WHERE UPPER(g.nome) LIKE UPPER(?1)", "%" + genero + "%");
    }

    public PanacheQuery<Filme> findByAtor(String ator) {
        return find("SELECT f FROM Filme f JOIN f.atores a WHERE UPPER(a.nome) LIKE UPPER(?1)", "%" + ator + "%");
    }
}