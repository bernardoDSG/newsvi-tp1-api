package br.unitins.repository;

import br.unitins.model.Sala;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SalaRepository implements PanacheRepository<Sala> {
    
    public Sala findByNumero(Integer numero) {
        return find("numero", numero).firstResult();
    }
    
    public long countByCinema(Long cinemaId) {
        return find("cinema.id", cinemaId).count();
    }
}