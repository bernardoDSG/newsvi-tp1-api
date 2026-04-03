package br.unitins.service;

import java.util.List;

import br.unitins.model.Poltrona;
import br.unitins.repository.PoltronaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PoltronaServiceImpl implements PoltronaService {

    @Inject
    PoltronaRepository repository;

    @Override
    @Transactional
    public Poltrona create(@Valid Poltrona poltrona) {
        if (poltrona.getCodigo() == null || poltrona.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da poltrona é obrigatório");
        }
        if (poltrona.getDisponibilidade() == null) {
            throw new IllegalArgumentException("Disponibilidade é obrigatória");
        }
        repository.persist(poltrona);
        return poltrona;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Poltrona não encontrada com ID: " + id);
        }
    }

    @Override
    public List<Poltrona> findAll() {
        return repository.listAll();
    }

    @Override
    public Poltrona findById(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Poltrona poltrona = repository.findById(id);
        if (poltrona == null) {
            throw new NotFoundException("Poltrona não encontrada com ID: " + id);
        }
        return poltrona;
    }

    @Override
    public List<Poltrona> findByCodigo(@NotNull(message = "Código não pode ser nulo") String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser vazio");
        }
        return repository.findByCodigo(codigo).list();
    }

    @Override
    public List<Poltrona> findByDisponibilidade(@NotNull(message = "ID da disponibilidade não pode ser nulo") Long disponibilidadeId) {
        if (disponibilidadeId == null) {
            throw new IllegalArgumentException("ID da disponibilidade não pode ser nulo");
        }
        return repository.findByDisponibilidade(disponibilidadeId).list();
    }

    @Override
    @Transactional
    public void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Poltrona poltrona) {
        Poltrona p = findById(id);
        
        if (poltrona.getCodigo() != null && !poltrona.getCodigo().trim().isEmpty()) {
            p.setCodigo(poltrona.getCodigo());
        }
        if (poltrona.getDisponibilidade() != null) {
            p.setDisponibilidade(poltrona.getDisponibilidade());
        }
    }
}