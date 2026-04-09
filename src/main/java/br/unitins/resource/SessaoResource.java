package br.unitins.resource;

import java.util.List;

import br.unitins.dto.SessaoRequestDTO;
import br.unitins.dto.SessaoResponseDTO;
import br.unitins.mapper.SessaoMapper;
import br.unitins.model.Sala;
import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.model.TipoSessao;
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
        if (sessao == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Sessão não encontrada com ID: " + id)
                    .build();
        }
        return Response.ok(SessaoMapper.toResponseDTO(sessao)).build();
    }

    @POST
    public Response criar(@Valid SessaoRequestDTO dto) {
        try {
            Sessao sessao = SessaoMapper.toEntity(dto);

            // 1. Validar Filme
            if (dto.filmeId() != null) {
                var filme = filmeRepository.findById(dto.filmeId());
                if (filme == null) {
                    return Response.status(Status.BAD_REQUEST)
                            .entity("Filme não encontrado com ID: " + dto.filmeId())
                            .build();
                }
                sessao.setFilme(filme);
            }

            // 2. Validar Tipo de Sessão
            if (dto.tipoSessaoId() != null) {
                TipoSessao tipo = TipoSessao.valueOf(dto.tipoSessaoId());
                if (tipo == null) {
                    return Response.status(Status.BAD_REQUEST)
                            .entity("Tipo de sessão inválido. Valores válidos: 1=DUBLADA, 2=LEGENDADA")
                            .build();
                }
                sessao.setTipo(tipo);
            }

            // 3. Validar Status da Sessão (NOVO)
            if (dto.statusId() != null) {
                StatusSessao status = StatusSessao.valueOf(dto.statusId());
                if (status == null) {
                    return Response.status(Status.BAD_REQUEST)
                            .entity("Status de sessão inválido. Valores válidos: 1=EM_BREVE, 2=EM_EXIBICAO, 3=ENCERRADO, 4=CANCELADA")
                            .build();
                }
                sessao.setStatus(status);
            } else {
                // Valor padrão se não informado
                sessao.setStatus(StatusSessao.EM_BREVE);
            }

            // 4. Validar Salas
            if (dto.salasIds() != null && !dto.salasIds().isEmpty()) {
                List<Sala> salasEncontradas = dto.salasIds().stream()
                        .map(id -> salaRepository.findById(id))
                        .filter(s -> s != null)
                        .toList();

                if (salasEncontradas.size() != dto.salasIds().size()) {
                    return Response.status(Status.BAD_REQUEST)
                            .entity("Uma ou mais salas não foram encontradas")
                            .build();
                }
                sessao.setSalas(salasEncontradas);
            } else {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Pelo menos uma sala deve ser associada à sessão")
                        .build();
            }

            // 5. Validar horários
            if (sessao.getInicio() == null || sessao.getFim() == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Horário de início e fim são obrigatórios")
                        .build();
            }

            if (sessao.getInicio().isAfter(sessao.getFim())) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Horário de início não pode ser após o horário de fim")
                        .build();
            }

            // 6. Validar capacidade (NOVO)
            if (sessao.getCapacidadeTotal() == null || sessao.getCapacidadeTotal() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Capacidade total deve ser maior que zero")
                        .build();
            }

            if (sessao.getCapacidadeDisponivel() == null || sessao.getCapacidadeDisponivel() < 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Capacidade disponível não pode ser negativa")
                        .build();
            }

            if (sessao.getCapacidadeDisponivel() > sessao.getCapacidadeTotal()) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Capacidade disponível não pode ser maior que capacidade total")
                        .build();
            }

            // 7. Validar preço (NOVO)
            if (sessao.getPreco() == null || sessao.getPreco() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Preço deve ser maior que zero")
                        .build();
            }

            // 8. Validar se já existe sessão no mesmo horário para a mesma sala (NOVO)
            for (Sala sala : sessao.getSalas()) {
                boolean conflito = service.existsBySalaAndHorario(sala.getId(), sessao.getInicio(), sessao.getFim());
                if (conflito) {
                    return Response.status(Status.CONFLICT)
                            .entity("Já existe uma sessão agendada para a sala " + sala.getId() +
                                    " no horário " + sessao.getInicio() + " a " + sessao.getFim())
                            .build();
                }
            }

            service.create(sessao);
            return Response.status(Status.CREATED)
                    .entity(SessaoMapper.toResponseDTO(sessao))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
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

            if (dto.tipoSessaoId() != null) {
                TipoSessao tipo = TipoSessao.valueOf(dto.tipoSessaoId());
                if (tipo == null) {
                    return Response.status(Status.BAD_REQUEST)
                            .entity("Tipo de sessão inválido")
                            .build();
                }
                sessao.setTipo(tipo);
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