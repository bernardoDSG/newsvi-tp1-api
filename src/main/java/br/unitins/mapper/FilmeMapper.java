package br.unitins.mapper;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Filme;


public class FilmeMapper {
    

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
        filme.setGeneros(filmeDTO.generosRequestDTOs().stream().map(GeneroMapper::toEntity).toList());
        filme.setAtores(filmeDTO.atoresRequestDTOs().stream().map(AtorMapper::toEntity).toList());
        return filme;

    }

    public static FilmeResponseDTO toResponseDTO (Filme filme) {
        if (filme == null) {
            return null;
        }
        return new FilmeResponseDTO(
            filme.getId(), 
            filme.getNome(), 
            filme.getDuracao(), 
            filme.getSinopse(), 
            filme.getIdiomaOriginal(), 
            filme.getAnoLancamento(), 
            filme.getClassificacaoIndicativa(),
            filme.getGeneros().stream().map(GeneroMapper::toResponseDTO).toList(),
            filme.getAtores().stream().map(AtorMapper::toResponseDTO).toList()
        );
    }
}
