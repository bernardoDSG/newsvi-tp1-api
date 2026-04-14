package br.unitins.service;

import java.util.List;
import br.unitins.model.Diretor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DiretorService {
    List<Diretor> findAll();
    Diretor findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Diretor> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    List<Diretor> findByNacionalidade(@NotNull(message = "Nacionalidade não pode ser nula") String nacionalidade);
    Diretor create(@Valid Diretor diretor);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Diretor diretor);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}