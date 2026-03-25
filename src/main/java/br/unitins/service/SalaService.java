package br.unitins.service;

import java.util.List;

import br.unitins.model.Sala;

public interface SalaService {
    List<Sala> findAll();
    Sala findById(Long id);
    Sala create(Sala sala);
    void update(Long id, Sala sala);
    void delete(Long id);
}
