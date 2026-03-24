package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.model.TipoSessao;

public record SessaoResponseDTO(
    Long id,
    FilmeResponseDTO filme,
    TipoSessao tipo,
    List<SalaResponseDTO> salas,
    LocalDateTime inicio,
    LocalDateTime fim    
) {
    
}
