package br.unitins.service;

import java.util.List;

import br.unitins.model.AtorPremiado;
import br.unitins.repository.AtorPremiadoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class AtorPremiadoServiceImpl implements AtorPremiadoService {

    @Inject
    AtorPremiadoRepository repository;

    @Override
    @Transactional
    public AtorPremiado create(AtorPremiado ator) {
        repository.persist(ator);
        return ator;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
        
    }

    @Override
    public List<AtorPremiado> findAll() {
        return repository.listAll();
    }

    @Override
    public AtorPremiado findById(Long id) {
        
        return repository.findById(id);
    }

    @Override
    public List<AtorPremiado> findByNome(String nome) {
        
        return repository.findByNome(nome).list();
    }

    @Override
    public List<AtorPremiado> findByPremio(Long idPremio) {
    
        return repository.findByPremio(idPremio).list();
    }

    @Override
    @Transactional
    public void update(Long id, AtorPremiado ator) {
        AtorPremiado at = findById(id);

        at.setNome(ator.getNome());
        at.setPremiacao(ator.getPremiacao());
        
        
    }
    
}
