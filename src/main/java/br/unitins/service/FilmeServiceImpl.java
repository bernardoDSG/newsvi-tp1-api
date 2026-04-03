package br.unitins.service;

import java.util.List;

import br.unitins.model.Filme;
import br.unitins.repository.FilmeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FilmeServiceImpl implements FilmeService {

    @Inject
    FilmeRepository repository;

    @Override
    @Transactional
    public Filme create(Filme filme) {
        if (filme.getNome() == null || filme.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do filme é obrigatório");
        }
        repository.persist(filme);
        return filme;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        repository.deleteById(id);
    }

    @Override
    public List<Filme> findAll() {
        return repository.listAll();
    }

    @Override
    public Filme findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Filme filme = repository.findById(id);
        if (filme == null) {
            throw new NotFoundException("Filme não encontrado com ID: " + id);
        }
        return filme;
    }

    @Override
    public List<Filme> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    public List<Filme> findByGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("Gênero não pode ser vazio");
        }
        return repository.findByGenero(genero).list();
    }

    @Override
    public List<Filme> findByAtor(String ator) {
        if (ator == null || ator.trim().isEmpty()) {
            throw new IllegalArgumentException("Ator não pode ser vazio");
        }
        return repository.findByAtor(ator).list();
    }

    @Override
    @Transactional
    public void update(Long id, Filme filme) {
        Filme f = findById(id);
        
        if (filme.getNome() != null && !filme.getNome().trim().isEmpty()) {
            f.setNome(filme.getNome());
        }
        if (filme.getDuracao() != null) {
            f.setDuracao(filme.getDuracao());
        }
        if (filme.getSinopse() != null) {
            f.setSinopse(filme.getSinopse());
        }
        if (filme.getIdiomaOriginal() != null) {
            f.setIdiomaOriginal(filme.getIdiomaOriginal());
        }
        if (filme.getAnoLancamento() != null) {
            f.setAnoLancamento(filme.getAnoLancamento());
        }
        if (filme.getClassificacaoIndicativa() != null) {
            f.setClassificacaoIndicativa(filme.getClassificacaoIndicativa());
        }
        if (filme.getGeneros() != null) {
            f.setGeneros(filme.getGeneros());
        }
        if (filme.getAtores() != null) {
            f.setAtores(filme.getAtores());
        }
    }
}