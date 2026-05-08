package br.unitins.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.dto.PedidoRequestDTO;
import br.unitins.dto.PedidoResponseDTO;
import br.unitins.dto.StatusPedidoRequestDTO;
import br.unitins.mapper.PedidoMapper;
import br.unitins.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("CLIENTE")
    public Response criar(@Valid PedidoRequestDTO dto) {
        return Response.status(Status.CREATED)
                .entity(PedidoMapper.toResponseDTO(service.create(dto, jwt.getName())))
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response buscarTodos() {
        List<PedidoResponseDTO> list = service.findAll().stream().map(PedidoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(PedidoMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/meus")
    @RolesAllowed("CLIENTE")
    public Response meusPedidos() {
        List<PedidoResponseDTO> list = service.findMeusPedidos(jwt.getName()).stream()
                .map(PedidoMapper::toResponseDTO)
                .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/meus/{id}")
    @RolesAllowed("CLIENTE")
    public Response meuPedidoPorId(@PathParam("id") Long id) {
        return Response.ok(PedidoMapper.toResponseDTO(service.findMeuPedidoById(id, jwt.getName()))).build();
    }

    @PUT
    @Path("/{id}/status")
    @RolesAllowed("ADMIN")
    public Response alterarStatus(@PathParam("id") Long id, @Valid StatusPedidoRequestDTO dto) {
        service.updateStatus(id, dto.status());
        return Response.ok(PedidoMapper.toResponseDTO(service.findById(id))).build();
    }

    @PUT
    @Path("/meus/{id}/cancelar")
    @RolesAllowed("CLIENTE")
    public Response cancelarMeuPedido(@PathParam("id") Long id) {
        service.cancelarMeuPedido(id, jwt.getName());
        return Response.ok(PedidoMapper.toResponseDTO(service.findMeuPedidoById(id, jwt.getName()))).build();
    }
}
