package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PremioRequestDTO(
    @NotBlank(message = "Nome do prêmio é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @NotNull(message = "Ano é obrigatório")
    @Positive(message = "Ano deve ser positivo")
    Integer ano,
    
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    String categoria
) {}