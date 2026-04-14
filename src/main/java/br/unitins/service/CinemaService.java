package br.unitins.service;

import java.util.List;
import br.unitins.model.Cinema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CinemaService {
    List<Cinema> findAll();
    Cinema findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Cinema> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    List<Cinema> findByCidade(@NotNull(message = "Cidade não pode ser nula") String cidade);
    Cinema create(@Valid Cinema cinema);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Cinema cinema);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}