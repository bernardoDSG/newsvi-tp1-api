package br.unitins.dto;

import java.time.LocalDateTime;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    String cinema,
    LocalDateTime inicio,
    LocalDateTime fim,
    Integer capacidadeDisponivel,
    String tipo,
    String status
) {}

