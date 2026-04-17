package br.unitins.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SalaRequestDTO(
    @NotNull(message = "Número é obrigatório")
    @Positive(message = "Número deve ser positivo")
    Integer numero,
    
    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    Integer capacidade,
    
    List<Long> poltronasIds
) {}