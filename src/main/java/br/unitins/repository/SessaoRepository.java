package br.unitins.repository;

import java.time.LocalDateTime;

import br.unitins.model.Sessao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {
    
    // Você já deve ter esse método
    public PanacheQuery<Sessao> findByFilme(Long filmeId) {
        return find("filme.id", filmeId);
    }
    
    // NOVO: Buscar por status
    public PanacheQuery<Sessao> findByStatus(Long statusId) {
        return find("status.id", statusId);
    }
    
    // NOVO: Buscar sessões em exibição agora
    public PanacheQuery<Sessao> findSessoesEmExibicao(LocalDateTime agora) {
        return find("inicio <= ?1 AND fim >= ?1 AND status.id = 2", agora);
    }
}