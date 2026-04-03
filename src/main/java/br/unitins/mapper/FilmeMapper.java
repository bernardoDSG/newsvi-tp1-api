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
        filme.setSinopse(dto.sinopse());
        filme.setIdiomaOriginal(dto.idiomaOriginal());
        filme.setAnoLancamento(dto.anoLancamento());
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
            filme.getSinopse(),
            filme.getIdiomaOriginal(),
            filme.getAnoLancamento(),
            filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME() : null,
            filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
            filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
            null // prêmios do filme - se tiver relação com prêmios no futuro
        );
    }
}