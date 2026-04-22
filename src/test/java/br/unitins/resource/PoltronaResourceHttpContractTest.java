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

import br.unitins.model.Disponibilidade;
import br.unitins.model.Poltrona;
import br.unitins.service.PoltronaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class PoltronaResourceHttpContractTest {

    private static final String BASE_URL = "/poltronas";

    @InjectMock
    PoltronaService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @Test
    void deveListarPoltronasComStatus200() {
        when(service.findAll()).thenReturn(List.of(poltrona(1L, "A1"), poltrona(2L, "A2")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarPoltronaPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(poltrona(1L, "A1"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveBuscarPoltronaPorCodigoComStatus200() {
        when(service.findByCodigo("A1")).thenReturn(List.of(poltrona(1L, "A1")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/A1")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarPoltronaPorDisponibilidadeComStatus200() {
        when(service.findByDisponibilidade(1L)).thenReturn(List.of(poltrona(1L, "A1")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/disponibilidade/1")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarPoltronaComStatus201() {
        when(service.create(any(Poltrona.class))).thenAnswer(invocation -> {
            Poltrona poltrona = invocation.getArgument(0);
            poltrona.setId(10L);
            return poltrona;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"codigo":"A1","linha":"A","coluna":1,"disponibilidadeId":1}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("disponibilidade", equalTo("Disponível"));
    }

    @Test
    void deveAtualizarPoltronaComStatus200() {
        doNothing().when(service).update(any(Long.class), any(Poltrona.class));
        when(service.findById(1L)).thenReturn(poltrona(1L, "A1"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"codigo":"A1","linha":"A","coluna":1,"disponibilidadeId":1}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverPoltronaComStatus204() {
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
                {"codigo":"","linha":"AA","coluna":0,"disponibilidadeId":null}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Poltrona.class));
    }

    private Poltrona poltrona(Long id, String codigo) {
        Poltrona poltrona = new Poltrona();
        poltrona.setId(id);
        poltrona.setCodigo(codigo);
        poltrona.setLinha("A");
        poltrona.setColuna(1);
        poltrona.setDisponibilidade(Disponibilidade.DISPONIVEL);
        return poltrona;
    }
}
