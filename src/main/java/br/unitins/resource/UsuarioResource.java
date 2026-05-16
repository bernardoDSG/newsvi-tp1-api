package br.unitins.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.dto.UsuarioEnderecoRequestDTO;
import br.unitins.dto.UsuarioEnderecoResponseDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.dto.UsuarioPasswordChangeRequestDTO;
import br.unitins.service.UsuarioEnderecoService;
import br.unitins.service.UsuarioService;
import br.unitins.util.JwtUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioEnderecoService enderecoService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/me")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response getCurrent() {
        String login = JwtUtil.getLogin(jwt);
        UsuarioResponseDTO usuario = usuarioService.findCurrent(login);
        return Response.ok(usuario).build();
    }

    @PUT
    @Path("/me")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response updateCurrent(@Valid UsuarioUpdateRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        UsuarioResponseDTO usuario = usuarioService.updateCurrent(login, dto);
        return Response.ok(usuario).build();
    }

    @PUT
    @Path("/me/password")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response changePassword(@Valid UsuarioPasswordChangeRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        usuarioService.changePassword(login, dto);
        return Response.ok().build();
    }

    @GET
    @Path("/me/enderecos")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response getMyEnderecos() {
        String login = JwtUtil.getLogin(jwt);
        List<UsuarioEnderecoResponseDTO> list = enderecoService.listByUsuario(login);
        return Response.ok(list).build();
    }

    @POST
    @Path("/me/enderecos")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response addEndereco(@Valid UsuarioEnderecoRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        UsuarioEnderecoResponseDTO criado = enderecoService.addEndereco(login, dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }
}

