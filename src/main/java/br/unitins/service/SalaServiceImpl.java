package br.unitins.service;

import java.util.List;

import br.unitins.model.Sala;
import br.unitins.repository.SalaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SalaServiceImpl implements SalaService {

    @Inject
    SalaRepository repository;

    @Override
    @Transactional
    public Sala create(@Valid Sala sala) {
        if (sala.getPoltronas() == null) {
            throw new IllegalArgumentException("Poltronas são obrigatórias");
        }
        repository.persist(sala);
        return sala;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Sala não encontrada com ID: " + id);
        }
    }

    @Override
    public List<Sala> findAll() {
        return repository.listAll();
    }

    @Override
    public Sala findById(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Sala sala = repository.findById(id);
        if (sala == null) {
            throw new NotFoundException("Sala não encontrada com ID: " + id);
        }
        return sala;
    }

    @Override
    @Transactional
    public void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Sala sala) {
        Sala s = findById(id);
        
        if (sala.getPoltronas() != null) {
            s.setPoltronas(sala.getPoltronas());
        }
    }
}