package br.unitins.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SessaoResponseDTO(
    Long id,
    LocalDateTime inicio,
    LocalDateTime fim,
    Integer capacidadeTotal,
    Integer capacidadeDisponivel,
    BigDecimal preco,
    String status,
    String statusNome,
    String tipoSessao,
    String filmeNome,
    String cinemaNome,
    List<Long> salasIds
) {}
