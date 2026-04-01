package br.unitins.mapper;

import br.unitins.dto.FilmePremiadoRequestDTO;
import br.unitins.dto.FilmePremiadoResponseDTO;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.FilmePremiado;

public class FilmePremiadoMapper {
    
    public static FilmePremiado toEntity (FilmePremiadoRequestDTO filmeDTO) {
        if (filmeDTO == null) {
            return null;
        }
        FilmePremiado filmePremiado = new FilmePremiado();
        filmePremiado.setNome(filmeDTO.nome());
        filmePremiado.setDuracao(filmeDTO.duracao());
        filmePremiado.setSinopse(filmeDTO.sinopse());
        filmePremiado.setIdiomaOriginal(filmeDTO.idiomaOriginal());
        filmePremiado.setAnoLancamento(filmeDTO.anoLancamento());
        filmePremiado.setClassificacaoIndicativa(ClassificacaoIndicativa.valueOf(filmeDTO.idClassificacaoIndicativa()));
        filmePremiado.setGeneros(filmeDTO.generosRequestDTOs().stream().map(GeneroMapper::toEntity).toList());
        filmePremiado.setAtores(filmeDTO.atoresRequestDTOs().stream().map(AtorMapper::toEntity).toList());
        filmePremiado.setPremiacao(filmeDTO.premiosRequestDTOs().stream().map(PremioMapper::toEntity).toList());
        return filmePremiado;
    }

    public static FilmePremiadoResponseDTO toResponseDTO (FilmePremiado filmePremiado) {
        if (filmePremiado == null) {
            return null;
        }
        return new FilmePremiadoResponseDTO(
            filmePremiado.getId(), 
            filmePremiado.getNome(), 
            filmePremiado.getDuracao(), 
            filmePremiado.getSinopse(), 
            filmePremiado.getIdiomaOriginal(), 
            filmePremiado.getAnoLancamento(), 
            filmePremiado.getClassificacaoIndicativa(),
            filmePremiado.getGeneros().stream().map(GeneroMapper::toResponseDTO).toList(),
            filmePremiado.getAtores().stream().map(AtorMapper::toResponseDTO).toList(),
            filmePremiado.getPremiacao().stream().map(PremioMapper::toResponseDTO).toList()
        );
    }
}
