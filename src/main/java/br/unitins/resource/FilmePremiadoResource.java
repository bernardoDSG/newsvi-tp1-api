package br.unitins.resource;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import br.unitins.dto.FilmePremiadoRequestDTO;
import br.unitins.dto.FilmePremiadoResponseDTO;
import br.unitins.mapper.FilmePremiadoMapper;
import br.unitins.service.FilmePremiadoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/filmes-premiados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmePremiadoResource {
    @Inject
    FilmePremiadoService service;

    @GET
    public List<FilmePremiadoResponseDTO> buscarTodos() {
        return service.findAll().stream().map(FilmePremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public FilmePremiadoResponseDTO buscarPorId(Long id) {
        return FilmePremiadoMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{nome}")
    public List<FilmePremiadoResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(FilmePremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/anoLancamento/{anoLancamento}")
    public List<FilmePremiadoResponseDTO> buscarPeloAnoLancamento(Integer anoLancamento) {
        return service.findByAnoLancamento(anoLancamento).stream().map(FilmePremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/idiomaOriginal/{idiomaOriginal}")
    public List<FilmePremiadoResponseDTO> buscarPeloIdiomaOriginal(String idiomaOriginal) {
        return service.findByIdiomaOriginal(idiomaOriginal).stream().map(FilmePremiadoMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/premio/{idPremio}")
    public List<FilmePremiadoResponseDTO> buscarPeloPremio(Long idPremio) {
        return service.findByPremio(idPremio).stream().map(FilmePremiadoMapper::toResponseDTO).toList();
    }
    
    @POST
    public FilmePremiadoResponseDTO criar(FilmePremiadoRequestDTO filmePremiadoRequestDTO) {
        return FilmePremiadoMapper.toResponseDTO(service.create(FilmePremiadoMapper.toEntity(filmePremiadoRequestDTO)));
    }

    @PUT
    @Path("/{id}")
    public void atualizar(Long id, FilmePremiadoRequestDTO filmePremiadoRequestDTO) {
        service.update(id, FilmePremiadoMapper.toEntity(filmePremiadoRequestDTO));
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);
    }



}
