package br.unitins.service;

import java.util.List;
import br.unitins.model.Sala;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface SalaService {
    List<Sala> findAll();
    Sala findById(@NotNull(message = "ID não pode ser nulo") Long id);
    Sala findByNumero(@NotNull(message = "Número não pode ser nulo") Integer numero);
    List<Sala> findByCinema(@NotNull(message = "Cinema não pode ser nulo") Long cinemaId);
    Sala create(@Valid Sala sala);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Sala sala);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}