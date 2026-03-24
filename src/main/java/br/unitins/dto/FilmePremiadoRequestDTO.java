package br.unitins.dto;

import java.util.List;

public record FilmePremiadoRequestDTO(
        String nome,
        String duracao,
        String sinopse,
        String idiomaOriginal,
        Integer anoLancamento,
        Long idClassificacaoIndicativa,
        List<Long> idGeneros,
        List<Long> idAtores,
        List<Long> idsPremios) {

}
