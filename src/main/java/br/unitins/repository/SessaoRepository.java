package br.unitins.repository;

import br.unitins.model.Sessao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {
}