package br.unitins.dto;

public record PoltronaResponseDTO(
    Long id,
    String codigo,
    String disponibilidade
) {}