package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
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
            throw new ValidationException("Código da poltrona é obrigatório", "codigo");
        }
        if (poltrona.getDisponibilidade() == null) {
            throw new ValidationException("Disponibilidade é obrigatória", "disponibilidadeId");
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
    @Transactional
    public void update(Long id, Poltrona poltrona) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Poltrona existente = findById(id);

        if (poltrona.getCodigo() != null) {
            if (poltrona.getCodigo().trim().isEmpty()) {
                throw new ValidationException("Código da poltrona é obrigatório", "codigo");
            }
            existente.setCodigo(poltrona.getCodigo());
        }
        if (poltrona.getLinha() != null) {
            existente.setLinha(poltrona.getLinha());
        }
        if (poltrona.getColuna() != null) {
            existente.setColuna(poltrona.getColuna());
        }
        if (poltrona.getDisponibilidade() != null) {
            existente.setDisponibilidade(poltrona.getDisponibilidade());
        }
    }
}
