package br.unitins.dto;

import java.util.List;

import br.unitins.model.ClassificacaoIndicativa;

public record FilmeResponseDTO(
    Long id,
    String nome,
    String duracao,
    String sinopse,
    String idiomaOriginal,
    Integer anoLancamento,
    ClassificacaoIndicativa classificacaoIndicativa,
    List<GeneroResponseDTO> generos,
    List<AtorResponseDTO> atores
) {
    
}
