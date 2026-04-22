package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class FilmeResourceHttpContractTest {

    private static final String BASE_URL = "/filmes";

    @InjectMock
    FilmeService service;

    @InjectMock
    GeneroRepository generoRepository;

    @InjectMock
    AtorRepository atorRepository;

    @InjectMock
    PremioRepository premioRepository;

    @InjectMock
    DiretorRepository diretorRepository;

    @BeforeEach
    void setUp() {
        reset(service, generoRepository, atorRepository, premioRepository, diretorRepository);
    }

    @Test
    void deveListarFilmesComStatus200() {
        when(service.findAll()).thenReturn(List.of(filme(1L, "Deadpool 3"), filme(2L, "Oppenheimer")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].nome", equalTo("Deadpool 3"));
    }

    @Test
    void deveBuscarFilmePorIdComStatus200() {
        when(service.findById(1L)).thenReturn(filme(1L, "Deadpool 3"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("classificacaoIndicativa", equalTo("18 anos"));
    }

    @Test
    void deveBuscarFilmePorNomeComStatus200() {
        when(service.findByNome("Deadpool")).thenReturn(List.of(filme(1L, "Deadpool 3")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Deadpool")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarFilmePorGeneroComStatus200() {
        when(service.findByGenero("Acao")).thenReturn(List.of(filme(1L, "Deadpool 3")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/genero/Acao")
        .then()
            .statusCode(200);
    }

    @Test
    void deveBuscarFilmePorAtorComStatus200() {
        when(service.findByAtor("Ryan Reynolds")).thenReturn(List.of(filme(1L, "Deadpool 3")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/ator/Ryan Reynolds")
        .then()
            .statusCode(200);
    }

    @Test
    void deveBuscarFilmePorDiretorComStatus200() {
        when(service.findByDiretor(3L)).thenReturn(List.of(filme(1L, "Deadpool 3")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/diretor/3")
        .then()
            .statusCode(200);
    }

    @Test
    void deveBuscarFilmePorDuracaoComStatus200() {
        when(service.findByDuracaoBetween(90, 150)).thenReturn(List.of(filme(1L, "Deadpool 3")));

        given()
            .accept(ContentType.JSON)
            .queryParam("min", 90)
            .queryParam("max", 150)
        .when()
            .get(BASE_URL + "/duracao")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarFilmeComStatus201() {
        mockRelacionamentosFilme();
        when(service.create(any(Filme.class))).thenAnswer(invocation -> {
            Filme filme = invocation.getArgument(0);
            filme.setId(10L);
            return filme;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(filmePayload(2024))
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("diretorNome", equalTo("Shawn Levy"))
            .body("premios[0].id", equalTo(6));
    }

    @Test
    void deveAtualizarFilmeComStatus200() {
        mockRelacionamentosFilme();
        doNothing().when(service).update(any(Long.class), any(Filme.class));
        when(service.findById(1L)).thenReturn(filme(1L, "Deadpool 3"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(filmePayload(2024))
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("generos[0]", equalTo("Acao"));
    }

    @Test
    void deveRemoverFilmeComStatus204() {
        doNothing().when(service).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"","duracao":"","sinopse":"curta","idiomaOriginal":"Ingles","anoLancamento":null,"imagemPoster":"poster","trailerUrl":"trailer","classificacaoIndicativaId":null,"diretorId":3,"generosIds":[4],"atoresIds":[5],"premiosIds":[6]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("title", equalTo("Erro de validação"))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Filme.class));
    }

    private void mockRelacionamentosFilme() {
        when(diretorRepository.findById(3L)).thenReturn(diretor(3L, "Shawn Levy"));
        when(generoRepository.findById(4L)).thenReturn(genero(4L, "Acao"));
        when(atorRepository.findById(5L)).thenReturn(ator(5L, "Ryan Reynolds"));
        when(premioRepository.findById(6L)).thenReturn(premio(6L, "Oscar"));
    }

    private String filmePayload(int ano) {
        return """
            {"nome":"Deadpool 3","duracao":"2h 10m","sinopse":"Sinopse valida com mais de dez caracteres.","idiomaOriginal":"Ingles","anoLancamento":%d,"imagemPoster":"poster","trailerUrl":"trailer","classificacaoIndicativaId":6,"diretorId":3,"generosIds":[4],"atoresIds":[5],"premiosIds":[6]}
            """.formatted(ano);
    }

    private Filme filme(Long id, String nome) {
        Filme filme = new Filme();
        filme.setId(id);
        filme.setNome(nome);
        filme.setDuracao("2h 10m");
        filme.setDuracaoMinutos(130);
        filme.setSinopse("Sinopse valida com mais de dez caracteres.");
        filme.setIdiomaOriginal("Ingles");
        filme.setAnoLancamento(2024);
        filme.setImagemPoster("poster");
        filme.setTrailerUrl("trailer");
        filme.setClassificacaoIndicativa(ClassificacaoIndicativa.DEZOITO);
        filme.setDiretor(diretor(3L, "Shawn Levy"));
        filme.setGeneros(List.of(genero(4L, "Acao")));
        filme.setAtores(List.of(ator(5L, "Ryan Reynolds")));
        filme.setPremios(List.of(premio(6L, "Oscar")));
        return filme;
    }

    private Diretor diretor(Long id, String nome) {
        Diretor diretor = new Diretor();
        diretor.setId(id);
        diretor.setNome(nome);
        return diretor;
    }

    private Genero genero(Long id, String nome) {
        Genero genero = new Genero();
        genero.setId(id);
        genero.setNome(nome);
        return genero;
    }

    private Ator ator(Long id, String nome) {
        Ator ator = new Ator();
        ator.setId(id);
        ator.setNome(nome);
        return ator;
    }

    private Premio premio(Long id, String nome) {
        Premio premio = new Premio();
        premio.setId(id);
        premio.setNome(nome);
        premio.setAno(2024);
        premio.setCategoria("Melhor");
        return premio;
    }
}
