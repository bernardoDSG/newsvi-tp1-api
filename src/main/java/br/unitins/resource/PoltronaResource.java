package br.unitins.resource;

import java.util.List;

import br.unitins.dto.PoltronaRequestDTO;
import br.unitins.dto.PoltronaResponseDTO;
import br.unitins.mapper.PoltronaMapper;
import br.unitins.model.Disponibilidade;
import br.unitins.model.Poltrona;
import br.unitins.service.PoltronaService;
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

@Path("/poltronas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PoltronaResource {
    
    @Inject
    PoltronaService service;

    @GET
    public Response buscarTodos() {
        List<PoltronaResponseDTO> list = service.findAll().stream()
            .map(PoltronaMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Poltrona poltrona = service.findById(id);
        if (poltrona == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Poltrona não encontrada com ID: " + id)
                .build();
        }
        return Response.ok(PoltronaMapper.toResponseDTO(poltrona)).build();
    }

    @GET
    @Path("/find/{codigo}")
    public Response buscarPeloCodigo(@PathParam("codigo") String codigo) {
        List<PoltronaResponseDTO> list = service.findByCodigo(codigo).stream()
            .map(PoltronaMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhuma poltrona encontrada com código: " + codigo)
                .build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/disponibilidade/{id}")
    public Response buscarPorDisponibilidade(@PathParam("id") Long disponibilidadeId) {
        List<PoltronaResponseDTO> list = service.findByDisponibilidade(disponibilidadeId).stream()
            .map(PoltronaMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhuma poltrona encontrada para disponibilidade ID: " + disponibilidadeId)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid PoltronaRequestDTO dto) {
        try {
            Poltrona poltrona = PoltronaMapper.toEntity(dto);
            
            if (dto.disponibilidadeId() != null) {
                Disponibilidade disponibilidade = Disponibilidade.valueOf(dto.disponibilidadeId());
                if (disponibilidade == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Disponibilidade inválida")
                        .build();
                }
                poltrona.setDisponibilidade(disponibilidade);
            }
            
            service.create(poltrona);
            return Response.status(Status.CREATED)
                .entity(PoltronaMapper.toResponseDTO(poltrona))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar poltrona: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid PoltronaRequestDTO dto) {
        try {
            Poltrona existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Poltrona não encontrada com ID: " + id)
                    .build();
            }
            
            Poltrona poltrona = PoltronaMapper.toEntity(dto);
            poltrona.setId(id);
            
            if (dto.disponibilidadeId() != null) {
                Disponibilidade disponibilidade = Disponibilidade.valueOf(dto.disponibilidadeId());
                if (disponibilidade == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Disponibilidade inválida")
                        .build();
                }
                poltrona.setDisponibilidade(disponibilidade);
            }
            
            service.update(id, poltrona);
            return Response.ok(PoltronaMapper.toResponseDTO(poltrona)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar poltrona: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Poltrona poltrona = service.findById(id);
        if (poltrona == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Poltrona não encontrada com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}