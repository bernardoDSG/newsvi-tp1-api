package br.unitins.service;

import java.util.List;
import br.unitins.model.Poltrona;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface PoltronaService {
    List<Poltrona> findAll();
    Poltrona findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Poltrona> findByCodigo(@NotNull(message = "Código não pode ser nulo") String codigo);
    List<Poltrona> findByDisponibilidade(@NotNull(message = "Disponibilidade não pode ser nula") Long disponibilidadeId);
    Poltrona create(@Valid Poltrona poltrona);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Poltrona poltrona);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}