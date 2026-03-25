package br.unitins.service;

import java.util.List;

import br.unitins.model.FilmePremiado;

public interface FilmePremiadoService {
    List<FilmePremiado> findAll();
    FilmePremiado findById(Long id);
    List<FilmePremiado> findByNome (String nome);
    List<FilmePremiado> findByAnoLancamento(Integer anoLancamento);
    List<FilmePremiado> findByIdiomaOriginal(String idiomaOriginal);
    List<FilmePremiado> findByPremio(Long idPremio);
    FilmePremiado create(FilmePremiado filmePremiado);
    void update(Long id, FilmePremiado filmePremiado);
    void delete(Long id);
}
