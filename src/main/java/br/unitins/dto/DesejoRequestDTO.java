package br.unitins.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DesejoRequestDTO(
    @NotNull(message = "Sessao e obrigatoria")
    @Positive(message = "Id da sessao deve ser positivo")
    Long sessaoId
) {}

