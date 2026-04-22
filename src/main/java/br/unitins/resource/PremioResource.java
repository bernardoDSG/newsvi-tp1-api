package br.unitins.resource;

import java.time.Year;
import java.util.List;

import br.unitins.dto.PremioRequestDTO;
import br.unitins.dto.PremioResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.PremioMapper;
import br.unitins.model.Premio;
import br.unitins.service.PremioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/premios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PremioResource {

    @Inject
    PremioService service;

    @GET
    public Response buscarTodos() {
        List<PremioResponseDTO> list = service.findAll().stream().map(PremioMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(PremioMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<PremioResponseDTO> list = service.findByNome(nome).stream().map(PremioMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/categoria/{categoria}")
    public Response buscarPorCategoria(@PathParam("categoria") String categoria) {
        List<PremioResponseDTO> list = service.findByCategoria(categoria).stream().map(PremioMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid PremioRequestDTO dto) {
        validateAno(dto.ano());
        Premio criado = service.create(PremioMapper.toEntity(dto));
        return Response.status(Status.CREATED).entity(PremioMapper.toResponseDTO(criado)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid PremioRequestDTO dto) {
        validateAno(dto.ano());
        Premio premio = PremioMapper.toEntity(dto);
        premio.setId(id);
        service.update(id, premio);
        return Response.ok(PremioMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private void validateAno(Integer ano) {
        int anoAtual = Year.now().getValue();
        if (ano != null && ano > anoAtual) {
            throw new ValidationException("Ano não pode ser futuro. Ano atual: " + anoAtual, "ano");
        }
    }
}
