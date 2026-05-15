package br.unitins.dto;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    String login,
    String perfil
) {}
