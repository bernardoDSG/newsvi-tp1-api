package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SalaRequestDTO;
import br.unitins.dto.SalaResponseDTO;
import br.unitins.mapper.SalaMapper;
import br.unitins.model.Sala;
import br.unitins.service.SalaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/salas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SalaResource {
    
    @Inject
    SalaService service;

    @GET
    public List<SalaResponseDTO> buscarTodos() {
        return service.findAll().stream().map(SalaMapper :: toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public SalaResponseDTO buscarPorId(Long id) {
        return SalaMapper.toResponseDTO(service.findById(id));
    }

    @POST
    public SalaResponseDTO criar(SalaRequestDTO dto) {
        Sala sala = SalaMapper.toEntity(dto);
        service.create(sala);
        return SalaMapper.toResponseDTO(sala);
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, SalaRequestDTO dto) {
        Sala sala = SalaMapper.toEntity(dto);
        service.update(id, sala);
    }

    @DELETE
    @Path("/{id}")
    public void deletar(Long id) {
        service.delete(id);
    }
}
