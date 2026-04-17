package br.unitins.repository;

import br.unitins.model.Poltrona;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PoltronaRepository implements PanacheRepository<Poltrona> {
    
    public PanacheQuery<Poltrona> findByCodigo(String codigo) {
        return find("UPPER(codigo) LIKE UPPER(?1)", "%" + codigo + "%");
    }

    public PanacheQuery<Poltrona> findByDisponibilidade(Long disponibilidadeId) {
        return find("disponibilidade.id", disponibilidadeId);
    }
}