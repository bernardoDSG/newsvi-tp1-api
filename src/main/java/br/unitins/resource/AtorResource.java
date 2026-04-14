package br.unitins.resource;

import java.util.List;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.mapper.AtorMapper;
import br.unitins.model.Ator;
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
        List<AtorResponseDTO> list = service.findAll().stream()
            .map(AtorMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Ator ator = service.findById(id);
        return Response.ok(AtorMapper.toResponseDTO(ator)).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<AtorResponseDTO> list = service.findByNome(nome).stream()
            .map(AtorMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum ator encontrado com nome: " + nome)
                .build();
        }
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/premio/{premio}")
    public Response buscarPorPremio(@PathParam("premio") String premio) {
        List<AtorResponseDTO> list = service.findByPremio(premio).stream()
            .map(AtorMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum ator encontrado com prêmio: " + premio)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid AtorRequestDTO dto) {
        try {
            Ator ator = AtorMapper.toEntity(dto);
            
            if (dto.premiosIds() != null && !dto.premiosIds().isEmpty()) {
                ator.setPremios(dto.premiosIds().stream()
                    .map(id -> premioRepository.findById(id))
                    .filter(p -> p != null)
                    .toList());
            }
            
            service.create(ator);
            return Response.status(Status.CREATED)
                .entity(AtorMapper.toResponseDTO(ator))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar ator: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid AtorRequestDTO dto) {
        try {
            Ator existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Ator não encontrado com ID: " + id)
                    .build();
            }
            
            Ator ator = AtorMapper.toEntity(dto);
            ator.setId(id);
            
            if (dto.premiosIds() != null) {
                ator.setPremios(dto.premiosIds().stream()
                    .map(premioId -> premioRepository.findById(premioId))
                    .filter(p -> p != null)
                    .toList());
            }
            
            service.update(id, ator);
            return Response.ok(AtorMapper.toResponseDTO(ator)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar ator: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Ator ator = service.findById(id);
        if (ator == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Ator não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}