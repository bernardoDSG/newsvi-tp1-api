package br.unitins.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioEnderecoRequestDTO(
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

