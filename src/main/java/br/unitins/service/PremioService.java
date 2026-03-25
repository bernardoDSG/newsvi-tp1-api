package br.unitins.service;

import java.util.List;

import br.unitins.model.Premio;

public interface PremioService {
    List<Premio> findAll();
    Premio findById(Long id);
    List<Premio> findByNome(String nome);
    List<Premio> findByAno(Integer ano);
    Premio create(Premio premio);
    void update(Long id, Premio premio);
    void delete(Long id);
}
