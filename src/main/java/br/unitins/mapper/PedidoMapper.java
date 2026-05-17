package br.unitins.mapper;

import br.unitins.dto.ItemPedidoResponseDTO;
import br.unitins.dto.PedidoResponseDTO;
import br.unitins.model.ItemPedido;
import br.unitins.model.Pedido;

public class PedidoMapper {

    public static PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getDataCriacao(),
            pedido.getDataPagamento(),
            pedido.getTotal(),
            pedido.getDesconto(),
            pedido.getStatus() != null ? pedido.getStatus().name() : null,
            pedido.getFormaPagamento() != null ? pedido.getFormaPagamento().name() : null,
            pedido.getCodigoPagamento(),
            pedido.getUsuario() != null ? pedido.getUsuario().getLogin() : null,
            pedido.getItens() != null ? pedido.getItens().stream().map(PedidoMapper::toItemResponseDTO).toList() : null
        );
    }

    private static ItemPedidoResponseDTO toItemResponseDTO(ItemPedido item) {
        return new ItemPedidoResponseDTO(
            item.getId(),
            item.getQuantidade(),
            item.getValorUnitario(),
            item.getDescontoUnitario(),
            item.getSessao() != null ? item.getSessao().getId() : null,
            item.getSessao() != null && item.getSessao().getFilme() != null ? item.getSessao().getFilme().getNome() : null,
            item.getSessao() != null && item.getSessao().getCinema() != null ? item.getSessao().getCinema().getNome() : null,
            item.getPoltrona() != null ? item.getPoltrona().getId() : null,
            item.getPoltrona() != null ? item.getPoltrona().getCodigo() : null
        );
    }
}
