package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DiretorRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    String email,
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    String telefone,
    
    @Size(max = 50, message = "Nacionalidade deve ter no máximo 50 caracteres")
    String nacionalidade,
    
    String biografia
) {}