package br.unitins.dto;

import java.util.List;

import br.unitins.model.ClassificacaoIndicativa;

public record FilmePremiadoResponseDTO(
    Long id,
    String nome,
    String duracao,
    String sinopse,
    String idiomaOriginal,
    Integer anoLancamento,
    ClassificacaoIndicativa classificacaoIndicativa,
    List<GeneroResponseDTO> generos,
    List<AtorResponseDTO> atores,
    List<PremioResponseDTO> premiacao
) {
    
}
