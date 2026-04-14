package br.unitins.repository;

import br.unitins.model.Endereco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoRepository implements PanacheRepository<Endereco> {
    
    public Endereco findByCep(String cep) {
        return find("cep", cep).firstResult();
    }
}