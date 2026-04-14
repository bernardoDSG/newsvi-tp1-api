package br.unitins.service;

import java.util.List;
import br.unitins.model.Filme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface FilmeService {
    List<Filme> findAll();
    Filme findById(@NotNull(message = "ID não pode ser nulo") Long id);
    List<Filme> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome);
    List<Filme> findByGenero(@NotNull(message = "Gênero não pode ser nulo") String genero);
    List<Filme> findByAtor(@NotNull(message = "Ator não pode ser nulo") String ator);
    List<Filme> findByDiretor(@NotNull(message = "Diretor não pode ser nulo") Long diretorId);
    List<Filme> findByDuracaoBetween(Integer minMinutos, Integer maxMinutos);
    Filme create(@Valid Filme filme);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Filme filme);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}