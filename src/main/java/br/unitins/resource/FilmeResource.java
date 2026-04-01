package br.unitins.resource;

import java.util.List;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.mapper.FilmeMapper;
import br.unitins.model.Filme;
import br.unitins.service.FilmeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/filmes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmeResource {

    @Inject
    FilmeService service;

    @GET
    public List<FilmeResponseDTO> buscarTodos() {
        return service.findAll().stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/{id}")
    public FilmeResponseDTO buscarPorId(Long id) {
        return FilmeMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/{nome}")
    public List<FilmeResponseDTO> buscarPeloNome(String nome) {
        return service.findByNome(nome).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/anoLancamento/{anoLancamento}")
    public List<FilmeResponseDTO> buscarPeloAnoLancamento(Integer anoLancamento) {
        return service.findByAnoLancamento(anoLancamento).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/idiomaOriginal/{idiomaOriginal}")
    public List<FilmeResponseDTO> buscarPeloIdiomaOriginal(String idiomaOriginal) {
        return service.findByIdiomaOriginal(idiomaOriginal).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/classificacaoIndicativa/{idClassificacaoIndicativa}")
    public List<FilmeResponseDTO> buscarPelaClassificacaoIndicativa(Long idClassificacaoIndicativa) {
        return service.findByClassificacaoIndicativa(idClassificacaoIndicativa).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/genero/{idGenero}")
    public List<FilmeResponseDTO> buscarPeloGenero(Long idGenero) {
        return service.findByGenero(idGenero).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @GET
    @Path("/find/ator/{idAtor}")
    public List<FilmeResponseDTO> buscarPeloAtor(Long idAtor) {
        return service.findByAtor(idAtor).stream().map(FilmeMapper::toResponseDTO).toList();
    }

    @POST
    public FilmeResponseDTO criar(FilmeRequestDTO dto) {
        var filme = FilmeMapper.toEntity(dto);
        service.create(filme);
        return FilmeMapper.toResponseDTO(filme);
    }

    @PUT
    @Path("/{id}")
    public void alterar(Long id, FilmeRequestDTO dto) {
        Filme filme = FilmeMapper.toEntity(dto);
        service.update(id, filme);
    }

    @DELETE
    @Path("/{id}")
    public void deletarPorId(Long id) {
        service.delete(id);
    }
    


}
