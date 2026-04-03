package br.unitins.service;

import java.util.List;

import br.unitins.model.Ator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface AtorService {
    List<Ator> findAll();
    Ator findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Ator> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    Ator create(@Valid Ator ator);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Ator ator);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}