package br.unitins.resource;

import java.util.List;

import br.unitins.dto.GeneroRequestDTO;
import br.unitins.dto.GeneroResponseDTO;
import br.unitins.mapper.GeneroMapper;
import br.unitins.model.Genero;
import br.unitins.service.GeneroService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/generos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneroResource {

    @Inject
    GeneroService service;

    @GET
    public List<GeneroResponseDTO> buscarTodos() {
        return service.findAll().stream().map(GeneroMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public GeneroResponseDTO buscarPorId(Long id) {
        return GeneroMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{nome}")
    public List<GeneroResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(GeneroMapper::toResponseDTO).toList();
    }

    @POST
    public GeneroResponseDTO criar(GeneroRequestDTO dto) {
        Genero genero = GeneroMapper.toEntity(dto);
        service.create(genero);
        return GeneroMapper.toResponseDTO(genero);
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, GeneroRequestDTO dto) {
        Genero genero = GeneroMapper.toEntity(dto);
        service.update(id, genero);
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);
    }

}
