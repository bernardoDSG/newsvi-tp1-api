package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.exception.ConflictException;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.SessaoMapper;
import br.unitins.model.Cinema;
import br.unitins.model.Filme;
import br.unitins.model.Sala;
import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.model.TipoSessao;
import br.unitins.repository.CinemaRepository;
import br.unitins.repository.FilmeRepository;
import br.unitins.repository.SalaRepository;
import br.unitins.service.SessaoService;
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

@Path("/sessoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessaoResource {

    @Inject
    SessaoService service;

    @Inject
    FilmeRepository filmeRepository;

    @Inject
    SalaRepository salaRepository;

    @Inject
    CinemaRepository cinemaRepository;

    @GET
    public Response buscarTodos() {
        List<SessaoResponseDTO> list = service.findAll().stream().map(SessaoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(SessaoMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/filme/{filmeId}")
    public Response buscarPorFilme(@PathParam("filmeId") Long filmeId) {
        List<SessaoResponseDTO> list = service.findByFilme(filmeId).stream().map(SessaoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/cinema/{cinemaId}")
    public Response buscarPorCinema(@PathParam("cinemaId") Long cinemaId) {
        List<SessaoResponseDTO> list = service.findByCinema(cinemaId).stream().map(SessaoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/status/{statusId}")
    public Response buscarPorStatus(@PathParam("statusId") Long statusId) {
        List<SessaoResponseDTO> list = service.findByStatus(statusId).stream().map(SessaoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/em-exibicao")
    public Response buscarSessoesEmExibicao() {
        List<SessaoResponseDTO> list = service.findSessoesEmExibicao(null).stream().map(SessaoMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid SessaoRequestDTO dto) {
        Sessao sessao = SessaoMapper.toEntity(dto);
        sessao.setFilme(loadFilme(dto.filmeId()));
        sessao.setCinema(loadCinema(dto.cinemaId()));
        sessao.setTipo(loadTipo(dto.tipoSessaoId()));
        sessao.setStatus(loadStatus(dto.statusId()));
        sessao.setSalas(loadSalas(dto.salasIds()));

        validarConflitoHorario(sessao, null);

        Sessao criada = service.create(sessao);
        return Response.status(Status.CREATED).entity(SessaoMapper.toResponseDTO(criada)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid SessaoRequestDTO dto) {
        Sessao sessao = SessaoMapper.toEntity(dto);
        sessao.setId(id);
        sessao.setFilme(loadFilme(dto.filmeId()));
        sessao.setCinema(loadCinema(dto.cinemaId()));
        sessao.setTipo(loadTipo(dto.tipoSessaoId()));
        sessao.setStatus(loadStatus(dto.statusId()));
        if (dto.salasIds() != null) {
            sessao.setSalas(loadSalas(dto.salasIds()));
        }

        validarConflitoHorario(sessao, id);

        service.update(id, sessao);
        return Response.ok(SessaoMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private Filme loadFilme(Long filmeId) {
        Filme filme = filmeRepository.findById(filmeId);
        if (filme == null) {
            throw new ValidationException("Filme informado não existe: " + filmeId, "filmeId");
        }
        return filme;
    }

    private Cinema loadCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId);
        if (cinema == null) {
            throw new ValidationException("Cinema informado não existe: " + cinemaId, "cinemaId");
        }
        return cinema;
    }

    private TipoSessao loadTipo(Long tipoSessaoId) {
        TipoSessao tipo = TipoSessao.valueOf(tipoSessaoId);
        if (tipo == null) {
            throw new ValidationException("Tipo de sessão inválido", "tipoSessaoId");
        }
        return tipo;
    }

    private StatusSessao loadStatus(Long statusId) {
        StatusSessao status = StatusSessao.valueOf(statusId);
        if (status == null) {
            throw new ValidationException("Status de sessão inválido", "statusId");
        }
        return status;
    }

    private List<Sala> loadSalas(List<Long> salasIds) {
        if (salasIds == null) {
            return null;
        }
        return salasIds.stream().map(this::loadSala).toList();
    }

    private Sala loadSala(Long salaId) {
        Sala sala = salaRepository.findById(salaId);
        if (sala == null) {
            throw new ValidationException("Sala informada não existe: " + salaId, "salasIds");
        }
        return sala;
    }

    private void validarConflitoHorario(Sessao sessao, Long sessaoId) {
        if (sessao.getSalas() == null) {
            return;
        }
        for (Sala sala : sessao.getSalas()) {
            if (service.existsBySalaAndHorario(sala.getId(), sessao.getInicio(), sessao.getFim(), sessaoId)) {
                throw new ConflictException("Conflito de horário na sala " + sala.getNumero());
            }
        }
    }
}
