package br.unitins.dto;

import java.util.List;

public record AtorResponseDTO(
    Long id,
    String nome,
    List<PremioResponseDTO> premios
) {}