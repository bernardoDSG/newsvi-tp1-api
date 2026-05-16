package br.unitins.dto;

import br.unitins.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record StatusPedidoRequestDTO(

    @NotNull(message = "Status e obrigatorio")
    StatusPedido status
) {}

