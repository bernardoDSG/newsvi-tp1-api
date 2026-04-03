package br.unitins.dto;

import java.util.List;

public record FilmeResponseDTO(
    Long id,
    String nome,
    String duracao,
    String sinopse,
    String idiomaOriginal,
    Integer anoLancamento,
    String classificacaoIndicativa,
    List<String> generos,
    List<String> atores,
    List<PremioResponseDTO> premios
) {}