package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessaoResponseDTO(
    Long id,
    LocalDateTime inicio,
    LocalDateTime fim,
    String filmeNome,
    String tipoSessao,
    List<Long> salasIds
) {}