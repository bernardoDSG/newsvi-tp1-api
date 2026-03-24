package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessaoRequestDTO(
    LocalDateTime inicio,
    LocalDateTime fim,
    Long idFilme,
    Long idTipoSessao,
    List<Long> idSalas

) {
    
}
