package br.unitins.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.exception.ValidationException;
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
        validateSessao(sessao);

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
        Sessao existente = findById(id);

        if (sessao.getInicio() != null) {
            existente.setInicio(sessao.getInicio());
        }
        if (sessao.getFim() != null) {
            existente.setFim(sessao.getFim());
        }
        if (sessao.getFilme() != null) {
            existente.setFilme(sessao.getFilme());
        }
        if (sessao.getTipo() != null) {
            existente.setTipo(sessao.getTipo());
        }
        if (sessao.getSalas() != null) {
            existente.setSalas(sessao.getSalas());
        }
        if (sessao.getCapacidadeTotal() != null) {
            if (sessao.getCapacidadeTotal() <= 0) {
                throw new ValidationException("Capacidade total deve ser maior que zero", "capacidadeTotal");
            }
            existente.setCapacidadeTotal(sessao.getCapacidadeTotal());
        }
        if (sessao.getCapacidadeDisponivel() != null) {
            existente.setCapacidadeDisponivel(sessao.getCapacidadeDisponivel());
        }
        if (sessao.getStatus() != null) {
            existente.setStatus(sessao.getStatus());
        }
        if (sessao.getCinema() != null) {
            existente.setCinema(sessao.getCinema());
        }

        validateIntervalo(existente.getInicio(), existente.getFim());
    }

    private void validateSessao(Sessao sessao) {
        if (sessao.getInicio() == null) {
            throw new ValidationException("Horário de início é obrigatório", "inicio");
        }
        if (sessao.getFim() == null) {
            throw new ValidationException("Horário de fim é obrigatório", "fim");
        }
        validateIntervalo(sessao.getInicio(), sessao.getFim());
        if (sessao.getFilme() == null) {
            throw new ValidationException("Filme é obrigatório", "filmeId");
        }
        if (sessao.getTipo() == null) {
            throw new ValidationException("Tipo de sessão é obrigatório", "tipoSessaoId");
        }
        if (sessao.getCapacidadeTotal() == null || sessao.getCapacidadeTotal() <= 0) {
            throw new ValidationException("Capacidade total deve ser maior que zero", "capacidadeTotal");
        }
    }

    private void validateIntervalo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new ValidationException("Horário de início não pode ser após o horário de fim", "inicio");
        }
    }
}
