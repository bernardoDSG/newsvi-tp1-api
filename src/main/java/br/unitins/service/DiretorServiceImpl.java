package br.unitins.service;

import java.util.List;
import br.unitins.model.Diretor;
import br.unitins.repository.DiretorRepository;
import br.unitins.service.DiretorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DiretorServiceImpl implements DiretorService {

    @Inject
    DiretorRepository repository;

    @Override
    @Transactional
    public Diretor create(@Valid Diretor diretor) {
        if (diretor.getNome() == null || diretor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do diretor é obrigatório");
        }
        repository.persist(diretor);
        return diretor;
    }

    @Override
    @Transactional
    public void delete(@NotNull(message = "ID não pode ser nulo") Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Diretor não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Diretor> findAll() {
        return repository.listAll();
    }

    @Override
    public Diretor findById(Long id) {
        Diretor diretor = repository.findById(id);
        if (diretor == null) {
            throw new NotFoundException("Diretor não encontrado com ID: " + id);
        }
        return diretor;
    }

    @Override
    public List<Diretor> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return repository.findByNome(nome).list();
    }
    
    @Override
    public List<Diretor> findByNacionalidade(String nacionalidade) {
        if (nacionalidade == null || nacionalidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Nacionalidade não pode ser vazia");
        }
        return repository.findByNacionalidade(nacionalidade).list();
    }

    @Override
    @Transactional
    public void update(Long id, @Valid Diretor diretor) {
        Diretor d = findById(id);
        
        if (diretor.getNome() != null && !diretor.getNome().trim().isEmpty()) {
            d.setNome(diretor.getNome());
        }
        if (diretor.getEmail() != null) {
            d.setEmail(diretor.getEmail());
        }
        if (diretor.getTelefone() != null) {
            d.setTelefone(diretor.getTelefone());
        }
        if (diretor.getNacionalidade() != null) {
            d.setNacionalidade(diretor.getNacionalidade());
        }
        if (diretor.getBiografia() != null) {
            d.setBiografia(diretor.getBiografia());
        }
    }
}