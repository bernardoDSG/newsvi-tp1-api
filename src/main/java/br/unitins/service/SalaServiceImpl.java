package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
import br.unitins.model.Sala;
import br.unitins.repository.SalaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SalaServiceImpl implements SalaService {

    @Inject
    SalaRepository repository;

    @Override
    @Transactional
    public Sala create(Sala sala) {
        if (sala.getNumero() == null) {
            throw new ValidationException("Número da sala é obrigatório", "numero");
        }
        if (sala.getCapacidade() == null || sala.getCapacidade() <= 0) {
            throw new ValidationException("Capacidade deve ser maior que zero", "capacidade");
        }
        repository.persist(sala);
        return sala;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
    public Sala findById(Long id) {
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
    public Sala findByNumero(Integer numero) {
        if (numero == null) {
            throw new IllegalArgumentException("Número não pode ser nulo");
        }
        Sala sala = repository.findByNumero(numero);
        if (sala == null) {
            throw new NotFoundException("Sala não encontrada com número: " + numero);
        }
        return sala;
    }

    @Override
    @Transactional
    public void update(Long id, Sala sala) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Sala existente = findById(id);

        if (sala.getNumero() != null) {
            existente.setNumero(sala.getNumero());
        }
        if (sala.getCapacidade() != null) {
            if (sala.getCapacidade() <= 0) {
                throw new ValidationException("Capacidade deve ser maior que zero", "capacidade");
            }
            existente.setCapacidade(sala.getCapacidade());
        }
        if (sala.getPoltronas() != null) {
            existente.setPoltronas(sala.getPoltronas());
        }
    }
}
