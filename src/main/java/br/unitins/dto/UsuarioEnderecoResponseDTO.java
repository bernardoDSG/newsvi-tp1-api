package br.unitins.dto;

public record UsuarioEnderecoResponseDTO(
    Long id,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    String cep
) {}

