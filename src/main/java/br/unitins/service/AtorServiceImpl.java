package br.unitins.service;

import java.util.List;

import br.unitins.model.Ator;
import br.unitins.repository.AtorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AtorServiceImpl implements AtorService {

    @Inject
    AtorRepository repository;

    @Override
    @Transactional
    public Ator create(@Valid Ator ator) {
        if (ator.getNome() == null || ator.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do ator é obrigatório");
        }
        repository.persist(ator);
        return ator;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Ator não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Ator> findAll() {
        return repository.listAll();
    }

    @Override
    public Ator findById(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Ator ator = repository.findById(id);
        if (ator == null) {
            throw new NotFoundException("Ator não encontrado com ID: " + id);
        }
        return ator;
    }

    @Override
    public List<Ator> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Ator ator) {
        Ator a = findById(id);
        
        if (ator.getNome() != null && !ator.getNome().trim().isEmpty()) {
            a.setNome(ator.getNome());
        }
        if (ator.getPremios() != null) {
            a.setPremios(ator.getPremios());
        }
    }
}