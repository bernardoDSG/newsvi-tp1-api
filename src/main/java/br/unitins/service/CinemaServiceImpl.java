package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
import br.unitins.model.Cinema;
import br.unitins.repository.CinemaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CinemaServiceImpl implements CinemaService {

    @Inject
    CinemaRepository repository;

    @Override
    @Transactional
    public Cinema create(Cinema cinema) {
        if (cinema.getNome() == null || cinema.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do cinema é obrigatório", "nome");
        }
        if (cinema.getCnpj() == null || cinema.getCnpj().trim().isEmpty()) {
            throw new ValidationException("CNPJ do cinema é obrigatório", "cnpj");
        }
        repository.persist(cinema);
        return cinema;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Cinema não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Cinema> findAll() {
        return repository.listAll();
    }

    @Override
    public Cinema findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Cinema cinema = repository.findById(id);
        if (cinema == null) {
            throw new NotFoundException("Cinema não encontrado com ID: " + id);
        }
        return cinema;
    }

    @Override
    public List<Cinema> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    public List<Cinema> findByCidade(String cidade) {
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade não pode ser vazia");
        }
        return repository.findByCidade(cidade).list();
    }

    @Override
    @Transactional
    public void update(Long id, Cinema cinema) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Cinema existente = findById(id);

        if (cinema.getNome() != null) {
            if (cinema.getNome().trim().isEmpty()) {
                throw new ValidationException("Nome do cinema é obrigatório", "nome");
            }
            existente.setNome(cinema.getNome());
        }
        if (cinema.getCnpj() != null) {
            if (cinema.getCnpj().trim().isEmpty()) {
                throw new ValidationException("CNPJ do cinema é obrigatório", "cnpj");
            }
            existente.setCnpj(cinema.getCnpj());
        }
        if (cinema.getTelefone() != null) {
            existente.setTelefone(cinema.getTelefone());
        }
        if (cinema.getEndereco() != null) {
            existente.setEndereco(cinema.getEndereco());
        }
        if (cinema.getSalas() != null) {
            existente.setSalas(cinema.getSalas());
        }
    }
}
