package br.unitins.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRegisterRequestDTO(
    @NotBlank(message = "Login e obrigatorio")
    String login,
    @NotBlank(message = "Senha e obrigatoria")
    String senha,
    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    String email,
    String nome
) {}

