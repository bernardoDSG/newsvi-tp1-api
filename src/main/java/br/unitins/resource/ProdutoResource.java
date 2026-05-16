package br.unitins.resource;

import java.util.List;

import br.unitins.dto.ProdutoResponseDTO;
import br.unitins.mapper.ProdutoMapper;
import br.unitins.model.Sessao;
import br.unitins.repository.SessaoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    SessaoRepository sessaoRepository;

    @GET
    public Response search(@QueryParam("nome") String nome) {
        List<ProdutoResponseDTO> list = sessaoRepository.findByFilmeNome(nome)
                .stream()
                .map(ProdutoMapper::toResponseDTO)
                .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Sessao sessao = sessaoRepository.findById(id);
        if (sessao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ProdutoMapper.toResponseDTO(sessao)).build();
    }
}

