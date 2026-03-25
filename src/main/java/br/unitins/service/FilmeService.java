package br.unitins.service;

import java.util.List;

import br.unitins.model.Filme;

public interface FilmeService {
    List<Filme> findAll();
    Filme findById(Long id);
    List<Filme> findByNome(String nome);
    List<Filme> findByAnoLancamento(Integer anoLancamento);
    List<Filme> findByIdiomaOriginal(String idiomaOriginal);
    Filme create(Filme filme);
    void update(Long id, Filme filme);
    void delete(Long id);
}
