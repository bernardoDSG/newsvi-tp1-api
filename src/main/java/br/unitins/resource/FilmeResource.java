package br.unitins.resource;

import java.util.List;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.mapper.FilmeMapper;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Filme;
import br.unitins.repository.AtorRepository;
import br.unitins.repository.GeneroRepository;
import br.unitins.service.FilmeService;
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

@Path("/filmes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmeResource {
    
    @Inject
    FilmeService service;

    @Inject
    GeneroRepository generoRepository;

    @Inject
    AtorRepository atorRepository;

    @GET
    public Response buscarTodos() {
        List<FilmeResponseDTO> list = service.findAll().stream()
            .map(FilmeMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Filme filme = service.findById(id);
        if (filme == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Filme não encontrado com ID: " + id)
                .build();
        }
        return Response.ok(FilmeMapper.toResponseDTO(filme)).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<FilmeResponseDTO> list = service.findByNome(nome).stream()
            .map(FilmeMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum filme encontrado com nome: " + nome)
                .build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/genero/{genero}")
    public Response buscarPorGenero(@PathParam("genero") String genero) {
        List<FilmeResponseDTO> list = service.findByGenero(genero).stream()
            .map(FilmeMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum filme encontrado para o gênero: " + genero)
                .build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/ator/{ator}")
    public Response buscarPorAtor(@PathParam("ator") String ator) {
        List<FilmeResponseDTO> list = service.findByAtor(ator).stream()
            .map(FilmeMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum filme encontrado para o ator: " + ator)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid FilmeRequestDTO dto) {
        try {
            Filme filme = FilmeMapper.toEntity(dto);
            
            if (dto.classificacaoIndicativaId() != null) {
                ClassificacaoIndicativa classificacao = ClassificacaoIndicativa.valueOf(dto.classificacaoIndicativaId());
                if (classificacao == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Classificação indicativa inválida")
                        .build();
                }
                filme.setClassificacaoIndicativa(classificacao);
            }
            
            if (dto.generosIds() != null && !dto.generosIds().isEmpty()) {
                filme.setGeneros(dto.generosIds().stream()
                    .map(id -> generoRepository.findById(id))
                    .filter(g -> g != null)
                    .toList());
            }
            
            if (dto.atoresIds() != null && !dto.atoresIds().isEmpty()) {
                filme.setAtores(dto.atoresIds().stream()
                    .map(id -> atorRepository.findById(id))
                    .filter(a -> a != null)
                    .toList());
            }
            
            service.create(filme);
            return Response.status(Status.CREATED)
                .entity(FilmeMapper.toResponseDTO(filme))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar filme: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid FilmeRequestDTO dto) {
        try {
            Filme existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Filme não encontrado com ID: " + id)
                    .build();
            }
            
            Filme filme = FilmeMapper.toEntity(dto);
            filme.setId(id);
            
            if (dto.classificacaoIndicativaId() != null) {
                ClassificacaoIndicativa classificacao = ClassificacaoIndicativa.valueOf(dto.classificacaoIndicativaId());
                if (classificacao == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Classificação indicativa inválida")
                        .build();
                }
                filme.setClassificacaoIndicativa(classificacao);
            }
            
            if (dto.generosIds() != null) {
                filme.setGeneros(dto.generosIds().stream()
                    .map(generoId -> generoRepository.findById(generoId))
                    .filter(g -> g != null)
                    .toList());
            }
            
            if (dto.atoresIds() != null) {
                filme.setAtores(dto.atoresIds().stream()
                    .map(atorId -> atorRepository.findById(atorId))
                    .filter(a -> a != null)
                    .toList());
            }
            
            service.update(id, filme);
            return Response.ok(FilmeMapper.toResponseDTO(filme)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar filme: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Filme filme = service.findById(id);
        if (filme == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Filme não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}