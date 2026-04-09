package br.unitins.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.model.Sessao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface SessaoService {
    List<Sessao> findAll();
    Sessao findById(@NotNull(message = "ID não pode ser nulo") Long id);
    Sessao create(@Valid Sessao sessao);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Sessao sessao);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
    
    boolean existsBySalaAndHorario(Long salaId, LocalDateTime inicio, LocalDateTime fim);
}