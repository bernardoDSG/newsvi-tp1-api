package br.unitins.repository;
import br.unitins.model.AtorPremiado;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AtorPremiadoRepository implements PanacheRepository<AtorPremiado>{
    
    public PanacheQuery<AtorPremiado> findByNome(String nome) {
        return find("UPPER(nome) LIKE UPPER(?1)", "%" + nome + "%");
    }

    public PanacheQuery<AtorPremiado> findByPremio(Long idPremio) {
        return find("premio.id", idPremio);
    }
}
