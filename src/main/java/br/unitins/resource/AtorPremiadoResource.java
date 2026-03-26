package br.unitins.resource;

import java.util.List;

import br.unitins.dto.AtorPremiadoRequestDTO;
import br.unitins.dto.AtorPremiadoResponseDTO;
import br.unitins.mapper.AtorPremiadoMapper;
import br.unitins.model.AtorPremiado;
import br.unitins.service.AtorPremiadoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/atores-premiados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorPremiadoResource {
    
    @Inject
    AtorPremiadoService service;

    @GET
    public List<AtorPremiadoResponseDTO> buscarTodos() {
        return service.findAll().stream().map(AtorPremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/{nome}")
    public List<AtorPremiadoResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(AtorPremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public AtorPremiadoResponseDTO buscarPorId(Long id) {
        return AtorPremiadoMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("find/premio/{idPremio}")
    public List<AtorPremiadoResponseDTO> buscarPeloIdPremio(Long idPremio) {
        return service.findByPremio(idPremio).stream().map(AtorPremiadoMapper::toResponseDTO).toList();
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);;
    }

    @POST
    public AtorPremiadoResponseDTO criar(AtorPremiadoRequestDTO dto) {
        AtorPremiado atorPremiado = AtorPremiadoMapper.toEntity(dto);
        service.create(atorPremiado);
        return AtorPremiadoMapper.toResponseDTO(atorPremiado);
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, AtorPremiadoRequestDTO dto) {
        service.update(id, AtorPremiadoMapper.toEntity(dto));
        
    }


}
