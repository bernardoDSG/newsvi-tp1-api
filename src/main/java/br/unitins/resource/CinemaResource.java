package br.unitins.resource;

import java.util.List;

import br.unitins.dto.CinemaRequestDTO;
import br.unitins.dto.CinemaResponseDTO;
import br.unitins.mapper.CinemaMapper;
import br.unitins.mapper.EnderecoMapper;
import br.unitins.model.Cinema;
import br.unitins.model.Endereco;
import br.unitins.repository.EnderecoRepository;
import br.unitins.service.CinemaService;
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

@Path("/cinemas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CinemaResource {
    
    @Inject
    CinemaService service;
    
    @Inject
    EnderecoRepository enderecoRepository;

    @GET
    public Response buscarTodos() {
        List<CinemaResponseDTO> list = service.findAll().stream()
            .map(CinemaMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Cinema cinema = service.findById(id);
        return Response.ok(CinemaMapper.toResponseDTO(cinema)).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<CinemaResponseDTO> list = service.findByNome(nome).stream()
            .map(CinemaMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum cinema encontrado com nome: " + nome)
                .build();
        }
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/cidade/{cidade}")
    public Response buscarPorCidade(@PathParam("cidade") String cidade) {
        List<CinemaResponseDTO> list = service.findByCidade(cidade).stream()
            .map(CinemaMapper::toResponseDTO)
            .toList();
        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                .entity("Nenhum cinema encontrado na cidade: " + cidade)
                .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid CinemaRequestDTO dto) {
        try {
            Cinema cinema = CinemaMapper.toEntity(dto);
            
            if (dto.endereco() != null) {
                Endereco endereco = EnderecoMapper.toEntity(dto.endereco());
                endereco.setCinema(cinema);
                enderecoRepository.persist(endereco);
                cinema.setEndereco(endereco);
            }
            
            service.create(cinema);
            return Response.status(Status.CREATED)
                .entity(CinemaMapper.toResponseDTO(cinema))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar cinema: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid CinemaRequestDTO dto) {
        try {
            Cinema existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Cinema não encontrado com ID: " + id)
                    .build();
            }
            
            Cinema cinema = CinemaMapper.toEntity(dto);
            cinema.setId(id);
            
            if (dto.endereco() != null) {
                if (existing.getEndereco() != null) {
                    existing.getEndereco().setLogradouro(dto.endereco().logradouro());
                    existing.getEndereco().setNumero(dto.endereco().numero());
                    existing.getEndereco().setComplemento(dto.endereco().complemento());
                    existing.getEndereco().setBairro(dto.endereco().bairro());
                    existing.getEndereco().setCidade(dto.endereco().cidade());
                    existing.getEndereco().setEstado(dto.endereco().estado());
                    existing.getEndereco().setCep(dto.endereco().cep());
                } else {
                    Endereco endereco = EnderecoMapper.toEntity(dto.endereco());
                    endereco.setCinema(cinema);
                    enderecoRepository.persist(endereco);
                    cinema.setEndereco(endereco);
                }
            }
            
            service.update(id, cinema);
            return Response.ok(CinemaMapper.toResponseDTO(cinema)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar cinema: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Cinema cinema = service.findById(id);
        if (cinema == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Cinema não encontrado com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}