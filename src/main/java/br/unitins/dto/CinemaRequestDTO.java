package br.unitins.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CinemaRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Size(min = 14, max = 18, message = "CNPJ deve ter entre 14 e 18 caracteres")
    String cnpj,
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    String telefone,
    
    @NotNull(message = "ID do endereço é obrigatório")
    Long enderecoId,
    
    List<Long> salasIds
) {}