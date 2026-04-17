package br.unitins.service;

import java.util.List;

import br.unitins.model.Diretor;
import br.unitins.repository.DiretorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DiretorServiceImpl implements DiretorService {

    @Inject
    DiretorRepository repository;

    @Override
    @Transactional
    public Diretor create(Diretor diretor) {
        if (diretor.getNome() == null || diretor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do diretor é obrigatório");
        }
        repository.persist(diretor);
        return diretor;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
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
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
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
    public void update(Long id, Diretor diretor) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Diretor d = findById(id);
        
        if (diretor.getNome() != null && !diretor.getNome().trim().isEmpty()) {
            d.setNome(diretor.getNome());
        }
        if (diretor.getDataNascimento() != null) {
            d.setDataNascimento(diretor.getDataNascimento());
        }
        if (diretor.getNacionalidade() != null) {
            d.setNacionalidade(diretor.getNacionalidade());
        }
        if (diretor.getUrlFoto() != null) {
            d.setUrlFoto(diretor.getUrlFoto());
        }
    }
}