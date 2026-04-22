package br.unitins.resource;

import java.time.Year;
import java.util.List;

import br.unitins.dto.FilmeRequestDTO;
import br.unitins.dto.FilmeResponseDTO;
import br.unitins.exception.ValidationException;
import br.unitins.mapper.FilmeMapper;
import br.unitins.model.Ator;
import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Diretor;
import br.unitins.model.Filme;
import br.unitins.model.Genero;
import br.unitins.model.Premio;
import br.unitins.repository.AtorRepository;
import br.unitins.repository.DiretorRepository;
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

    @Inject
    DiretorRepository diretorRepository;

    @GET
    public Response buscarTodos() {
        List<FilmeResponseDTO> list = service.findAll().stream().map(FilmeMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(FilmeMapper.toResponseDTO(service.findById(id))).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<FilmeResponseDTO> list = service.findByNome(nome).stream().map(FilmeMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/genero/{genero}")
    public Response buscarPorGenero(@PathParam("genero") String genero) {
        List<FilmeResponseDTO> list = service.findByGenero(genero).stream().map(FilmeMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/ator/{ator}")
    public Response buscarPorAtor(@PathParam("ator") String ator) {
        List<FilmeResponseDTO> list = service.findByAtor(ator).stream().map(FilmeMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/diretor/{diretorId}")
    public Response buscarPorDiretor(@PathParam("diretorId") Long diretorId) {
        List<FilmeResponseDTO> list = service.findByDiretor(diretorId).stream().map(FilmeMapper::toResponseDTO).toList();
        return Response.ok(list).build();
    }

    @GET
    @Path("/duracao")
    public Response buscarPorDuracao(
        @QueryParam("min") @DefaultValue("0") Integer minMinutos,
        @QueryParam("max") @DefaultValue("1000") Integer maxMinutos) {

        if (minMinutos < 0 || maxMinutos < 0) {
            throw new IllegalArgumentException("Min e Max devem ser valores positivos");
        }
        if (minMinutos > maxMinutos) {
            throw new IllegalArgumentException("Min não pode ser maior que Max");
        }

        List<FilmeResponseDTO> list = service.findByDuracaoBetween(minMinutos, maxMinutos).stream()
            .map(FilmeMapper::toResponseDTO)
            .toList();
        return Response.ok(list).build();
    }

    @POST
    public Response criar(@Valid FilmeRequestDTO dto) {
        validateAnoLancamento(dto.anoLancamento());

        Filme filme = FilmeMapper.toEntity(dto);
        filme.setClassificacaoIndicativa(loadClassificacao(dto.classificacaoIndicativaId()));
        if (dto.diretorId() != null) {
            filme.setDiretor(loadDiretor(dto.diretorId()));
        }
        filme.setGeneros(loadGeneros(dto.generosIds()));
        filme.setAtores(loadAtores(dto.atoresIds()));
        filme.setPremios(loadPremios(dto.premiosIds()));

        Filme criado = service.create(filme);
        return Response.status(Status.CREATED).entity(FilmeMapper.toResponseDTO(criado)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid FilmeRequestDTO dto) {
        validateAnoLancamento(dto.anoLancamento());

        Filme filme = FilmeMapper.toEntity(dto);
        filme.setId(id);
        filme.setClassificacaoIndicativa(loadClassificacao(dto.classificacaoIndicativaId()));
        if (dto.diretorId() != null) {
            filme.setDiretor(loadDiretor(dto.diretorId()));
        }
        if (dto.generosIds() != null) {
            filme.setGeneros(loadGeneros(dto.generosIds()));
        }
        if (dto.atoresIds() != null) {
            filme.setAtores(loadAtores(dto.atoresIds()));
        }
        if (dto.premiosIds() != null) {
            filme.setPremios(loadPremios(dto.premiosIds()));
        }

        service.update(id, filme);
        return Response.ok(FilmeMapper.toResponseDTO(service.findById(id))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPorId(@PathParam("id") Long id) {
        service.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    private void validateAnoLancamento(Integer ano) {
        int anoAtual = Year.now().getValue();
        if (ano != null && ano > anoAtual) {
            throw new ValidationException("Ano de lançamento não pode ser futuro. Ano atual: " + anoAtual, "anoLancamento");
        }
    }

    private ClassificacaoIndicativa loadClassificacao(Long classificacaoId) {
        ClassificacaoIndicativa classificacao = ClassificacaoIndicativa.valueOf(classificacaoId);
        if (classificacao == null) {
            throw new ValidationException("Classificação indicativa inválida", "classificacaoIndicativaId");
        }
        return classificacao;
    }

    private Diretor loadDiretor(Long diretorId) {
        Diretor diretor = diretorRepository.findById(diretorId);
        if (diretor == null) {
            throw new ValidationException("Diretor informado não existe: " + diretorId, "diretorId");
        }
        return diretor;
    }

    private List<Genero> loadGeneros(List<Long> generosIds) {
        if (generosIds == null) {
            return null;
        }
        return generosIds.stream().map(this::loadGenero).toList();
    }

    private List<Ator> loadAtores(List<Long> atoresIds) {
        if (atoresIds == null) {
            return null;
        }
        return atoresIds.stream().map(this::loadAtor).toList();
    }

    private List<Premio> loadPremios(List<Long> premiosIds) {
        if (premiosIds == null) {
            return null;
        }
        return premiosIds.stream().map(this::loadPremio).toList();
    }

    private Genero loadGenero(Long generoId) {
        Genero genero = generoRepository.findById(generoId);
        if (genero == null) {
            throw new ValidationException("Gênero informado não existe: " + generoId, "generosIds");
        }
        return genero;
    }

    private Ator loadAtor(Long atorId) {
        Ator ator = atorRepository.findById(atorId);
        if (ator == null) {
            throw new ValidationException("Ator informado não existe: " + atorId, "atoresIds");
        }
        return ator;
    }

    private Premio loadPremio(Long premioId) {
        Premio premio = premioRepository.findById(premioId);
        if (premio == null) {
            throw new ValidationException("Prêmio informado não existe: " + premioId, "premiosIds");
        }
        return premio;
    }
}
