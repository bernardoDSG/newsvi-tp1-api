package br.unitins.service;

import java.util.List;

import br.unitins.model.Genero;

public interface GeneroService {
    List<Genero> findAll();
    Genero findById(Long id);
    List<Genero> findByNome(String nome);
    Genero create(Genero genero);
    void update(Long id, Genero genero);
    void delete(Long id);
}
