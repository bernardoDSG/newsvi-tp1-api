package br.unitins.dto;

import java.util.List;

public record SalaResponseDTO(
    Long id,
    Integer numero,
    Integer capacidade,
    List<String> poltronasCodigos
) {}