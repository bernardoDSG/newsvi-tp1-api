package br.unitins.repository;

import br.unitins.model.Sessao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {
    
    public PanacheQuery<Sessao> findByTipoSessao(Long idTipoSessao) {
        return find("idTipoSessao = ?1", idTipoSessao);
    }

    public PanacheQuery<Sessao> findBySala (Long idSala) {
        return find("idSala = ?1", idSala);
    }

    public PanacheQuery<Sessao> findByFilme (Long idFilme) {
        return find("idFilme = ?1", idFilme);
    }

    
}
