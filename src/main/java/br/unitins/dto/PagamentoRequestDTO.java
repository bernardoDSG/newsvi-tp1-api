package br.unitins.dto;

import br.unitins.model.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(
    @NotNull(message = "Forma de pagamento e obrigatoria")
    FormaPagamento formaPagamento
) {}
