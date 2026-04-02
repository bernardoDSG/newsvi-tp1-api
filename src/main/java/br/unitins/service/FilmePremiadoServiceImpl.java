package br.unitins.service;

import java.util.List;

import br.unitins.model.FilmePremiado;
import br.unitins.repository.FilmePremiadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FilmePremiadoServiceImpl implements FilmePremiadoService {

    @Inject
    FilmePremiadoRepository repository;

    @Override
    @Transactional
    public FilmePremiado create(FilmePremiado filmePremiado) {
        repository.persist(filmePremiado);
        return filmePremiado;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public List<FilmePremiado> findAll() {
        return repository.listAll();
    }

    @Override
    public List<FilmePremiado> findByAnoLancamento(Integer anoLancamento) {
        return repository.findByAnoLancamento(anoLancamento).list();
    }

    @Override
    public FilmePremiado findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<FilmePremiado> findByIdiomaOriginal(String idiomaOriginal) {
        return repository.findByIdiomaOriginal(idiomaOriginal).list();
    }

    @Override
    public List<FilmePremiado> findByNome(String nome) {
        return repository.findByNome(nome).list();
    }

    @Override
    public List<FilmePremiado> findByPremio(Long idPremio) {
        return repository.findByPremio(idPremio).list();
    }

    @Override
    @Transactional
    public void update(Long id, FilmePremiado filmePremiado) {
        FilmePremiado fil = findById(id);
        fil.setNome(filmePremiado.getNome());
        fil.setAnoLancamento(filmePremiado.getAnoLancamento());
        fil.setIdiomaOriginal(filmePremiado.getIdiomaOriginal());
        fil.setPremiacao(filmePremiado.getPremiacao());

    }

}
