package br.unitins.repository;

import br.unitins.model.FilmePremiado;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class FilmePremiadoRepository implements PanacheRepository<FilmePremiado> {
    
    public PanacheQuery<FilmePremiado> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public PanacheQuery<FilmePremiado> findByAnoLancamento (Integer anoLancamento) {
        return find("anoLancamento", anoLancamento);
    }

    public PanacheQuery<FilmePremiado> findByIdiomaOriginal (String idiomaOriginal) {
        return find("UPPER(idiomaOriginal) LIKE UPPER(?1)", "%" + idiomaOriginal + "%");
    }

    public PanacheQuery<FilmePremiado> findByPremio(Long idPremio) {
        return find("premio.id", idPremio);
    }
}
