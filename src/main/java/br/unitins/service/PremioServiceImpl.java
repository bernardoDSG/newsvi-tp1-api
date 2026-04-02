package br.unitins.service;

import java.util.List;

import br.unitins.model.Premio;
import br.unitins.repository.PremioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PremioServiceImpl implements PremioService {

    @Inject
    PremioRepository repository;

    @Override
    @Transactional
    public Premio create(Premio premio) {
        repository.persist(premio);
        return premio;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Premio> findAll() {
        return repository.listAll();
    }

    @Override
    public Premio findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Premio> findByNome(String nome) {
        return repository.findByNome(nome).list();
    }

    @Override
    public List<Premio> findByAno(Integer ano) {
        return repository.findByAno(ano).list();
    }

    @Override
    @Transactional
    public void update(Long id, Premio premio) {
        Premio prem = findById(id);
        prem.setNome(premio.getNome());
        prem.setAno(premio.getAno());
    }
}