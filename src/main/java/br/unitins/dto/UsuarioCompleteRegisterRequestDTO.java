package br.unitins.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCompleteRegisterRequestDTO(
    @NotBlank(message = "Login e obrigatorio")
    String login,
    @NotBlank(message = "Senha e obrigatoria")
    String senha,
    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    String email,
    @NotBlank(message = "Nome e obrigatorio")
    String nome,
    @NotBlank(message = "Logradouro e obrigatorio")
    String logradouro,
    @NotBlank(message = "Numero e obrigatorio")
    String numero,
    String complemento,
    @NotBlank(message = "Bairro e obrigatorio")
    String bairro,
    @NotBlank(message = "Cidade e obrigatoria")
    String cidade,
    @NotBlank(message = "Estado e obrigatorio")
    String estado,
    @NotBlank(message = "CEP e obrigatorio")
    String cep
) {}
