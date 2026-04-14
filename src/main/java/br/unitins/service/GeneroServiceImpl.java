package br.unitins.service;

import java.util.List;

import br.unitins.model.Genero;
import br.unitins.repository.GeneroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class GeneroServiceImpl implements GeneroService {

    @Inject
    GeneroRepository repository;

    @Override
    @Transactional
    public Genero create(Genero genero) {
        if (genero.getNome() == null || genero.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do gênero é obrigatório");
        }
        repository.persist(genero);
        return genero;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Gênero não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Genero> findAll() {
        return repository.listAll();
    }

    @Override
    public Genero findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Genero genero = repository.findById(id);
        if (genero == null) {
            throw new NotFoundException("Gênero não encontrado com ID: " + id);
        }
        return genero;
    }

    @Override
    public List<Genero> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(Long id, Genero genero) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Genero g = findById(id);
        if (genero.getNome() != null && !genero.getNome().trim().isEmpty()) {
            g.setNome(genero.getNome());
        }
    }
}