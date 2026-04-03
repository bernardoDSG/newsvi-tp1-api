package br.unitins.service;

import java.util.List;

import br.unitins.model.Sessao;
import br.unitins.repository.SessaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SessaoServiceImpl implements SessaoService {

    @Inject
    SessaoRepository repository;

    @Override
    @Transactional
    public Sessao create(@Valid Sessao sessao) {
        if (sessao.getInicio() == null) {
            throw new IllegalArgumentException("Horário de início é obrigatório");
        }
        if (sessao.getFim() == null) {
            throw new IllegalArgumentException("Horário de fim é obrigatório");
        }
        if (sessao.getInicio().isAfter(sessao.getFim())) {
            throw new IllegalArgumentException("Horário de início não pode ser após o horário de fim");
        }
        if (sessao.getFilme() == null) {
            throw new IllegalArgumentException("Filme é obrigatório");
        }
        if (sessao.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de sessão é obrigatório");
        }
        repository.persist(sessao);
        return sessao;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Sessão não encontrada com ID: " + id);
        }
    }

    @Override
    public List<Sessao> findAll() {
        return repository.listAll();
    }

    @Override
    public Sessao findById(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Sessao sessao = repository.findById(id);
        if (sessao == null) {
            throw new NotFoundException("Sessão não encontrada com ID: " + id);
        }
        return sessao;
    }

    @Override
    @Transactional
    public void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Sessao sessao) {
        Sessao s = findById(id);
        
        if (sessao.getInicio() != null) {
            s.setInicio(sessao.getInicio());
        }
        if (sessao.getFim() != null) {
            s.setFim(sessao.getFim());
        }
        if (sessao.getFilme() != null) {
            s.setFilme(sessao.getFilme());
        }
        if (sessao.getTipo() != null) {
            s.setTipo(sessao.getTipo());
        }
        if (sessao.getSalas() != null) {
            s.setSalas(sessao.getSalas());
        }
        
        if (s.getInicio() != null && s.getFim() != null && s.getInicio().isAfter(s.getFim())) {
            throw new IllegalArgumentException("Horário de início não pode ser após o horário de fim");
        }
    }
}