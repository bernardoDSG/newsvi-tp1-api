package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Genero;
import br.unitins.service.GeneroService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class GeneroResourceHttpContractTest {

    private static final String BASE_URL = "/generos";

    @InjectMock
    GeneroService generoService;

    @BeforeEach
    void setUp() {
        reset(generoService);
    }

    private Genero mockGenero(Long id, String nome) {
        Genero genero = new Genero();
        genero.setId(id);
        genero.setNome(nome);
        return genero;
    }

    @Test
    void deveListarGenerosComStatus200() {
        when(generoService.findAll()).thenReturn(List.of(
            mockGenero(1L, "Ação"),
            mockGenero(2L, "Comédia")
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
            .body("[0].nome", equalTo("Ação"));
    }

    @Test
    void deveBuscarGeneroPorIdComStatus200() {
        when(generoService.findById(1L)).thenReturn(mockGenero(1L, "Ação"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Ação"));
    }

    @Test
    void deveRetornar404QuandoBuscarGeneroPorIdInexistente() {
        when(generoService.findById(999L)).thenThrow(new NotFoundException("Gênero não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarGeneroComStatus201() {
        Genero generoCriado = mockGenero(10L, "Aventura");
        when(generoService.create(any(Genero.class))).thenReturn(generoCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Aventura\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Aventura"));
        
        verify(generoService, times(1)).create(any(Genero.class));
    }

    @Test
    void deveAtualizarGeneroComStatus200() {
        doNothing().when(generoService).update(eq(1L), any(Genero.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Ação e Aventura\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(generoService, times(1)).update(eq(1L), any(Genero.class));
    }

    @Test
    void deveRemoverGeneroComStatus204() {
        doNothing().when(generoService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(generoService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarGeneroInexistente() {
        doThrow(new NotFoundException("Gênero não encontrado"))
            .when(generoService).update(eq(999L), any(Genero.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Aventura\"}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar404AoRemoverGeneroInexistente() {
        doThrow(new NotFoundException("Gênero não encontrado"))
            .when(generoService).delete(999L);

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
            .body("{\"nome\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(generoService, never()).create(any(Genero.class));
    }
}