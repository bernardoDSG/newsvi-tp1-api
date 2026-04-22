package br.unitins.resource;

import java.util.List;

import br.unitins.dto.PoltronaRequestDTO;
import br.unitins.dto.PoltronaResponseDTO;
import br.unitins.exception.ValidationException;
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
        List<PoltronaResponseDTO> list = service.findAll().stream().map(PoltronaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(PoltronaMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/find/{codigo}")
    public Response buscarPeloCodigo(@PathParam("codigo") String codigo) {
        List<PoltronaResponseDTO> list = service.findByCodigo(codigo).stream().map(PoltronaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/disponibilidade/{id}")
    public Response buscarPorDisponibilidade(@PathParam("id") Long disponibilidadeId) {
        List<PoltronaResponseDTO> list = service.findByDisponibilidade(disponibilidadeId).stream()
            .map(PoltronaMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid PoltronaRequestDTO dto) {
        Poltrona poltrona = PoltronaMapper.toEntity(dto);
        poltrona.setDisponibilidade(loadDisponibilidade(dto.disponibilidadeId()));
        Poltrona criada = service.create(poltrona);
        return Response.status(Status.CREATED).entity(PoltronaMapper.toResponseDTO(criada)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid PoltronaRequestDTO dto) {
        Poltrona poltrona = PoltronaMapper.toEntity(dto);
        poltrona.setId(id);
        poltrona.setDisponibilidade(loadDisponibilidade(dto.disponibilidadeId()));
        service.update(id, poltrona);
        return Response.ok(PoltronaMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private Disponibilidade loadDisponibilidade(Long disponibilidadeId) {
        Disponibilidade disponibilidade = Disponibilidade.valueOf(disponibilidadeId);
        if (disponibilidade == null) {
            throw new ValidationException("Disponibilidade inválida", "disponibilidadeId");
        }
        return disponibilidade;
    }
}
