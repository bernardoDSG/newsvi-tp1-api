package br.unitins.dto;

public record DesejoResponseDTO(
    Long id,
    Long sessaoId,
    String filmeNome,
    String cinemaNome,
    Long usuarioId,
    String usuarioLogin
) {}
