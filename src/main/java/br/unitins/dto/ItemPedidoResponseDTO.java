package br.unitins.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
    Long id,
    Integer quantidade,
    BigDecimal valorUnitario,
    BigDecimal descontoUnitario,
    Long sessaoId,
    String filmeNome,
    String cinemaNome,
    Long poltronaId,
    String poltronaCodigo
) {}
