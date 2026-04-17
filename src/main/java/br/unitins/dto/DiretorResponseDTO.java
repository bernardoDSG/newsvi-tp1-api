package br.unitins.dto;

import java.time.LocalDate;

public record DiretorResponseDTO(
    Long id,
    String nome,
    LocalDate dataNascimento,
    String nacionalidade,
    String urlFoto
) {}