package br.unitins.resource;

import java.util.List;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.mapper.FilmeMapper;
import br.unitins.mapper.PremioMapper;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Filme;
import br.unitins.repository.AtorRepository;
import br.unitins.repository.GeneroRepository;
import br.unitins.repository.PremioRepository;
import br.unitins.service.FilmeService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

    @Inject
    PremioRepository premioRepository;

    // ==================== GET (buscar todos) ====================
    @SuppressWarnings("null")
    @GET
    public Response buscarTodos() {
        List<FilmeResponseDTO> list = service.findAll().stream()
                .map(filme -> new FilmeResponseDTO(
                        filme.getId(),
                        filme.getNome(),
                        filme.getDuracao(),
                        filme.getDuracaoMinutos(),
                        filme.getSinopse(),
                        filme.getIdiomaOriginal(),
                        filme.getAnoLancamento(),
                        filme.getImagemPoster(),
                        filme.getTrailerUrl(),
                        filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME()
                                : null,
                        filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                        filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                        filme.getPremios() != null ? filme.getPremios().stream()
                                .map(PremioMapper::toResponseDTO)
                                .toList() : null))
                .toList();
        return Response.ok(list).build();
    }

    // ==================== GET (buscar por ID) ====================
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Filme filme = service.findById(id);
        if (filme == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Filme não encontrado com ID: " + id)
                    .build();
        }

        FilmeResponseDTO response = new FilmeResponseDTO(
                filme.getId(),
                filme.getNome(),
                filme.getDuracao(),
                filme.getDuracaoMinutos(),
                filme.getSinopse(),
                filme.getIdiomaOriginal(),
                filme.getAnoLancamento(),
                filme.getImagemPoster(),
                filme.getTrailerUrl(),
                filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME() : null,
                filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                filme.getPremios() != null ? filme.getPremios().stream()
                        .map(PremioMapper::toResponseDTO)
                        .toList() : null);

        return Response.ok(response).build();
    }

    // ==================== GET (buscar por nome) ====================
    @SuppressWarnings("null")
    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<FilmeResponseDTO> list = service.findByNome(nome).stream()
                .map(filme -> new FilmeResponseDTO(
                        filme.getId(),
                        filme.getNome(),
                        filme.getDuracao(),
                        filme.getDuracaoMinutos(),
                        filme.getSinopse(),
                        filme.getIdiomaOriginal(),
                        filme.getAnoLancamento(),
                        filme.getImagemPoster(),
                        filme.getTrailerUrl(),
                        filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME()
                                : null,
                        filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                        filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                        filme.getPremios() != null ? filme.getPremios().stream()
                                .map(PremioMapper::toResponseDTO)
                                .toList() : null))
                .toList();

        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Nenhum filme encontrado com nome: " + nome)
                    .build();
        }
        return Response.ok(list).build();
    }

    // ==================== GET (buscar por gênero) ====================
    @SuppressWarnings("null")
    @GET
    @Path("/genero/{genero}")
    public Response buscarPorGenero(@PathParam("genero") String genero) {
        List<FilmeResponseDTO> list = service.findByGenero(genero).stream()
                .map(filme -> new FilmeResponseDTO(
                        filme.getId(),
                        filme.getNome(),
                        filme.getDuracao(),
                        filme.getDuracaoMinutos(),
                        filme.getSinopse(),
                        filme.getIdiomaOriginal(),
                        filme.getAnoLancamento(),
                        filme.getImagemPoster(),
                        filme.getTrailerUrl(),
                        filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME()
                                : null,
                        filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                        filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                        filme.getPremios() != null ? filme.getPremios().stream()
                                .map(PremioMapper::toResponseDTO)
                                .toList() : null))
                .toList();

        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Nenhum filme encontrado para o gênero: " + genero)
                    .build();
        }
        return Response.ok(list).build();
    }

    // ==================== GET (buscar por ator) ====================
    @SuppressWarnings("null")
    @GET
    @Path("/ator/{ator}")
    public Response buscarPorAtor(@PathParam("ator") String ator) {
        List<FilmeResponseDTO> list = service.findByAtor(ator).stream()
                .map(filme -> new FilmeResponseDTO(
                        filme.getId(),
                        filme.getNome(),
                        filme.getDuracao(),
                        filme.getDuracaoMinutos(),
                        filme.getSinopse(),
                        filme.getIdiomaOriginal(),
                        filme.getAnoLancamento(),
                        filme.getImagemPoster(),
                        filme.getTrailerUrl(),
                        filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME()
                                : null,
                        filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                        filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                        filme.getPremios() != null ? filme.getPremios().stream()
                                .map(PremioMapper::toResponseDTO)
                                .toList() : null))
                .toList();

        if (list.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Nenhum filme encontrado para o ator: " + ator)
                    .build();
        }
        return Response.ok(list).build();
    }

    // ==================== GET (buscar por duração) ====================
    @SuppressWarnings("null")
    @GET
    @Path("/duracao")
    public Response buscarPorDuracao(
            @QueryParam("min") @DefaultValue("0") Integer minMinutos,
            @QueryParam("max") @DefaultValue("1000") Integer maxMinutos) {

        if (minMinutos < 0 || maxMinutos < 0) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Min e Max devem ser valores positivos")
                    .build();
        }

        if (minMinutos > maxMinutos) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Min não pode ser maior que Max")
                    .build();
        }

        List<FilmeResponseDTO> list = service.findByDuracaoBetween(minMinutos, maxMinutos).stream()
                .map(filme -> new FilmeResponseDTO(
                        filme.getId(),
                        filme.getNome(),
                        filme.getDuracao(),
                        filme.getDuracaoMinutos(),
                        filme.getSinopse(),
                        filme.getIdiomaOriginal(),
                        filme.getAnoLancamento(),
                        filme.getImagemPoster(),
                        filme.getTrailerUrl(),
                        filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME()
                                : null,
                        filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                        filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                        filme.getPremios() != null ? filme.getPremios().stream()
                                .map(PremioMapper::toResponseDTO)
                                .toList() : null))
                .toList();

        return Response.ok(list).build();
    }

    // ==================== POST (criar) ====================
    @POST
    public Response criar(@Valid FilmeRequestDTO dto) {
        try {
            int anoAtual = java.time.Year.now().getValue();
            if (dto.anoLancamento() > anoAtual) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Ano de lançamento não pode ser futuro. Ano atual: " + anoAtual)
                        .build();
            }

            Filme filme = FilmeMapper.toEntity(dto);

            if (dto.classificacaoIndicativaId() != null) {
                ClassificacaoIndicativa classificacao = ClassificacaoIndicativa
                        .valueOf(dto.classificacaoIndicativaId());
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

            if (dto.premiosIds() != null && !dto.premiosIds().isEmpty()) {
                filme.setPremios(dto.premiosIds().stream()
                        .map(id -> premioRepository.findById(id))
                        .filter(p -> p != null)
                        .toList());
            }

            service.create(filme);

            FilmeResponseDTO response = new FilmeResponseDTO(
                    filme.getId(),
                    filme.getNome(),
                    filme.getDuracao(),
                    filme.getDuracaoMinutos(),
                    filme.getSinopse(),
                    filme.getIdiomaOriginal(),
                    filme.getAnoLancamento(),
                    filme.getImagemPoster(),
                    filme.getTrailerUrl(),
                    filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME() : null,
                    filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                    filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                    filme.getPremios() != null ? filme.getPremios().stream()
                            .map(PremioMapper::toResponseDTO)
                            .toList() : null);

            return Response.status(Status.CREATED)
                    .entity(response)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar filme: " + e.getMessage())
                    .build();
        }
    }

    // ==================== PUT (atualizar) ====================
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

            int anoAtual = java.time.Year.now().getValue();
            if (dto.anoLancamento() > anoAtual) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Ano de lançamento não pode ser futuro. Ano atual: " + anoAtual)
                        .build();
            }

            Filme filme = FilmeMapper.toEntity(dto);
            filme.setId(id);

            if (dto.classificacaoIndicativaId() != null) {
                ClassificacaoIndicativa classificacao = ClassificacaoIndicativa
                        .valueOf(dto.classificacaoIndicativaId());
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

            if (dto.premiosIds() != null) {
                filme.setPremios(dto.premiosIds().stream()
                        .map(premioId -> premioRepository.findById(premioId))
                        .filter(p -> p != null)
                        .toList());
            }

            service.update(id, filme);

            // Buscar o filme atualizado para retornar
            Filme filmeAtualizado = service.findById(id);

            FilmeResponseDTO response = new FilmeResponseDTO(
                    filmeAtualizado.getId(),
                    filmeAtualizado.getNome(),
                    filmeAtualizado.getDuracao(),
                    filmeAtualizado.getDuracaoMinutos(),
                    filmeAtualizado.getSinopse(),
                    filmeAtualizado.getIdiomaOriginal(),
                    filmeAtualizado.getAnoLancamento(),
                    filmeAtualizado.getImagemPoster(),
                    filmeAtualizado.getTrailerUrl(),
                    filmeAtualizado.getClassificacaoIndicativa() != null
                            ? filmeAtualizado.getClassificacaoIndicativa().getNOME()
                            : null,
                    filmeAtualizado.getGeneros() != null
                            ? filmeAtualizado.getGeneros().stream().map(g -> g.getNome()).toList()
                            : null,
                    filmeAtualizado.getAtores() != null
                            ? filmeAtualizado.getAtores().stream().map(a -> a.getNome()).toList()
                            : null,
                    filmeAtualizado.getPremios() != null ? filmeAtualizado.getPremios().stream()
                            .map(PremioMapper::toResponseDTO)
                            .toList() : null);

            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar filme: " + e.getMessage())
                    .build();
        }
    }

    // ==================== DELETE ====================
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