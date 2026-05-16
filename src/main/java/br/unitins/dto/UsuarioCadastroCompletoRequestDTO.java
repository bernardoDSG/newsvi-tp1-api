package br.unitins.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCadastroCompletoRequestDTO(
    @NotBlank(message = "Nome e obrigatorio")
    String nome,

    @Email(message = "Email invalido")
    String email,

    @NotNull(message = "Endereco e obrigatorio")
    @Valid
    UsuarioEnderecoRequestDTO endereco
) {}
