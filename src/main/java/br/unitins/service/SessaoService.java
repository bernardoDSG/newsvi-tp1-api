package br.unitins.service;

import java.util.List;

import br.unitins.model.Sessao;

public interface SessaoService {
    List<Sessao> findAll();
    Sessao findById(Long id);
    List<Sessao> findByTipoSessao(Long idTipo);
    List<Sessao> findBySala(Long idSala);
    List<Sessao> findByFilme(Long idFilme);
    Sessao create(Sessao sessao);
    void update(Long id, Sessao sessao);
    void delete(Long id);
}
