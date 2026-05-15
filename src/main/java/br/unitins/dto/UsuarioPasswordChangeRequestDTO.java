package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioPasswordChangeRequestDTO(
    @NotBlank(message = "Senha atual e obrigatoria")
    String senhaAtual,
    @NotBlank(message = "Nova senha e obrigatoria")
    String novaSenha
) {}
