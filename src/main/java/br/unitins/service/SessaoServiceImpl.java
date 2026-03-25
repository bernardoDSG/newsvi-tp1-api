package br.unitins.service;

import java.util.List;

import br.unitins.model.Sessao;
import br.unitins.repository.SessaoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class SessaoServiceImpl implements SessaoService {

    @Inject
    SessaoRepository repository;

    @Override
    public List<Sessao> findAll() {
        return repository.listAll();
    }

    @Override
    public Sessao findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Sessao> findByTipoSessao(Long idTipo) {
        return repository.findByTipoSessao(idTipo).list();
    }

    @Override
    public List<Sessao> findBySala(Long idSala) {
        return repository.findBySala(idSala).list();
    }

    @Override
    public List<Sessao> findByFilme(Long idFilme) {
        return repository.findByFilme(idFilme).list();
    }

    @Override
    @Transactional
    public Sessao create(Sessao sessao) {
        repository.persist(sessao);
        return sessao;
    }

    @Override
    @Transactional
    public void update(Long id, Sessao sessao) {
        Sessao s = findById(id);
        s.setTipo(sessao.getTipo());
        s.setSalas(sessao.getSalas());
        s.setFilme(sessao.getFilme());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
