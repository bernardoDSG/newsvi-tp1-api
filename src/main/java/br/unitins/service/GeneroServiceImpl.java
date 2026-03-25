package br.unitins.service;

import java.util.List;

import br.unitins.model.Genero;
import br.unitins.repository.GeneroRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class GeneroServiceImpl implements GeneroService {

    @Inject
    GeneroRepository repository;

    @Override
    public List<Genero> findAll() {
        return repository.listAll();
    }

    @Override
    public Genero findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Genero> findByNome(String nome) {
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public Genero create(Genero genero) {
        repository.persist(genero);
        return genero;
    }

    @Override
    @Transactional
    public void update(Long id, Genero genero) {
        Genero gen = findById(id);
        gen.setNome(genero.getNome());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}