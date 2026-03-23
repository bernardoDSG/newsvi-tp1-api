package br.unitins.dto;

import java.util.List;

public record AtorPremiadoRequestDTO(
    String nome,
    List<Long> idsPremios
) {
    
}
