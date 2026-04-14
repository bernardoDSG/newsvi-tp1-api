package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.mapper.SessaoMapper;
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
        List<SessaoResponseDTO> list = service.findAll().stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Sessao sessao = service.findById(id);
        return Response.ok(SessaoMapper.toResponseDTO(sessao)).build();
    }
    
    @GET
    @Path("/filme/{filmeId}")
    public Response buscarPorFilme(@PathParam("filmeId") Long filmeId) {
        List<SessaoResponseDTO> list = service.findByFilme(filmeId).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/cinema/{cinemaId}")
    public Response buscarPorCinema(@PathParam("cinemaId") Long cinemaId) {
        List<SessaoResponseDTO> list = service.findByCinema(cinemaId).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/status/{statusId}")
    public Response buscarPorStatus(@PathParam("statusId") Long statusId) {
        List<SessaoResponseDTO> list = service.findByStatus(statusId).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }
    
    @GET
    @Path("/em-exibicao")
    public Response buscarSessoesEmExibicao() {
        List<SessaoResponseDTO> list = service.findSessoesEmExibicao(null).stream()
            .map(SessaoMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid SessaoRequestDTO dto) {
        try {
            Sessao sessao = SessaoMapper.toEntity(dto);
            
            if (dto.filmeId() != null) {
                var filme = filmeRepository.findById(dto.filmeId());
                if (filme == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Filme não encontrado com ID: " + dto.filmeId())
                        .build();
                }
                sessao.setFilme(filme);
            }
            
            if (dto.cinemaId() != null) {
                var cinema = cinemaRepository.findById(dto.cinemaId());
                if (cinema == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Cinema não encontrado com ID: " + dto.cinemaId())
                        .build();
                }
                sessao.setCinema(cinema);
            }
            
            if (dto.tipoSessaoId() != null) {
                TipoSessao tipo = TipoSessao.valueOf(dto.tipoSessaoId());
                if (tipo == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Tipo de sessão inválido")
                        .build();
                }
                sessao.setTipo(tipo);
            }
            
            if (dto.statusId() != null) {
                StatusSessao status = StatusSessao.valueOf(dto.statusId());
                if (status == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Status de sessão inválido")
                        .build();
                }
                sessao.setStatus(status);
            }
            
            if (dto.salasIds() != null && !dto.salasIds().isEmpty()) {
                sessao.setSalas(dto.salasIds().stream()
                    .map(id -> salaRepository.findById(id))
                    .filter(s -> s != null)
                    .toList());
            }
            
            if (sessao.getInicio().isAfter(sessao.getFim())) {
                return Response.status(Status.BAD_REQUEST)
                    .entity("Horário de início não pode ser após o horário de fim")
                    .build();
            }
            
            // Verificar conflito de horário
            for (var sala : sessao.getSalas()) {
                if (service.existsBySalaAndHorario(sala.getId(), sessao.getInicio(), sessao.getFim(), null)) {
                    return Response.status(Status.CONFLICT)
                        .entity("Conflito de horário na sala " + sala.getNumero())
                        .build();
                }
            }
            
            service.create(sessao);
            return Response.status(Status.CREATED)
                .entity(SessaoMapper.toResponseDTO(sessao))
                .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar sessão: " + e.getMessage())
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid SessaoRequestDTO dto) {
        try {
            Sessao existing = service.findById(id);
            if (existing == null) {
                return Response.status(Status.NOT_FOUND)
                    .entity("Sessão não encontrada com ID: " + id)
                    .build();
            }
            
            Sessao sessao = SessaoMapper.toEntity(dto);
            sessao.setId(id);
            
            if (dto.filmeId() != null) {
                var filme = filmeRepository.findById(dto.filmeId());
                if (filme == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Filme não encontrado com ID: " + dto.filmeId())
                        .build();
                }
                sessao.setFilme(filme);
            }
            
            if (dto.cinemaId() != null) {
                var cinema = cinemaRepository.findById(dto.cinemaId());
                if (cinema == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Cinema não encontrado com ID: " + dto.cinemaId())
                        .build();
                }
                sessao.setCinema(cinema);
            }
            
            if (dto.tipoSessaoId() != null) {
                TipoSessao tipo = TipoSessao.valueOf(dto.tipoSessaoId());
                if (tipo == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Tipo de sessão inválido")
                        .build();
                }
                sessao.setTipo(tipo);
            }
            
            if (dto.statusId() != null) {
                StatusSessao status = StatusSessao.valueOf(dto.statusId());
                if (status == null) {
                    return Response.status(Status.BAD_REQUEST)
                        .entity("Status de sessão inválido")
                        .build();
                }
                sessao.setStatus(status);
            }
            
            if (dto.salasIds() != null) {
                sessao.setSalas(dto.salasIds().stream()
                    .map(salaId -> salaRepository.findById(salaId))
                    .filter(s -> s != null)
                    .toList());
            }
            
            if (sessao.getInicio() != null && sessao.getFim() != null && 
                sessao.getInicio().isAfter(sessao.getFim())) {
                return Response.status(Status.BAD_REQUEST)
                    .entity("Horário de início não pode ser após o horário de fim")
                    .build();
            }
            
            // Verificar conflito de horário (excluindo a própria sessão)
            for (var sala : sessao.getSalas()) {
                if (service.existsBySalaAndHorario(sala.getId(), sessao.getInicio(), sessao.getFim(), id)) {
                    return Response.status(Status.CONFLICT)
                        .entity("Conflito de horário na sala " + sala.getNumero())
                        .build();
                }
            }
            
            service.update(id, sessao);
            return Response.ok(SessaoMapper.toResponseDTO(sessao)).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao atualizar sessão: " + e.getMessage())
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        Sessao sessao = service.findById(id);
        if (sessao == null) {
            return Response.status(Status.NOT_FOUND)
                .entity("Sessão não encontrada com ID: " + id)
                .build();
        }
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }
}