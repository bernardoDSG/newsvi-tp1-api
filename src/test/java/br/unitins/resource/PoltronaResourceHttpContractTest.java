package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Disponibilidade;
import br.unitins.model.Poltrona;
import br.unitins.service.PoltronaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class PoltronaResourceHttpContractTest {

    private static final String BASE_URL = "/poltronas";

    @InjectMock
    PoltronaService poltronaService;

    @BeforeEach
    void setUp() {
        reset(poltronaService);
    }

    private Poltrona mockPoltrona(Long id, String codigo) {
        Poltrona poltrona = new Poltrona();
        poltrona.setId(id);
        poltrona.setCodigo(codigo);
        poltrona.setLinha("A");
        poltrona.setColuna(1);
        poltrona.setDisponibilidade(Disponibilidade.DISPONIVEL);
        return poltrona;
    }

    @Test
    void deveListarPoltronasComStatus200() {
        when(poltronaService.findAll()).thenReturn(List.of(
            mockPoltrona(1L, "A1"),
            mockPoltrona(2L, "A2")
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
            .body("[0].codigo", equalTo("A1"));
    }

    @Test
    void deveBuscarPoltronaPorIdComStatus200() {
        when(poltronaService.findById(1L)).thenReturn(mockPoltrona(1L, "A1"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("codigo", equalTo("A1"));
    }

    @Test
    void deveRetornar404QuandoBuscarPoltronaPorIdInexistente() {
        when(poltronaService.findById(999L)).thenThrow(new NotFoundException("Poltrona não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarPoltronaComStatus201() {
        Poltrona poltronaCriada = mockPoltrona(10L, "B1");
        when(poltronaService.create(any(Poltrona.class))).thenReturn(poltronaCriada);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"codigo\":\"B1\",\"linha\":\"B\",\"coluna\":1,\"disponibilidadeId\":1}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("codigo", equalTo("B1"));
        
        verify(poltronaService, times(1)).create(any(Poltrona.class));
    }

    @Test
    void deveAtualizarPoltronaComStatus200() {
        doNothing().when(poltronaService).update(eq(10L), any(Poltrona.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"codigo\":\"B1\",\"linha\":\"B\",\"coluna\":1,\"disponibilidadeId\":2}")
        .when()
            .put(BASE_URL + "/10")
        .then()
            .statusCode(200);
        
        verify(poltronaService, times(1)).update(eq(10L), any(Poltrona.class));
    }

    @Test
    void deveRemoverPoltronaComStatus204() {
        doNothing().when(poltronaService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(poltronaService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarPoltronaInexistente() {
        doThrow(new NotFoundException("Poltrona não encontrada"))
            .when(poltronaService).update(eq(999L), any(Poltrona.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"codigo\":\"Z1\",\"linha\":\"Z\",\"coluna\":1,\"disponibilidadeId\":1}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"codigo\":\"\",\"disponibilidadeId\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(poltronaService, never()).create(any(Poltrona.class));
    }
}