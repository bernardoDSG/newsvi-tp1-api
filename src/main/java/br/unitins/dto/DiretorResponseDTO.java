package br.unitins.dto;

import java.util.List;

public record DiretorResponseDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    String nacionalidade,
    String biografia,
    List<String> filmesNomes
) {}