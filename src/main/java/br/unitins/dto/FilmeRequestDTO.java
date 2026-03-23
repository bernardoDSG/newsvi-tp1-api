package br.unitins.dto;

import java.util.List;

public record FilmeRequestDTO(
    String nome,
    String duracao,
    String sinopse,
    String idiomaOriginal,
    Integer anoLancamento,
    Long idClassificacaoIndicativa,
    List<Long> idGeneros,
    List<Long> idAtores
) {
    
}
