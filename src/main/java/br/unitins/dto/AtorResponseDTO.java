package br.unitins.dto;

import java.time.LocalDate;
import java.util.List;

public record AtorResponseDTO(
    Long id,
    String nome,
    LocalDate dataNascimento,
    String nacionalidade,
    String urlFoto,  // NOVO
    List<PremioResponseDTO> premios
) {}