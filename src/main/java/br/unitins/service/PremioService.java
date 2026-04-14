package br.unitins.service;

import java.util.List;
import br.unitins.model.Premio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface PremioService {
    List<Premio> findAll();
    Premio findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Premio> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    List<Premio> findByCategoria(@NotNull(message = "Categoria não pode ser nula") String categoria);
    Premio create(@Valid Premio premio);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Premio premio);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}