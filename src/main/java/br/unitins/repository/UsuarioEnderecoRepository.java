package br.unitins.repository;

import java.util.List;

import br.unitins.model.UsuarioEndereco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioEnderecoRepository implements PanacheRepository<UsuarioEndereco> {

    public List<UsuarioEndereco> listByUsuarioLogin(String login) {
        return list("usuario.login", login);
    }
}

