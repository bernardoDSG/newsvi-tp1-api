package br.unitins.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.unitins.dto.DesejoRequestDTO;
import br.unitins.dto.DesejoResponseDTO;
import br.unitins.service.DesejoService;
import br.unitins.util.JwtUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/desejos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DesejoResource {

    @Inject
    DesejoService desejoService;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("CLIENTE")
    public Response list() {
        String login = JwtUtil.getLogin(jwt);
        List<DesejoResponseDTO> list = desejoService.listByUsuario(login);
        return Response.ok(list).build();
    }

    @POST
    @RolesAllowed("CLIENTE")
    public Response add(@Valid DesejoRequestDTO dto) {
        String login = JwtUtil.getLogin(jwt);
        DesejoResponseDTO response = desejoService.add(login, dto);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("CLIENTE")
    public Response remove(@PathParam("id") Long id) {
        String login = JwtUtil.getLogin(jwt);
        desejoService.remove(login, id);
        return Response.noContent().build();
    }

    private Long parseLongOrNull(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

