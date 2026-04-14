package br.unitins.service;

import java.util.List;
import br.unitins.model.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface EnderecoService {
    List<Endereco> findAll();
    Endereco findById(@NotNull(message = "ID não pode ser nulo") Long id);
    Endereco findByCep(@NotNull(message = "CEP não pode ser nulo") String cep);
    Endereco create(@Valid Endereco endereco);
    void update(@NotNull(message = "ID não pode ser nulo") Long id, @Valid Endereco endereco);
    void delete(@NotNull(message = "ID não pode ser nulo") Long id);
}