package br.unitins.service;

import java.util.List;

import br.unitins.model.Sala;
import br.unitins.repository.SalaRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class SalaServiceImpl implements SalaService {

    @Inject
    SalaRepository repository;

    @Override
    @Transactional
    public Sala create(Sala sala) {
        repository.persist(sala);
        return sala;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
        
    }

    @Override
    public List<Sala> findAll() {
        return repository.listAll();
    }

    @Override
    public Sala findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void update(Long id, Sala sala) {
        Sala s = findById(id);
        s.setPoltronas(sala.getPoltronas());
    }
    
}
