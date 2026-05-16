package br.unitins.repository;

import br.unitins.model.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public PanacheQuery<Pedido> findByUsuarioLogin(String login) {
        return find("usuario.login", login);
    }
}

