package br.unitins.dto;

import java.util.List;

public record SalaResponseDTO(
    Long id,
    List<String> poltronasCodigos
) {}