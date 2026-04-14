package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessaoResponseDTO(
    Long id,
    LocalDateTime inicio,
    LocalDateTime fim,
    Double preco,
    Integer capacidadeTotal,
    Integer capacidadeDisponivel,
    String status,
    String statusNome,
    String tipoSessao,
    String filmeNome,
    String cinemaNome,
    List<Long> salasIds
) {}