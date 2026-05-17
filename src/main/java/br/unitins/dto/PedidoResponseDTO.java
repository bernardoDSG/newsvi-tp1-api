package br.unitins.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime dataCriacao,
    LocalDateTime dataPagamento,
    BigDecimal total,
    BigDecimal desconto,
    String status,
    String formaPagamento,
    String codigoPagamento,
    String usuarioLogin,
    List<ItemPedidoResponseDTO> itens
) {}
