package br.unitins.dto;

import br.unitins.model.Disponibilidade;

public record PoltronaResponseDTO(
    Long id,
    String codigo,
    Disponibilidade disponibilidade
) {
    
}
