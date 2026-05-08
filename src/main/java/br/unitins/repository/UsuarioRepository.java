package br.unitins.repository;

import java.util.Optional;

import br.unitins.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Optional<Usuario> findByLogin(String login) {
        return find("login", login).firstResultOptional();
    }
}
