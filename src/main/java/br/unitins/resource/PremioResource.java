package br.unitins.resource;
import java.util.List;

import br.unitins.dto.PremioRequestDTO;
import br.unitins.dto.PremioResponseDTO;
import br.unitins.mapper.PremioMapper;
import br.unitins.model.Premio;
import br.unitins.service.PremioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/premios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PremioResource {
    
    @Inject
    PremioService service;

    @GET
    public List<PremioResponseDTO> buscarTodos() {
        return service.findAll().stream().map(PremioMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public PremioResponseDTO buscarPorId (Long id) {
        return PremioMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{nome}")
    public List<PremioResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(PremioMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/ano/{ano}")
    public List<PremioResponseDTO> buscarPeloAno(Integer ano) {
        return service.findByAno(ano).stream().map(PremioMapper::toResponseDTO).toList();
    }

    @POST
    public PremioResponseDTO criar(PremioRequestDTO dto) {
        Premio premio = PremioMapper.toEntity(dto);
        service.create(premio);
        return PremioMapper.toResponseDTO(premio);
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, PremioRequestDTO dto) {
        Premio premio = PremioMapper.toEntity(dto);
        service.update(id, premio);
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);
    }
}
