package br.unitins.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FilmeRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 200, message = "Nome deve ter entre 1 e 200 caracteres")
    String nome,
    
    @NotBlank(message = "Duração é obrigatória")
    @Size(min = 1, max = 20, message = "Duração deve ter entre 1 e 20 caracteres")
    String duracao,
    
    @NotBlank(message = "Sinopse é obrigatória")
    @Size(min = 10, max = 2000, message = "Sinopse deve ter entre 10 e 2000 caracteres")
    String sinopse,
    
    @Size(max = 50, message = "Idioma original deve ter no máximo 50 caracteres")
    String idiomaOriginal,
    
    @NotNull(message = "Ano de lançamento é obrigatório")
    @Positive(message = "Ano de lançamento deve ser positivo")
    Integer anoLancamento,
    
    @NotNull(message = "Classificação indicativa é obrigatória")
    @Positive(message = "ID da classificação deve ser positivo")
    Long classificacaoIndicativaId,
    
    List<Long> generosIds,
    
    List<Long> atoresIds,
    
    List<Long> premiosIds
) {}