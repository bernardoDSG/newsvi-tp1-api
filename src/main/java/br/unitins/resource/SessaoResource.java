package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.mapper.SessaoMapper;
import br.unitins.model.Sessao;
import br.unitins.service.SessaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sessoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessaoResource {
    
    @Inject
    SessaoService service;

    @GET
    public List<SessaoResponseDTO> findAll() {
        return service.findAll().stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
    }

    @GET
    @Path("/{id}")
    public SessaoResponseDTO findById(Long id) {
        return SessaoMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/tipo/{idTipo}")
    public List<SessaoResponseDTO> findByTipoSessao(Long idTipo) {
        return service.findByTipoSessao(idTipo).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
    }

    @GET
    @Path("/sala/{idSala}")
    public List<SessaoResponseDTO> findBySala(Long idSala) {
        return service.findBySala(idSala).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
    }

    @GET
    @Path("/filme/{idFilme}")
    public List<SessaoResponseDTO> findByFilme(Long idFilme) {
        return service.findByFilme(idFilme).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
    }

    @POST
    public SessaoResponseDTO create(SessaoRequestDTO requestDTO) {
        Sessao sessao = SessaoMapper.toEntity(requestDTO);
        return SessaoMapper.toResponseDTO(service.create(sessao));
    }

    @PUT
    @Path("/{id}")
    public void update(Long id, SessaoRequestDTO requestDTO) {
        Sessao sessao = SessaoMapper.toEntity(requestDTO);
        service.update(id, sessao);
    }

    @DELETE
    @Path("/{id}")
    public void delete(Long id) {
        service.delete(id);
    }
}
