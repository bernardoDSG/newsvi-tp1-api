package br.unitins.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(

    @NotNull(message = "Sessao e obrigatoria")
    @Positive(message = "ID da sessao deve ser positivo")
    Long sessaoId,

    @Positive(message = "ID da poltrona deve ser positivo")
    Long poltronaId,

    @NotNull(message = "Quantidade e obrigatoria")
    @Positive(message = "Quantidade deve ser positiva")
    Integer quantidade,

    @NotNull(message = "Valor unitario e obrigatorio")
    @DecimalMin(value = "0.01", message = "Valor unitario deve ser maior que zero")
    BigDecimal valorUnitario,

    @DecimalMin(value = "0.00", message = "Desconto unitario nao pode ser negativo")
    BigDecimal descontoUnitario
) {}

