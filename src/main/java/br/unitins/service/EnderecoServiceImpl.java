package br.unitins.service;

import java.util.List;

import br.unitins.model.Endereco;
import br.unitins.repository.EnderecoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    @Inject
    EnderecoRepository repository;

    @Override
    @Transactional
    public Endereco create(Endereco endereco) {
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }
        repository.persist(endereco);
        return endereco;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Endereço não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Endereco> findAll() {
        return repository.listAll();
    }

    @Override
    public Endereco findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Endereco endereco = repository.findById(id);
        if (endereco == null) {
            throw new NotFoundException("Endereço não encontrado com ID: " + id);
        }
        return endereco;
    }

    @Override
    public Endereco findByCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        Endereco endereco = repository.findByCep(cep);
        if (endereco == null) {
            throw new NotFoundException("Endereço não encontrado com CEP: " + cep);
        }
        return endereco;
    }

    @Override
    @Transactional
    public void update(Long id, Endereco endereco) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Endereco e = findById(id);
        
        if (endereco.getLogradouro() != null) {
            e.setLogradouro(endereco.getLogradouro());
        }
        if (endereco.getNumero() != null) {
            e.setNumero(endereco.getNumero());
        }
        if (endereco.getComplemento() != null) {
            e.setComplemento(endereco.getComplemento());
        }
        if (endereco.getBairro() != null) {
            e.setBairro(endereco.getBairro());
        }
        if (endereco.getCidade() != null) {
            e.setCidade(endereco.getCidade());
        }
        if (endereco.getEstado() != null) {
            e.setEstado(endereco.getEstado());
        }
        if (endereco.getCep() != null) {
            e.setCep(endereco.getCep());
        }
    }
}