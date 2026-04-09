package br.unitins.mapper;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.model.Filme;

public class FilmeMapper {
   
    public static Filme toEntity(FilmeRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Filme filme = new Filme();
        filme.setNome(dto.nome());
        filme.setDuracao(dto.duracao());
        // REMOVIDO: filme.setDuracaoMinutos(dto.duracaoMinutos());
        // A duracaoMinutos será calculada automaticamente no setDuracao() da entidade
        filme.setSinopse(dto.sinopse());
        filme.setIdiomaOriginal(dto.idiomaOriginal());
        filme.setAnoLancamento(dto.anoLancamento());
        filme.setImagemPoster(dto.imagemPoster());
        filme.setTrailerUrl(dto.trailerUrl());
        return filme;
    }

    public static FilmeResponseDTO toResponseDTO(Filme filme) {
        if (filme == null) {
            return null;
        }
        return new FilmeResponseDTO(
            filme.getId(),
            filme.getNome(),
            filme.getDuracao(),
            filme.getDuracaoMinutos(),  // ← OK: no ResponseDTO isso existe
            filme.getSinopse(),
            filme.getIdiomaOriginal(),
            filme.getAnoLancamento(),
            filme.getImagemPoster(),
            filme.getTrailerUrl(),
            filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME() : null,
            filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
            filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
            filme.getPremios() != null ? filme.getPremios().stream()
                .map(PremioMapper::toResponseDTO)
                .toList() : null
        );
    }
}