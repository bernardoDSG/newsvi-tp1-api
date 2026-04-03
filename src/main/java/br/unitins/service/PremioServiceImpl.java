package br.unitins.service;

import java.util.List;

import br.unitins.model.Premio;
import br.unitins.repository.PremioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PremioServiceImpl implements PremioService {

    @Inject
    PremioRepository repository;

    @Override
    @Transactional
    public Premio create(@Valid Premio premio) {
        if (premio.getNome() == null || premio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do prêmio é obrigatório");
        }
        if (premio.getAno() == null) {
            throw new IllegalArgumentException("Ano é obrigatório");
        }
        repository.persist(premio);
        return premio;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Prêmio não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Premio> findAll() {
        return repository.listAll();
    }

    @Override
    public Premio findById(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Premio premio = repository.findById(id);
        if (premio == null) {
            throw new NotFoundException("Prêmio não encontrado com ID: " + id);
        }
        return premio;
    }

    @Override
    public List<Premio> findByNome(@NotNull(message = "Nome não pode ser nulo") String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Premio premio) {
        Premio p = findById(id);
        
        if (premio.getNome() != null && !premio.getNome().trim().isEmpty()) {
            p.setNome(premio.getNome());
        }
        if (premio.getAno() != null) {
            p.setAno(premio.getAno());
        }
    }
}