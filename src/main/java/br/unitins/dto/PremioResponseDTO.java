package br.unitins.dto;

public record PremioResponseDTO(
    Long id,
    String nome,
    String categoria,
    Integer ano
) {
    
}
