package br.unitins.service;

import java.util.List;

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
            throw new IllegalArgumentException("Número da sala é obrigatório");
        }
        if (sala.getCapacidade() == null || sala.getCapacidade() <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero");
        }
        repository.persist(sala);
        return sala;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
    public List<Sala> findByCinema(Long cinemaId) {
        if (cinemaId == null) {
            throw new IllegalArgumentException("Cinema ID não pode ser nulo");
        }
        return repository.list("cinema.id", cinemaId);
    }

    @Override
    @Transactional
    public void update(Long id, Sala sala) {
        Sala s = findById(id);
        
        if (sala.getNumero() != null) {
            s.setNumero(sala.getNumero());
        }
        if (sala.getCapacidade() != null && sala.getCapacidade() > 0) {
            s.setCapacidade(sala.getCapacidade());
        }
        if (sala.getCinema() != null) {
            s.setCinema(sala.getCinema());
        }
        if (sala.getPoltronas() != null) {
            s.setPoltronas(sala.getPoltronas());
        }
    }
}