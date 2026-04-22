package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SalaRequestDTO;
import br.unitins.dto.SalaResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.SalaMapper;
import br.unitins.model.Poltrona;
import br.unitins.model.Sala;
import br.unitins.repository.PoltronaRepository;
import br.unitins.service.SalaService;
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

@Path("/salas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalaResource {

    @Inject
    SalaService service;

    @Inject
    PoltronaRepository poltronaRepository;

    @GET
    public Response buscarTodos() {
        List<SalaResponseDTO> list = service.findAll().stream().map(SalaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(SalaMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/numero/{numero}")
    public Response buscarPorNumero(@PathParam("numero") Integer numero) {
        return Response.ok(SalaMapper.toResponseDTO(service.findByNumero(numero))).build();
    }

    @POST
    public Response criar(@Valid SalaRequestDTO dto) {
        Sala sala = SalaMapper.toEntity(dto);
        sala.setPoltronas(loadPoltronas(dto.poltronasIds()));
        Sala criada = service.create(sala);
        return Response.status(Status.CREATED).entity(SalaMapper.toResponseDTO(criada)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid SalaRequestDTO dto) {
        Sala sala = SalaMapper.toEntity(dto);
        sala.setId(id);
        if (dto.poltronasIds() != null) {
            sala.setPoltronas(loadPoltronas(dto.poltronasIds()));
        }
        service.update(id, sala);
        return Response.ok(SalaMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private List<Poltrona> loadPoltronas(List<Long> poltronasIds) {
        if (poltronasIds == null) {
            return null;
        }
        return poltronasIds.stream().map(this::loadPoltrona).toList();
    }

    private Poltrona loadPoltrona(Long poltronaId) {
        Poltrona poltrona = poltronaRepository.findById(poltronaId);
        if (poltrona == null) {
            throw new ValidationException("Poltrona informada não existe: " + poltronaId, "poltronasIds");
        }
        return poltrona;
    }
}
