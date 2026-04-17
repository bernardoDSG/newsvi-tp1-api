package br.unitins.dto;

public record PoltronaResponseDTO(
    Long id,
    String codigo,
    String linha,
    Integer coluna,
    String disponibilidade
) {}