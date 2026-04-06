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
    
    @Inject
    PremioRepository premioRepository;  // <-- Verifique se tem essa injeção

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
            
            // ⭐ A PARTE MAIS IMPORTANTE - ASSOCIAR OS PRÊMIOS ⭐
            if (dto.premiosIds() != null && !dto.premiosIds().isEmpty()) {
                filme.setPremios(dto.premiosIds().stream()
                    .map(id -> premioRepository.findById(id))
                    .filter(p -> p != null)
                    .toList());
            }
            
            service.create(filme);
            
            // ⭐ CONSTRUIR O RESPONSE COM OS PRÊMIOS ⭐
            FilmeResponseDTO responseDTO = new FilmeResponseDTO(
                filme.getId(),
                filme.getNome(),
                filme.getDuracao(),
                filme.getSinopse(),
                filme.getIdiomaOriginal(),
                filme.getAnoLancamento(),
                filme.getClassificacaoIndicativa() != null ? filme.getClassificacaoIndicativa().getNOME() : null,
                filme.getGeneros() != null ? filme.getGeneros().stream().map(g -> g.getNome()).toList() : null,
                filme.getAtores() != null ? filme.getAtores().stream().map(a -> a.getNome()).toList() : null,
                filme.getPremios() != null ? filme.getPremios().stream()
                    .map(PremioMapper::toResponseDTO)
                    .toList() : null
            );
            
            return Response.status(Status.CREATED)
                .entity(responseDTO)
                .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
        } catch (Exception e) {
            e.printStackTrace();  // <-- Adicione isso para ver o erro no console
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao criar filme: " + e.getMessage())
                .build();
        }
    }

    // GET, PUT, DELETE com a mesma lógica de construção do response
}