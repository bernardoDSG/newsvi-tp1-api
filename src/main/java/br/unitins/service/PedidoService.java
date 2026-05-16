package br.unitins.service;

import java.util.List;

import br.unitins.dto.PedidoRequestDTO;
import br.unitins.model.Pedido;
import br.unitins.model.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface PedidoService {

    Pedido create(@Valid PedidoRequestDTO dto, String loginUsuario);
    List<Pedido> findAll();
    List<Pedido> findMeusPedidos(String loginUsuario);
    Pedido findById(@NotNull(message = "ID nao pode ser nulo") Long id);
    Pedido findMeuPedidoById(@NotNull(message = "ID nao pode ser nulo") Long id, String loginUsuario);
    void updateStatus(@NotNull(message = "ID nao pode ser nulo") Long id, StatusPedido status);
    void cancelarMeuPedido(@NotNull(message = "ID nao pode ser nulo") Long id, String loginUsuario);
}

