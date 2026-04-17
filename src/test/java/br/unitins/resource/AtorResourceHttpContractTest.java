package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Ator;
import br.unitins.service.AtorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class AtorResourceHttpContractTest {

    private static final String BASE_URL = "/atores";

    @InjectMock
    AtorService atorService;

    @BeforeEach
    void setUp() {
        reset(atorService);
    }

    private Ator mockAtor(Long id, String nome) {
        Ator ator = new Ator();
        ator.setId(id);
        ator.setNome(nome);
        ator.setDataNascimento(LocalDate.of(1976, 10, 23));
        ator.setNacionalidade("Canadense");
        ator.setUrlFoto("https://image.tmdb.org/p/w500/ryanreynolds.jpg");
        return ator;
    }

    @Test
    void deveListarAtoresComStatus200() {
        when(atorService.findAll()).thenReturn(List.of(
            mockAtor(1L, "Ryan Reynolds"),
            mockAtor(2L, "Hugh Jackman")
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
            .body("[0].nome", equalTo("Ryan Reynolds"));
    }

    @Test
    void deveBuscarAtorPorIdComStatus200() {
        when(atorService.findById(1L)).thenReturn(mockAtor(1L, "Ryan Reynolds"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Ryan Reynolds"));
    }

    @Test
    void deveRetornar404QuandoBuscarAtorPorIdInexistente() {
        when(atorService.findById(999L)).thenThrow(new NotFoundException("Ator não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarAtorComStatus201() {
        Ator atorCriado = mockAtor(10L, "Ryan Reynolds");
        when(atorService.create(any(Ator.class))).thenReturn(atorCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Ryan Reynolds\",\"dataNascimento\":\"1976-10-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/ryanreynolds.jpg\",\"premiosIds\":[]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Ryan Reynolds"));
        
        verify(atorService, times(1)).create(any(Ator.class));
    }

    @Test
    void deveAtualizarAtorComStatus200() {
        doNothing().when(atorService).update(eq(1L), any(Ator.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Ryan Rodney Reynolds\",\"dataNascimento\":\"1976-10-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/ryanreynolds.jpg\",\"premiosIds\":[]}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(atorService, times(1)).update(eq(1L), any(Ator.class));
    }

    @Test
    void deveRemoverAtorComStatus204() {
        doNothing().when(atorService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(atorService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarAtorInexistente() {
        doThrow(new NotFoundException("Ator não encontrado"))
            .when(atorService).update(eq(999L), any(Ator.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Ryan Reynolds\",\"dataNascimento\":\"1976-10-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/ryanreynolds.jpg\",\"premiosIds\":[]}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar404AoRemoverAtorInexistente() {
        doThrow(new NotFoundException("Ator não encontrado"))
            .when(atorService).delete(999L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"dataNascimento\":\"\",\"nacionalidade\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(atorService, never()).create(any(Ator.class));
    }

    @Test
    void deveRetornar400QuandoJsonForMalformado() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Ryan Reynolds\",\"dataNascimento\":\"1976-10-23\"")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }
}