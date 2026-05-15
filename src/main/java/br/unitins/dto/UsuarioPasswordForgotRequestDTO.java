package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioPasswordForgotRequestDTO(
    @NotBlank(message = "Login e obrigatorio")
    String login,
    @NotBlank(message = "Nova senha e obrigatoria")
    String novaSenha
) {}
