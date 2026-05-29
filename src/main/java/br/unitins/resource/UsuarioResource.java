package br.unitins.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.dto.AlterarSenhaRequestDTO;
import br.unitins.dto.EsqueciSenhaRequestDTO;
import br.unitins.dto.UsuarioCadastroCompletoRequestDTO;
import br.unitins.dto.UsuarioCadastroRequestDTO;
import br.unitins.dto.UsuarioEnderecoRequestDTO;
import br.unitins.dto.UsuarioEnderecoResponseDTO;
import br.unitins.dto.UsuarioResponseDTO;
import br.unitins.dto.UsuarioUpdateRequestDTO;
import br.unitins.model.Perfil;
import br.unitins.service.KeycloakPasswordService;
import br.unitins.service.UsuarioEnderecoService;
import br.unitins.service.UsuarioService;
import br.unitins.util.JwtUtil;
import jakarta.annotation.security.PermitAll;
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
    KeycloakPasswordService passwordService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/esqueci-senha")
    @PermitAll
    public Response esqueciSenha(@Valid EsqueciSenhaRequestDTO dto) {
        passwordService.enviarEmailRedefinicaoSenha(dto.login());
        return Response.noContent().build();
    }

    @POST
    @Path("/me/cadastro")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response createCurrent(@Valid UsuarioCadastroRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        UsuarioResponseDTO usuario = usuarioService.createCurrent(login, resolvePerfil(), dto);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @POST
    @Path("/me/cadastro/completo")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response createCurrentCompleto(@Valid UsuarioCadastroCompletoRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        UsuarioResponseDTO usuario = usuarioService.createCurrentCompleto(login, resolvePerfil(), dto);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

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
    @Path("/me/senha")
    @RolesAllowed({"CLIENTE", "ADMIN"})
    public Response alterarSenha(@Valid AlterarSenhaRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        passwordService.alterarSenha(login, dto.senhaAtual(), dto.novaSenha());
        return Response.noContent().build();
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

    private Perfil resolvePerfil() {
        return jwt.getGroups() != null && jwt.getGroups().contains("ADMIN")
                ? Perfil.ADMIN
                : Perfil.CLIENTE;
    }
}
