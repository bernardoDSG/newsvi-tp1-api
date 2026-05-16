package br.unitins.dto;

import jakarta.validation.constraints.Email;

public record UsuarioUpdateRequestDTO(
    String nome,
    @Email(message = "Email invalido")
    String email
) {}

