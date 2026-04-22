package br.unitins.service;

import java.util.List;

import br.unitins.exception.ValidationException;
import br.unitins.model.Premio;
import br.unitins.repository.PremioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PremioServiceImpl implements PremioService {

    @Inject
    PremioRepository repository;

    @Override
    @Transactional
    public Premio create(Premio premio) {
        if (premio.getNome() == null || premio.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do prêmio é obrigatório", "nome");
        }
        if (premio.getAno() == null) {
            throw new ValidationException("Ano é obrigatório", "ano");
        }
        repository.persist(premio);
        return premio;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
    public Premio findById(Long id) {
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
    public List<Premio> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }

    @Override
    public List<Premio> findByCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        return repository.findByCategoria(categoria).list();
    }

    @Override
    @Transactional
    public void update(Long id, Premio premio) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Premio existente = findById(id);

        if (premio.getNome() != null) {
            if (premio.getNome().trim().isEmpty()) {
                throw new ValidationException("Nome do prêmio é obrigatório", "nome");
            }
            existente.setNome(premio.getNome());
        }
        if (premio.getAno() != null) {
            existente.setAno(premio.getAno());
        }
        if (premio.getCategoria() != null) {
            existente.setCategoria(premio.getCategoria());
        }
    }
}
