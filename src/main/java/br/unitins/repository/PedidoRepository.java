package br.unitins.repository;

import java.util.List;
import java.util.Optional;

import br.unitins.model.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public PanacheQuery<Pedido> findByUsuarioLogin(String login) {
        return find("usuario.login", login);
    }

    public List<Pedido> listAllWithDetails() {
        return find("""
            select distinct p
            from Pedido p
            left join fetch p.usuario
            left join fetch p.itens i
            left join fetch i.sessao s
            left join fetch s.filme
            left join fetch s.cinema
            left join fetch i.poltrona
            """).list();
    }

    public Optional<Pedido> findByIdWithDetails(Long id) {
        return find("""
            select distinct p
            from Pedido p
            left join fetch p.usuario
            left join fetch p.itens i
            left join fetch i.sessao s
            left join fetch s.filme
            left join fetch s.cinema
            left join fetch i.poltrona
            where p.id = ?1
            """, id).firstResultOptional();
    }

    public List<Pedido> findByUsuarioLoginWithDetails(String login) {
        return find("""
            select distinct p
            from Pedido p
            left join fetch p.usuario u
            left join fetch p.itens i
            left join fetch i.sessao s
            left join fetch s.filme
            left join fetch s.cinema
            left join fetch i.poltrona
            where u.login = ?1
            """, login).list();
    }
}
