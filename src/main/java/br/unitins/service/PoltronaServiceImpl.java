package br.unitins.service;

import java.util.List;

import br.unitins.model.Poltrona;
import br.unitins.repository.PoltronaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PoltronaServiceImpl implements PoltronaService {

    @Inject
    PoltronaRepository repository;

    @Override
    @Transactional
    public Poltrona create(Poltrona poltrona) {
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
    public void delete(Long id) {
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
    public Poltrona findById(Long id) {
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
    public List<Poltrona> findByCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser vazio");
        }
        return repository.findByCodigo(codigo).list();
    }

    @Override
    public List<Poltrona> findByDisponibilidade(Long disponibilidadeId) {
        if (disponibilidadeId == null) {
            throw new IllegalArgumentException("ID da disponibilidade não pode ser nulo");
        }
        return repository.findByDisponibilidade(disponibilidadeId).list();
    }
    
    @Override
    public List<Poltrona> findBySala(Long salaId) {
        if (salaId == null) {
            throw new IllegalArgumentException("Sala ID não pode ser nulo");
        }
        return repository.findBySala(salaId).list();
    }

    @Override
    @Transactional
    public void update(Long id, Poltrona poltrona) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Poltrona p = findById(id);
        
        if (poltrona.getCodigo() != null && !poltrona.getCodigo().trim().isEmpty()) {
            p.setCodigo(poltrona.getCodigo());
        }
        if (poltrona.getLinha() != null) {
            p.setLinha(poltrona.getLinha());
        }
        if (poltrona.getColuna() != null) {
            p.setColuna(poltrona.getColuna());
        }
        if (poltrona.getSala() != null) {
            p.setSala(poltrona.getSala());
        }
        if (poltrona.getDisponibilidade() != null) {
            p.setDisponibilidade(poltrona.getDisponibilidade());
        }
    }
}