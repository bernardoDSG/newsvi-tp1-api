package br.unitins.service;

import java.time.LocalDateTime;
import java.util.List;
import br.unitins.model.Sessao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface SessaoService {
    List<Sessao> findAll();
    Sessao findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Sessao> findByFilme(@NotNull(message = "Filme não pode ser nulo") Long filmeId);
    List<Sessao> findByCinema(@NotNull(message = "Cinema não pode ser nulo") Long cinemaId);
    List<Sessao> findByStatus(@NotNull(message = "Status não pode ser nulo") Long statusId);
    List<Sessao> findSessoesEmExibicao(LocalDateTime agora);
    boolean existsBySalaAndHorario(Long salaId, LocalDateTime inicio, LocalDateTime fim, Long sessaoId);
    Sessao create(@Valid Sessao sessao);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Sessao sessao);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}