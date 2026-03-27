package br.unitins.service;

import java.util.List;

import br.unitins.model.Filme;
import br.unitins.repository.FilmeRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class FilmeServiceImpl implements FilmeService {

    @Inject
    FilmeRepository filmeRepository;


    @Override
    public List<Filme> findByAtor(Long idAtor) {
        
        return filmeRepository.findByAtor(idAtor).list();
    }

    @Override
    public List<Filme> findByClassificacaoIndicativa(Long idClassificacaoIndicativa) {
        
        return filmeRepository.findByClassificacaoIndicativa(idClassificacaoIndicativa).list();
    }

    @Override
    public List<Filme> findByGenero(Long idGenero) {
        
        return filmeRepository.findByGenero(idGenero).list();
    }

    @Override
    @Transactional
    public Filme create(Filme filme) {

        filmeRepository.persist(filme);
        return filme;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        filmeRepository.deleteById(id);

    }

    @Override
    public List<Filme> findAll() {
        return filmeRepository.listAll();
    }

    @Override
    public List<Filme> findByAnoLancamento(Integer anoLancamento) {

        return filmeRepository.findByAnoLancamento(anoLancamento).list();
    }

    @Override
    public Filme findById(Long id) {

        return filmeRepository.findById(id);
    }

    @Override
    public List<Filme> findByIdiomaOriginal(String idiomaOriginal) {
        return filmeRepository.findByIdiomaOriginal(idiomaOriginal).list();
    }

    @Override
    public List<Filme> findByNome(String nome) {

        return filmeRepository.findByNome(nome).list();
    }

    @Override
    @Transactional
    public void update(Long id, Filme filme) {
        Filme fil = findById(id);
        fil.setNome(filme.getNome());
        fil.setAnoLancamento(filme.getAnoLancamento());
        fil.setIdiomaOriginal(filme.getIdiomaOriginal());

    }

}
