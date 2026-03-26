package br.unitins.resource;

import java.util.List;

import br.unitins.dto.AtorRequestDTO;
import br.unitins.dto.AtorResponseDTO;
import br.unitins.mapper.AtorMapper;
import br.unitins.model.Ator;
import br.unitins.service.AtorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/atores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorResource {
    
    @Inject
    AtorService service;

    @GET
    public List<AtorResponseDTO> buscarTodos() {
        return service.findAll().stream().map(AtorMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public AtorResponseDTO buscarPorId( Long id) {
        return AtorMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{nome}")
    public List<AtorResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(AtorMapper::toResponseDTO).toList();
    }

    @POST
    public AtorResponseDTO criar(AtorRequestDTO dto) {
        Ator ator = AtorMapper.toEntity(dto);
        service.create(ator);
        return AtorMapper.toResponseDTO(ator);    
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, AtorRequestDTO dto) {
        Ator ator = AtorMapper.toEntity(dto);
        service.update(id, ator);
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);
    }

}
