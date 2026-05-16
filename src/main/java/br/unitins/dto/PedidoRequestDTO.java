package br.unitins.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record PedidoRequestDTO(

    @NotEmpty(message = "Pedido deve possuir ao menos um item")
    List<@Valid ItemPedidoRequestDTO> itens
) {}

