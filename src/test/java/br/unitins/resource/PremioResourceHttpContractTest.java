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

import br.unitins.model.Premio;
import br.unitins.service.PremioService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class PremioResourceHttpContractTest {

    private static final String BASE_URL = "/premios";

    @InjectMock
    PremioService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @Test
    void deveListarPremiosComStatus200() {
        when(service.findAll()).thenReturn(List.of(premio(1L, "Oscar"), premio(2L, "Globo de Ouro")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarPremioPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(premio(1L, "Oscar"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveBuscarPremioPorNomeComStatus200() {
        when(service.findByNome("Oscar")).thenReturn(List.of(premio(1L, "Oscar")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Oscar")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarPremioPorCategoriaComStatus200() {
        when(service.findByCategoria("Melhor Filme")).thenReturn(List.of(premio(1L, "Oscar")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/categoria/Melhor Filme")
        .then()
            .statusCode(200);
    }

    @Test
    void deveCriarPremioComStatus201() {
        when(service.create(any(Premio.class))).thenAnswer(invocation -> {
            Premio premio = invocation.getArgument(0);
            premio.setId(10L);
            return premio;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Oscar","ano":2024,"categoria":"Melhor Filme"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Oscar"));
    }

    @Test
    void deveAtualizarPremioComStatus200() {
        doNothing().when(service).update(any(Long.class), any(Premio.class));
        when(service.findById(1L)).thenReturn(premio(1L, "Oscar"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Oscar","ano":2024,"categoria":"Melhor Filme"}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverPremioComStatus204() {
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
                {"nome":"","ano":null,"categoria":"Melhor Filme"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Premio.class));
    }

    private Premio premio(Long id, String nome) {
        Premio premio = new Premio();
        premio.setId(id);
        premio.setNome(nome);
        premio.setAno(2024);
        premio.setCategoria("Melhor Filme");
        return premio;
    }
}
