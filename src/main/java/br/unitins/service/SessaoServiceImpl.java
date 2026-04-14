package br.unitins.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.repository.SessaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SessaoServiceImpl implements SessaoService {

    @Inject
    SessaoRepository repository;

    @Override
    @Transactional
    public Sessao create(Sessao sessao) {
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
        if (sessao.getPreco() == null || sessao.getPreco() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (sessao.getCapacidadeTotal() == null || sessao.getCapacidadeTotal() <= 0) {
            throw new IllegalArgumentException("Capacidade total deve ser maior que zero");
        }
        
        if (sessao.getStatus() == null) {
            sessao.setStatus(StatusSessao.EM_BREVE);
        }

        if (sessao.getCapacidadeDisponivel() == null) {
            sessao.setCapacidadeDisponivel(sessao.getCapacidadeTotal());
        }

        repository.persist(sessao);
        return sessao;
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
    public Sessao findById(Long id) {
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
    public List<Sessao> findByFilme(Long filmeId) {
        if (filmeId == null) {
            throw new IllegalArgumentException("Filme ID não pode ser nulo");
        }
        return repository.findByFilme(filmeId).list();
    }
    
    @Override
    public List<Sessao> findByCinema(Long cinemaId) {
        if (cinemaId == null) {
            throw new IllegalArgumentException("Cinema ID não pode ser nulo");
        }
        return repository.findByCinema(cinemaId).list();
    }
    
    @Override
    public List<Sessao> findByStatus(Long statusId) {
        if (statusId == null) {
            throw new IllegalArgumentException("Status ID não pode ser nulo");
        }
        return repository.findByStatus(statusId).list();
    }
    
    @Override
    public List<Sessao> findSessoesEmExibicao(LocalDateTime agora) {
        if (agora == null) {
            agora = LocalDateTime.now();
        }
        return repository.findSessoesEmExibicao(agora).list();
    }

    @Override
    public boolean existsBySalaAndHorario(Long salaId, LocalDateTime inicio, LocalDateTime fim, Long sessaoId) {
        if (salaId == null || inicio == null || fim == null) {
            throw new IllegalArgumentException("SalaId, início e fim são obrigatórios");
        }
        long count = repository.countConflitoHorario(salaId, inicio, fim, sessaoId);
        return count > 0;
    }

    @Override
    @Transactional
    public void update(Long id, Sessao sessao) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
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
        if (sessao.getPreco() != null && sessao.getPreco() > 0) {
            s.setPreco(sessao.getPreco());
        }
        if (sessao.getCapacidadeTotal() != null && sessao.getCapacidadeTotal() > 0) {
            s.setCapacidadeTotal(sessao.getCapacidadeTotal());
        }
        if (sessao.getCapacidadeDisponivel() != null) {
            s.setCapacidadeDisponivel(sessao.getCapacidadeDisponivel());
        }
        if (sessao.getStatus() != null) {
            s.setStatus(sessao.getStatus());
        }
        if (sessao.getCinema() != null) {
            s.setCinema(sessao.getCinema());
        }

        if (s.getInicio() != null && s.getFim() != null && s.getInicio().isAfter(s.getFim())) {
            throw new IllegalArgumentException("Horário de início não pode ser após o horário de fim");
        }
    }
}