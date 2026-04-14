package br.unitins.resource;

import java.util.List;

import br.unitins.dto.DiretorRequestDTO;
import br.unitins.dto.DiretorResponseDTO;
import br.unitins.mapper.DiretorMapper;
import br.unitins.model.Diretor;
import br.unitins.service.DiretorService;
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

@Path("/diretores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiretorResource {
    
    @Inject
    DiretorService service;

    @GET
    public Response buscarTodos() {
        List<DiretorResponseDTO> list = service.findAll().stream()
            .map(DiretorMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Diretor diretor = service.findById(id);
        return Response.ok(DiretorMapper.toResponseDTO(diretor)).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<DiretorResponseDTO> list = service.findByNome(nome).stream()
            .map(DiretorMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum diretor encontrado com nome: " + nome)
                .build();
        }
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/nacionalidade/{nacionalidade}")
    public Response buscarPorNacionalidade(@PathParam("nacionalidade") String nacionalidade) {
        List<DiretorResponseDTO> list = service.findByNacionalidade(nacionalidade).stream()
            .map(DiretorMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum diretor encontrado com nacionalidade: " + nacionalidade)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid DiretorRequestDTO dto) {
        try {
            Diretor diretor = DiretorMapper.toEntity(dto);
            service.create(diretor);
            return Response.status(Status.CREATED)
                .entity(DiretorMapper.toResponseDTO(diretor))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar diretor: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid DiretorRequestDTO dto) {
        try {
            Diretor existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Diretor não encontrado com ID: " + id)
                    .build();
            }
            
            Diretor diretor = DiretorMapper.toEntity(dto);
            diretor.setId(id);
            service.update(id, diretor);
            return Response.ok(DiretorMapper.toResponseDTO(diretor)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar diretor: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Diretor diretor = service.findById(id);
        if (diretor == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Diretor não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}