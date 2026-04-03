package br.unitins.service;

import java.util.List;

import br.unitins.model.Genero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GeneroService {
    List<Genero> findAll();
    Genero findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Genero> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    Genero create(@Valid Genero genero);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Genero genero);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}