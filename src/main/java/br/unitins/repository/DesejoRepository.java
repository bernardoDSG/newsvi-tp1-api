package br.unitins.repository;

import java.util.List;

import br.unitins.model.Desejo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DesejoRepository implements PanacheRepository<Desejo> {

    public List<Desejo> listByUsuarioLogin(String login) {
        return list("usuario.login", login);
    }

    public long countByUsuarioAndSessaoId(Long usuarioId, Long sessaoId) {
        return count("usuario.id = ?1 and sessao.id = ?2", usuarioId, sessaoId);
    }
}

