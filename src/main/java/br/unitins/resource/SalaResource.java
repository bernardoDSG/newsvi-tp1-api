package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SalaRequestDTO;
import br.unitins.dto.SalaResponseDTO;
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
        List<SalaResponseDTO> list = service.findAll().stream()
            .map(SalaMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Sala sala = service.findById(id);
        return Response.ok(SalaMapper.toResponseDTO(sala)).build();
    }
    
    @GET
    @Path("/numero/{numero}")
    public Response buscarPorNumero(@PathParam("numero") Integer numero) {
        Sala sala = service.findByNumero(numero);
        return Response.ok(SalaMapper.toResponseDTO(sala)).build();
    }

    // No método criar, você já associa poltronas via poltronasIds
// A sala é dona do relacionamento (unidirecional)

@POST
public Response criar(@Valid SalaRequestDTO dto) {
    try {
        Sala sala = SalaMapper.toEntity(dto);
        
        if (dto.poltronasIds() != null && !dto.poltronasIds().isEmpty()) {
            // Buscar poltronas existentes (NÃO criar novas)
            List<Poltrona> poltronas = dto.poltronasIds().stream()
                .map(id -> poltronaRepository.findById(id))
                .filter(p -> p != null)
                .toList();
            sala.setPoltronas(poltronas);
        }
        
        service.create(sala);
        return Response.status(Status.CREATED)
            .entity(SalaMapper.toResponseDTO(sala))
            .build();
    } catch (Exception e) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
            .entity("Erro ao criar sala: " + e.getMessage())
            .build();
    }
}

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid SalaRequestDTO dto) {
        try {
            Sala existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Sala não encontrada com ID: " + id)
                    .build();
            }
            
            Sala sala = SalaMapper.toEntity(dto);
            sala.setId(id);
            
            if (dto.poltronasIds() != null) {
                List<Poltrona> poltronas = dto.poltronasIds().stream()
                    .map(poltronaId -> poltronaRepository.findById(poltronaId))
                    .filter(p -> p != null)
                    .toList();
                sala.setPoltronas(poltronas);
            }
            
            service.update(id, sala);
            return Response.ok(SalaMapper.toResponseDTO(sala)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar sala: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Sala sala = service.findById(id);
        if (sala == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Sala não encontrada com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}