package br.unitins.dto;

import java.util.List;

public record SalaRequestDTO(
    List<PoltronaRequestDTO> poltronas
) {
    
}
