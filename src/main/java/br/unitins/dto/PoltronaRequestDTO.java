package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PoltronaRequestDTO(
    @NotBlank(message = "Código é obrigatório")
    @Size(min = 1, max = 10, message = "Código deve ter entre 1 e 10 caracteres")
    String codigo,
    
    @Size(max = 1, message = "Linha deve ter no máximo 1 caractere")
    String linha,
    
    @Positive(message = "Coluna deve ser positiva")
    Integer coluna,
    
    @NotNull(message = "Disponibilidade é obrigatória")
    @Positive(message = "ID da disponibilidade deve ser positivo")
    Long disponibilidadeId
) {}