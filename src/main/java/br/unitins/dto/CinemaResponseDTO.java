package br.unitins.dto;

import java.util.List;

public record CinemaResponseDTO(
    Long id,
    String nome,
    String cnpj,
    String telefone,
    Long enderecoId,
    EnderecoResponseDTO endereco,
    List<String> salasNumeros
) {}