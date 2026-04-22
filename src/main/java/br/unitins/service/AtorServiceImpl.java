package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
import br.unitins.model.Ator;
import br.unitins.repository.AtorRepository;
import br.unitins.repository.PremioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class AtorServiceImpl implements AtorService {

    @Inject
    AtorRepository repository;

    @Inject
    PremioRepository premioRepository;

    @Override
    @Transactional
    public Ator create(Ator ator) {
        if (ator.getNome() == null || ator.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do ator é obrigatório", "nome");
        }
        repository.persist(ator);
        return ator;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
    public Ator findById(Long id) {
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
    public List<Ator> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    public List<Ator> findByPremio(String premio) {
        if (premio == null || premio.trim().isEmpty()) {
            throw new IllegalArgumentException("Prêmio não pode ser vazio");
        }
        return repository.findByPremio(premio).list();
    }

    @Override
    @Transactional
    public void update(Long id, Ator ator) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Ator existente = findById(id);

        if (ator.getNome() != null) {
            if (ator.getNome().trim().isEmpty()) {
                throw new ValidationException("Nome do ator é obrigatório", "nome");
            }
            existente.setNome(ator.getNome());
        }

        if (ator.getDataNascimento() != null) {
            existente.setDataNascimento(ator.getDataNascimento());
        }
        if (ator.getNacionalidade() != null) {
            existente.setNacionalidade(ator.getNacionalidade());
        }
        if (ator.getUrlFoto() != null) {
            existente.setUrlFoto(ator.getUrlFoto());
        }
        if (ator.getPremios() != null) {
            existente.setPremios(ator.getPremios());
        }
    }
}
