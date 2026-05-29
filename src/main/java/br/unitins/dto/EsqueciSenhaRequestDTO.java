package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequestDTO(
    @NotBlank(message = "Login ou email e obrigatorio")
    String login
) {}
