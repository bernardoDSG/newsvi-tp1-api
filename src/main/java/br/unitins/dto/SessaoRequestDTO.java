package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessaoRequestDTO(
    LocalDateTime inicio,
    LocalDateTime fim,
    FilmeRequestDTO filmeRequestDTO,
    Long idTipoSessao,
    List<SalaRequestDTO> salaRequestDTOs

) {
    
}
