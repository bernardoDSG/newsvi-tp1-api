package br.unitins.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCadastroRequestDTO(
    @NotBlank(message = "Nome e obrigatorio")
    String nome,

    @Email(message = "Email invalido")
    String email
) {}
