package br.unitins.service;

import java.util.List;

import br.unitins.model.Filme;
import br.unitins.repository.FilmeRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class FilmeServiceImpl implements FilmeService {

    @Inject
    FilmeRepository repository;

    @Override
    @Transactional
    public Filme create(Filme filme) {

        repository.persist(filme);
        return filme;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public List<Filme> findAll() {
        return repository.listAll();
    }

    @Override
    public List<Filme> findByAnoLancamento(Integer anoLancamento) {

        return repository.findByAnoLancamento(anoLancamento).list();
    }

    @Override
    public Filme findById(Long id) {

        return repository.findById(id);
    }

    @Override
    public List<Filme> findByIdiomaOriginal(String idiomaOriginal) {
        return repository.findByIdiomaOriginal(idiomaOriginal).list();
    }

    @Override
    public List<Filme> findByNome(String nome) {

        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(Long id, Filme filme) {
        Filme fil = findById(id);
        fil.setNome(filme.getNome());
        fil.setAnoLancamento(filme.getAnoLancamento());
        fil.setIdiomaOriginal(filme.getIdiomaOriginal());

    }

}
