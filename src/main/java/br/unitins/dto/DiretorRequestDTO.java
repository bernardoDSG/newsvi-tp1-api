package br.unitins.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record DiretorRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento,
    
    @Size(max = 50, message = "Nacionalidade deve ter no máximo 50 caracteres")
    String nacionalidade,
    
    @Size(max = 500, message = "URL da foto deve ter no máximo 500 caracteres")
    String urlFoto  // NOVO
) {}