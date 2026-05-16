package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EsqueciSenhaRequestDTO(
    @NotBlank(message = "Login ou email e obrigatorio")
    String login,

    @NotBlank(message = "Nova senha e obrigatoria")
    @Size(min = 8, message = "Nova senha deve ter pelo menos 8 caracteres")
    String novaSenha
) {}
