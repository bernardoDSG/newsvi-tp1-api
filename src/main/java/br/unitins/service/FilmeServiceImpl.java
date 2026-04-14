package br.unitins.service;

import java.util.List;
import br.unitins.model.Filme;
import br.unitins.repository.FilmeRepository;
import br.unitins.service.FilmeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class FilmeServiceImpl implements FilmeService {

    @Inject
    FilmeRepository repository;

    @Override
    @Transactional
    public Filme create(@Valid Filme filme) {
        if (filme.getNome() == null || filme.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do filme é obrigatório");
        }
        if (filme.getDuracao() != null && !filme.getDuracao().trim().isEmpty()) {
            Integer minutos = Filme.converterDuracaoParaMinutos(filme.getDuracao());
            if (minutos == null || minutos <= 0) {
                throw new IllegalArgumentException("Formato de duração inválido");
            }
        }
        repository.persist(filme);
        return filme;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Filme não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Filme> findAll() {
        return repository.listAll();
    }

    @Override
    public Filme findById(Long id) {
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
    public List<Filme> findByDiretor(Long diretorId) {
        if (diretorId == null) {
            throw new IllegalArgumentException("Diretor ID não pode ser nulo");
        }
        return repository.findByDiretor(diretorId).list();
    }

    @Override
    public List<Filme> findByDuracaoBetween(Integer minMinutos, Integer maxMinutos) {
        if (minMinutos == null || maxMinutos == null) {
            throw new IllegalArgumentException("Min e Max minutos são obrigatórios");
        }
        if (minMinutos > maxMinutos) {
            throw new IllegalArgumentException("Min não pode ser maior que Max");
        }
        return repository.findByDuracaoBetween(minMinutos, maxMinutos).list();
    }

    @Override
    @Transactional
    public void update(Long id, @Valid Filme filme) {
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
        if (filme.getImagemPoster() != null) {
            f.setImagemPoster(filme.getImagemPoster());
        }
        if (filme.getTrailerUrl() != null) {
            f.setTrailerUrl(filme.getTrailerUrl());
        }
        if (filme.getDiretor() != null) {
            f.setDiretor(filme.getDiretor());
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
        if (filme.getPremios() != null) {
            f.setPremios(filme.getPremios());
        }
    }
}