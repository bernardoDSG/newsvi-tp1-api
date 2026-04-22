package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
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
            throw new ValidationException("Nome do filme é obrigatório", "nome");
        }
        validateDuracao(filme.getDuracao());
        repository.persist(filme);
        return filme;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
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
    public void update(Long id, Filme filme) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Filme existente = findById(id);

        if (filme.getNome() != null) {
            if (filme.getNome().trim().isEmpty()) {
                throw new ValidationException("Nome do filme é obrigatório", "nome");
            }
            existente.setNome(filme.getNome());
        }
        if (filme.getDuracao() != null) {
            validateDuracao(filme.getDuracao());
            existente.setDuracao(filme.getDuracao());
        }
        if (filme.getSinopse() != null) {
            existente.setSinopse(filme.getSinopse());
        }
        if (filme.getIdiomaOriginal() != null) {
            existente.setIdiomaOriginal(filme.getIdiomaOriginal());
        }
        if (filme.getAnoLancamento() != null) {
            existente.setAnoLancamento(filme.getAnoLancamento());
        }
        if (filme.getImagemPoster() != null) {
            existente.setImagemPoster(filme.getImagemPoster());
        }
        if (filme.getTrailerUrl() != null) {
            existente.setTrailerUrl(filme.getTrailerUrl());
        }
        if (filme.getDiretor() != null) {
            existente.setDiretor(filme.getDiretor());
        }
        if (filme.getClassificacaoIndicativa() != null) {
            existente.setClassificacaoIndicativa(filme.getClassificacaoIndicativa());
        }
        if (filme.getGeneros() != null) {
            existente.setGeneros(filme.getGeneros());
        }
        if (filme.getAtores() != null) {
            existente.setAtores(filme.getAtores());
        }
        if (filme.getPremios() != null) {
            existente.setPremios(filme.getPremios());
        }
    }

    private void validateDuracao(String duracao) {
        if (duracao != null && !duracao.trim().isEmpty()) {
            Integer minutos = Filme.converterDuracaoParaMinutos(duracao);
            if (minutos == null || minutos <= 0) {
                throw new ValidationException("Formato de duração inválido", "duracao");
            }
        }
    }
}
