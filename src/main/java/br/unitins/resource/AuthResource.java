package br.unitins.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.dto.UsuarioCompleteRegisterRequestDTO;
import br.unitins.dto.UsuarioPasswordForgotRequestDTO;
import br.unitins.dto.UsuarioRegisterRequestDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.service.UsuarioService;
import br.unitins.util.JwtUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JsonWebToken jwt;

    
    @GET
    @Path("/login")
    public Response redirectToKeycloak() {
        return Response.status(Response.Status.FOUND)
                .location(java.net.URI.create("/"))
                .build();
    }

    
    @GET
    @Path("/me")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response getCurrentUser() {
        String login = JwtUtil.getLogin(jwt);
        UsuarioResponseDTO response = usuarioService.findCurrent(login);
        return Response.ok(response).build();
    }

    
    @POST
    @Path("/register")
    public Response register(@Valid UsuarioRegisterRequestDTO dto) {
        UsuarioResponseDTO response = usuarioService.register(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    
    @POST
    @Path("/register/completo")
    public Response registerComplete(@Valid UsuarioCompleteRegisterRequestDTO dto) {
        UsuarioResponseDTO response = usuarioService.registerComplete(dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    
    @POST
    @Path("/forgot-password")
    public Response forgotPassword(@Valid UsuarioPasswordForgotRequestDTO dto) {
        usuarioService.forgotPassword(dto);
        return Response.ok().build();
    }

    
    @GET
    @Path("/logout")
    public Response logout() {
        return Response.status(Response.Status.FOUND)
                .location(java.net.URI.create("/"))
                .build();
    }
}

