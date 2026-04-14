package br.unitins.resource;

import java.util.List;

import br.unitins.dto.EnderecoRequestDTO;
import br.unitins.dto.EnderecoResponseDTO;
import br.unitins.mapper.EnderecoMapper;
import br.unitins.model.Endereco;
import br.unitins.service.EnderecoService;
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

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {
    
    @Inject
    EnderecoService service;

    @GET
    public Response buscarTodos() {
        List<EnderecoResponseDTO> list = service.findAll().stream()
            .map(EnderecoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Endereco endereco = service.findById(id);
        return Response.ok(EnderecoMapper.toResponseDTO(endereco)).build();
    }

    @GET
    @Path("/cep/{cep}")
    public Response buscarPorCep(@PathParam("cep") String cep) {
        Endereco endereco = service.findByCep(cep);
        return Response.ok(EnderecoMapper.toResponseDTO(endereco)).build();
    }

    @POST
    public Response criar(@Valid EnderecoRequestDTO dto) {
        try {
            Endereco endereco = EnderecoMapper.toEntity(dto);
            service.create(endereco);
            return Response.status(Status.CREATED)
                .entity(EnderecoMapper.toResponseDTO(endereco))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar endereço: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid EnderecoRequestDTO dto) {
        try {
            Endereco existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Endereço não encontrado com ID: " + id)
                    .build();
            }
            
            Endereco endereco = EnderecoMapper.toEntity(dto);
            endereco.setId(id);
            service.update(id, endereco);
            return Response.ok(EnderecoMapper.toResponseDTO(endereco)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar endereço: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Endereco endereco = service.findById(id);
        if (endereco == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Endereço não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}