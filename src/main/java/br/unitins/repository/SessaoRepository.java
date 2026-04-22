package br.unitins.repository;

import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {
    
    public PanacheQuery<Sessao> findByFilme(Long filmeId) {
        return find("filme.id", filmeId);
    }
    
    public PanacheQuery<Sessao> findByCinema(Long cinemaId) {
        return find("cinema.id", cinemaId);
    }
    
    public PanacheQuery<Sessao> findByStatus(Long statusId) {
        StatusSessao status = StatusSessao.valueOf(statusId);
        return find("status", status);
    }
    
    public PanacheQuery<Sessao> findSessoesEmExibicao(LocalDateTime agora) {
        return find("inicio <= ?1 AND fim >= ?1 AND status = ?2", agora, StatusSessao.EM_EXIBICAO);
    }
    
    public long countConflitoHorario(Long salaId, LocalDateTime inicio, LocalDateTime fim, Long sessaoId) {
        String query = "SELECT COUNT(s) FROM Sessao s JOIN s.salas sala " +
                       "WHERE sala.id = ?1 AND " +
                       "((s.inicio BETWEEN ?2 AND ?3) OR " +
                       "(s.fim BETWEEN ?2 AND ?3) OR " +
                       "(?2 BETWEEN s.inicio AND s.fim))";
        if (sessaoId != null) {
            query += " AND s.id != ?4";
            return find(query, salaId, inicio, fim, sessaoId).count();
        }
        return find(query, salaId, inicio, fim).count();
    }
}
