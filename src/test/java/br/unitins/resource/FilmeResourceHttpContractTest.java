package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.ClassificacaoIndicativa;
import br.unitins.model.Filme;
import br.unitins.service.FilmeService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class FilmeResourceHttpContractTest {

    private static final String BASE_URL = "/filmes";

    @InjectMock
    FilmeService filmeService;

    @BeforeEach
    void setUp() {
        reset(filmeService);
    }

    private Filme mockFilme(Long id, String nome) {
        Filme filme = new Filme();
        filme.setId(id);
        filme.setNome(nome);
        filme.setDuracao("2h 10m");
        filme.setDuracaoMinutos(130);
        filme.setSinopse("Sinopse do filme");
        filme.setIdiomaOriginal("Inglês");
        filme.setAnoLancamento(2024);
        filme.setImagemPoster("https://image.tmdb.org/poster.jpg");
        filme.setTrailerUrl("https://youtube.com/trailer");
        filme.setClassificacaoIndicativa(ClassificacaoIndicativa.DEZOITO);
        return filme;
    }

    @Test
    void deveListarFilmesComStatus200() {
        when(filmeService.findAll()).thenReturn(List.of(
            mockFilme(1L, "Deadpool 3"),
            mockFilme(2L, "Oppenheimer")
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Deadpool 3"));
    }

    @Test
    void deveBuscarFilmePorIdComStatus200() {
        when(filmeService.findById(1L)).thenReturn(mockFilme(1L, "Deadpool 3"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Deadpool 3"));
    }

    @Test
    void deveRetornar404QuandoBuscarFilmePorIdInexistente() {
        when(filmeService.findById(999L)).thenThrow(new NotFoundException("Filme não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarFilmeComStatus201() {
        Filme filmeCriado = mockFilme(10L, "Deadpool 3");
        when(filmeService.create(any(Filme.class))).thenReturn(filmeCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Deadpool 3\",\"duracao\":\"2h 10m\",\"sinopse\":\"Sinopse...\",\"idiomaOriginal\":\"Inglês\",\"anoLancamento\":2024,\"imagemPoster\":\"https://image.tmdb.org/poster.jpg\",\"trailerUrl\":\"https://youtube.com/trailer\",\"classificacaoIndicativaId\":6,\"diretorId\":1,\"generosIds\":[1,2],\"atoresIds\":[1,2],\"premiosIds\":[]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Deadpool 3"));
        
        verify(filmeService, times(1)).create(any(Filme.class));
    }

    @Test
    void deveAtualizarFilmeComStatus200() {
        doNothing().when(filmeService).update(eq(1L), any(Filme.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Deadpool 3 - Edição Especial\",\"duracao\":\"2h 20m\",\"sinopse\":\"Sinopse atualizada...\",\"idiomaOriginal\":\"Inglês\",\"anoLancamento\":2024,\"imagemPoster\":\"https://image.tmdb.org/poster.jpg\",\"trailerUrl\":\"https://youtube.com/trailer\",\"classificacaoIndicativaId\":6,\"diretorId\":1,\"generosIds\":[1,2],\"atoresIds\":[1],\"premiosIds\":[1]}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(filmeService, times(1)).update(eq(1L), any(Filme.class));
    }

    @Test
    void deveRemoverFilmeComStatus204() {
        doNothing().when(filmeService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(filmeService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarFilmeInexistente() {
        doThrow(new NotFoundException("Filme não encontrado"))
            .when(filmeService).update(eq(999L), any(Filme.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Filme Teste\",\"duracao\":\"1h 30m\",\"sinopse\":\"Sinopse...\",\"idiomaOriginal\":\"Português\",\"anoLancamento\":2024,\"imagemPoster\":\"https://image.tmdb.org/poster.jpg\",\"trailerUrl\":\"https://youtube.com/trailer\",\"classificacaoIndicativaId\":1,\"diretorId\":1,\"generosIds\":[1],\"atoresIds\":[1],\"premiosIds\":[]}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoAnoLancamentoFuturo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Deadpool 3\",\"duracao\":\"2h 10m\",\"sinopse\":\"Sinopse...\",\"idiomaOriginal\":\"Inglês\",\"anoLancamento\":2030,\"imagemPoster\":\"https://image.tmdb.org/poster.jpg\",\"trailerUrl\":\"https://youtube.com/trailer\",\"classificacaoIndicativaId\":6,\"diretorId\":1,\"generosIds\":[1,2],\"atoresIds\":[1,2],\"premiosIds\":[]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"duracao\":\"\",\"sinopse\":\"\",\"anoLancamento\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(filmeService, never()).create(any(Filme.class));
    }
}