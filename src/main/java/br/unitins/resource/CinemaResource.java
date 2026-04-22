package br.unitins.resource;

import java.util.List;

import br.unitins.dto.CinemaRequestDTO;
import br.unitins.dto.CinemaResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.CinemaMapper;
import br.unitins.model.Cinema;
import br.unitins.model.Endereco;
import br.unitins.model.Sala;
import br.unitins.repository.EnderecoRepository;
import br.unitins.repository.SalaRepository;
import br.unitins.service.CinemaService;
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

@Path("/cinemas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CinemaResource {

    @Inject
    CinemaService service;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    SalaRepository salaRepository;

    @GET
    public Response buscarTodos() {
        List<CinemaResponseDTO> list = service.findAll().stream().map(CinemaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(CinemaMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<CinemaResponseDTO> list = service.findByNome(nome).stream().map(CinemaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/cidade/{cidade}")
    public Response buscarPorCidade(@PathParam("cidade") String cidade) {
        List<CinemaResponseDTO> list = service.findByCidade(cidade).stream().map(CinemaMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid CinemaRequestDTO dto) {
        Cinema cinema = CinemaMapper.toEntity(dto);
        cinema.setEndereco(loadEndereco(dto.enderecoId()));
        cinema.setSalas(loadSalas(dto.salasIds()));
        Cinema criado = service.create(cinema);
        return Response.status(Status.CREATED).entity(CinemaMapper.toResponseDTO(criado)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid CinemaRequestDTO dto) {
        Cinema cinema = CinemaMapper.toEntity(dto);
        cinema.setId(id);
        cinema.setEndereco(loadEndereco(dto.enderecoId()));
        if (dto.salasIds() != null) {
            cinema.setSalas(loadSalas(dto.salasIds()));
        }
        service.update(id, cinema);
        return Response.ok(CinemaMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private Endereco loadEndereco(Long enderecoId) {
        Endereco endereco = enderecoRepository.findById(enderecoId);
        if (endereco == null) {
            throw new ValidationException("Endereço informado não existe: " + enderecoId, "enderecoId");
        }
        return endereco;
    }

    private List<Sala> loadSalas(List<Long> salasIds) {
        if (salasIds == null) {
            return null;
        }
        return salasIds.stream().map(this::loadSala).toList();
    }

    private Sala loadSala(Long salaId) {
        Sala sala = salaRepository.findById(salaId);
        if (sala == null) {
            throw new ValidationException("Sala informada não existe: " + salaId, "salasIds");
        }
        return sala;
    }
}
