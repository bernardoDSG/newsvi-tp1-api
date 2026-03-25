package br.unitins.service;

import java.util.List;

import br.unitins.model.Ator;

public interface AtorService {
    List<Ator> findAll();
    Ator findById(Long id);
    List<Ator> findByNome(String nome);
    Ator create(Ator ator);
    void update(Long id, Ator ator);
    void delete(Long id);
}
