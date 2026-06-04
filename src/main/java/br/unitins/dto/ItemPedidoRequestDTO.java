package br.unitins.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(

    @NotNull(message = "Sessao e obrigatoria")
    @Positive(message = "ID da sessao deve ser positivo")
    Long sessaoId,

    @NotNull(message = "Poltrona e obrigatoria")
    @Positive(message = "ID da poltrona deve ser positivo")
    Long poltronaId
) {}
