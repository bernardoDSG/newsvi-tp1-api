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

import br.unitins.model.Genero;
import br.unitins.service.GeneroService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class GeneroResourceHttpContractTest {

    private static final String BASE_URL = "/generos";

    @InjectMock
    GeneroService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @Test
    void deveListarGenerosComStatus200() {
        when(service.findAll()).thenReturn(List.of(genero(1L, "Acao"), genero(2L, "Drama")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarGeneroPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(genero(1L, "Acao"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveBuscarGeneroPorNomeComStatus200() {
        when(service.findByNome("Acao")).thenReturn(List.of(genero(1L, "Acao")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Acao")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarGeneroComStatus201() {
        when(service.create(any(Genero.class))).thenAnswer(invocation -> {
            Genero genero = invocation.getArgument(0);
            genero.setId(10L);
            return genero;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Acao"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Acao"));
    }

    @Test
    void deveAtualizarGeneroComStatus200() {
        doNothing().when(service).update(any(Long.class), any(Genero.class));
        when(service.findById(1L)).thenReturn(genero(1L, "Acao"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Acao"}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverGeneroComStatus204() {
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
                {"nome":""}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Genero.class));
    }

    private Genero genero(Long id, String nome) {
        Genero genero = new Genero();
        genero.setId(id);
        genero.setNome(nome);
        return genero;
    }
}
