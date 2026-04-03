package br.unitins.resource;

import java.util.List;

import br.unitins.dto.GeneroRequestDTO;
import br.unitins.dto.GeneroResponseDTO;
import br.unitins.mapper.GeneroMapper;
import br.unitins.model.Genero;
import br.unitins.service.GeneroService;
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

@Path("/generos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneroResource {
    
    @Inject
    GeneroService service;

    @GET
    public Response buscarTodos() {
        List<GeneroResponseDTO> list = service.findAll().stream()
            .map(GeneroMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Genero genero = service.findById(id);
        if (genero == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Gênero não encontrado com ID: " + id)
                .build();
        }
        return Response.ok(GeneroMapper.toResponseDTO(genero)).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<GeneroResponseDTO> list = service.findByNome(nome).stream()
            .map(GeneroMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum gênero encontrado com nome: " + nome)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid GeneroRequestDTO dto) {
        try {
            Genero genero = GeneroMapper.toEntity(dto);
            service.create(genero);
            return Response.status(Status.CREATED)
                .entity(GeneroMapper.toResponseDTO(genero))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar gênero: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid GeneroRequestDTO dto) {
        try {
            Genero existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Gênero não encontrado com ID: " + id)
                    .build();
            }
            
            Genero genero = GeneroMapper.toEntity(dto);
            genero.setId(id);
            service.update(id, genero);
            return Response.ok(GeneroMapper.toResponseDTO(genero)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar gênero: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Genero genero = service.findById(id);
        if (genero == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Gênero não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}