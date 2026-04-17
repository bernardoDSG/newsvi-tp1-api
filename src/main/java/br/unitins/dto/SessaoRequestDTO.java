package br.unitins.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SessaoRequestDTO(
    @NotNull(message = "Horário de início é obrigatório")
    @Future(message = "Horário de início deve ser futuro")
    LocalDateTime inicio,
    
    @NotNull(message = "Horário de fim é obrigatório")
    @Future(message = "Horário de fim deve ser futuro")
    LocalDateTime fim,
    
    @NotNull(message = "Capacidade total é obrigatória")
    @Positive(message = "Capacidade total deve ser positiva")
    Integer capacidadeTotal,
    
    @NotNull(message = "Capacidade disponível é obrigatória")
    @Positive(message = "Capacidade disponível deve ser positiva")
    Integer capacidadeDisponivel,
    
    @NotNull(message = "Status é obrigatório")
    @Positive(message = "ID do status deve ser positivo")
    Long statusId,
    
    @NotNull(message = "Tipo de sessão é obrigatório")
    @Positive(message = "ID do tipo de sessão deve ser positivo")
    Long tipoSessaoId,
    
    @NotNull(message = "Filme é obrigatório")
    @Positive(message = "ID do filme deve ser positivo")
    Long filmeId,
    
    @NotNull(message = "Cinema é obrigatório")
    @Positive(message = "ID do cinema deve ser positivo")
    Long cinemaId,
    
    @NotNull(message = "Salas são obrigatórias")
    List<@Positive(message = "ID da sala deve ser positivo") Long> salasIds
) {}