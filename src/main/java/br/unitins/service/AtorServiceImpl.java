package br.unitins.service;

import java.util.List;

import br.unitins.model.Ator;
import br.unitins.repository.AtorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtorServiceImpl implements AtorService {

    @Inject
    AtorRepository repository;

    @Override
    @Transactional
    public Ator create(Ator ator) {
        repository.persist(ator);
        return ator;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public List<Ator> findAll() {
        return repository.listAll();
    }

    @Override
    public Ator findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Ator> findByNome(String nome) {
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(Long id, Ator ator) {
        Ator at = findById(id);

        at.setNome(ator.getNome());

    }

}
