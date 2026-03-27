package br.unitins.mapper;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Filme;
import br.unitins.service.FilmeService;
import br.unitins.service.GeneroService;
import jakarta.inject.Inject;

public class FilmeMapper {
    @Inject
    GeneroService service;

    public static Filme toEntity(FilmeRequestDTO filmeDTO) {
        if (filmeDTO == null) {
            return null;
        }
        Filme filme = new Filme();
        filme.setNome(filmeDTO.nome());
        filme.setDuracao(filmeDTO.duracao());
        filme.setSinopse(filmeDTO.sinopse());
        filme.setIdiomaOriginal(filmeDTO.idiomaOriginal());
        filme.setAnoLancamento(filmeDTO.anoLancamento());
        filme.setClassificacaoIndicativa(ClassificacaoIndicativa.valueOf(filmeDTO.idClassificacaoIndicativa()));
        filme.getGeneros(filmeDTO.idGeneros().stream().map(id -> service.findById(id)).toList());
    }
}
