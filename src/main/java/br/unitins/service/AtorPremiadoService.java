package br.unitins.service;

import java.util.List;
import br.unitins.model.AtorPremiado;

public interface AtorPremiadoService {
    List<AtorPremiado> findAll();
    AtorPremiado findById(Long id);
    List<AtorPremiado> findByNome(String nome);
    AtorPremiado create(AtorPremiado ator);
    List<AtorPremiado> findByPremio(Long idPremio);
    void update(Long id, AtorPremiado ator);
    void delete(Long id);
}
