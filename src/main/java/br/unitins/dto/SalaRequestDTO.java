package br.unitins.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SalaRequestDTO(
    @NotNull(message = "Lista de poltronas é obrigatória")
    List<@Positive(message = "ID da poltrona deve ser positivo") Long> poltronasIds
) {}