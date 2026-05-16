package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(

    @NotBlank(message = "O login e obrigatorio")
    String login,

    @NotBlank(message = "A senha e obrigatoria")
    String senha
) {}

