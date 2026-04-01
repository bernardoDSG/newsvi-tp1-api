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

    public PanacheQuery<Filme> findByAnoLancamento (Integer anoLancamento) {
        return find("anoLancamento", anoLancamento);
    }

    public PanacheQuery<Filme> findByIdiomaOriginal (String idiomaOriginal) {
        return find("UPPER(idiomaOriginal) LIKE UPPER(?1)", "%" + idiomaOriginal + "%");
    }

    public PanacheQuery<Filme> findByClassificacaoIndicativa (Long idClassificacaoIndicativa) {
        return find("classificacaoIndicativa.id", idClassificacaoIndicativa);
    }

    public PanacheQuery<Filme> findByGenero(Long idGenero) {
        return find("generos.id", idGenero);
    }

    public PanacheQuery<Filme> findByAtor(Long idAtor) {
        return find("atores.id", idAtor);
    }
    
}
