package br.unitins.resource;

import java.util.List;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.AtorMapper;
import br.unitins.model.Ator;
import br.unitins.model.Premio;
import br.unitins.repository.PremioRepository;
import br.unitins.service.AtorService;
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

@Path("/atores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorResource {

    @Inject
    AtorService service;

    @Inject
    PremioRepository premioRepository;

    @GET
    public Response buscarTodos() {
        List<AtorResponseDTO> list = service.findAll().stream().map(AtorMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(AtorMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<AtorResponseDTO> list = service.findByNome(nome).stream().map(AtorMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/premio/{premio}")
    public Response buscarPorPremio(@PathParam("premio") String premio) {
        List<AtorResponseDTO> list = service.findByPremio(premio).stream().map(AtorMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid AtorRequestDTO dto) {
        Ator ator = AtorMapper.toEntity(dto);
        ator.setPremios(loadPremios(dto.premiosIds()));
        Ator criado = service.create(ator);
        return Response.status(Status.CREATED).entity(AtorMapper.toResponseDTO(criado)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid AtorRequestDTO dto) {
        Ator ator = AtorMapper.toEntity(dto);
        ator.setId(id);
        if (dto.premiosIds() != null) {
            ator.setPremios(loadPremios(dto.premiosIds()));
        }
        service.update(id, ator);
        return Response.ok(AtorMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private List<Premio> loadPremios(List<Long> premiosIds) {
        if (premiosIds == null) {
            return null;
        }
        return premiosIds.stream().map(this::loadPremio).toList();
    }

    private Premio loadPremio(Long premioId) {
        Premio premio = premioRepository.findById(premioId);
        if (premio == null) {
            throw new ValidationException("Prêmio informado não existe: " + premioId, "premiosIds");
        }
        return premio;
    }
}
