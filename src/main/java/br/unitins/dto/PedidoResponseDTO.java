package br.unitins.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime dataCriacao,
    BigDecimal total,
    BigDecimal desconto,
    String status,
    String usuarioLogin,
    List<ItemPedidoResponseDTO> itens
) {}

