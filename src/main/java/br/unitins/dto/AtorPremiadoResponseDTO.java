package br.unitins.dto;

import java.util.List;

public record AtorPremiadoResponseDTO(
    Long id,
    String nome,
    List<PremioResponseDTO> premiacao
) {
    
}
