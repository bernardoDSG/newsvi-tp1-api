package br.unitins.service;

import java.util.List;

import br.unitins.model.Cinema;
import br.unitins.repository.CinemaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CinemaServiceImpl implements CinemaService {

    @Inject
    CinemaRepository repository;

    @Override
    @Transactional
    public Cinema create(@Valid Cinema cinema) {
        if (cinema.getNome() == null || cinema.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cinema é obrigatório");
        }
        if (cinema.getCnpj() == null || cinema.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ do cinema é obrigatório");
        }
        repository.persist(cinema);
        return cinema;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
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
    public void update(Long id, @Valid Cinema cinema) {
        Cinema c = findById(id);
        
        if (cinema.getNome() != null && !cinema.getNome().trim().isEmpty()) {
            c.setNome(cinema.getNome());
        }
        if (cinema.getCnpj() != null && !cinema.getCnpj().trim().isEmpty()) {
            c.setCnpj(cinema.getCnpj());
        }
        if (cinema.getTelefone() != null) {
            c.setTelefone(cinema.getTelefone());
        }
        if (cinema.getEndereco() != null) {
            c.setEndereco(cinema.getEndereco());
        }
    }
}