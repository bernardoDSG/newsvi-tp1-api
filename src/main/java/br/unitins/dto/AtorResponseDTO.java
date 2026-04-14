package br.unitins.dto;

import java.time.LocalDate;
import java.util.List;

public record AtorResponseDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    LocalDate dataNascimento,
    List<PremioResponseDTO> premios
) {}