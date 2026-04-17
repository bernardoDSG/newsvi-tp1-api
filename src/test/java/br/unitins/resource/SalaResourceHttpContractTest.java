package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Sala;
import br.unitins.service.SalaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class SalaResourceHttpContractTest {

    private static final String BASE_URL = "/salas";

    @InjectMock
    SalaService salaService;

    @BeforeEach
    void setUp() {
        reset(salaService);
    }

    private Sala mockSala(Long id, Integer numero) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setNumero(numero);
        sala.setCapacidade(150);
        return sala;
    }

    @Test
    void deveListarSalasComStatus200() {
        when(salaService.findAll()).thenReturn(List.of(
            mockSala(1L, 1),
            mockSala(2L, 2)
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
            .body("[0].numero", equalTo(1));
    }

    @Test
    void deveBuscarSalaPorIdComStatus200() {
        when(salaService.findById(1L)).thenReturn(mockSala(1L, 1));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("numero", equalTo(1));
    }

    @Test
    void deveBuscarSalaPorNumeroComStatus200() {
        when(salaService.findByNumero(1)).thenReturn(mockSala(1L, 1));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/numero/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("numero", equalTo(1));
    }

    @Test
    void deveRetornar404QuandoBuscarSalaPorIdInexistente() {
        when(salaService.findById(999L)).thenThrow(new NotFoundException("Sala não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarSalaComStatus201() {
        Sala salaCriada = mockSala(10L, 3);
        when(salaService.create(any(Sala.class))).thenReturn(salaCriada);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"numero\":3,\"capacidade\":200,\"poltronasIds\":[1,2,3]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("numero", equalTo(3));
        
        verify(salaService, times(1)).create(any(Sala.class));
    }

    @Test
    void deveAtualizarSalaComStatus200() {
        doNothing().when(salaService).update(eq(10L), any(Sala.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"numero\":3,\"capacidade\":250,\"poltronasIds\":[1,2,3,4]}")
        .when()
            .put(BASE_URL + "/10")
        .then()
            .statusCode(200);
        
        verify(salaService, times(1)).update(eq(10L), any(Sala.class));
    }

    @Test
    void deveRemoverSalaComStatus204() {
        doNothing().when(salaService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(salaService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarSalaInexistente() {
        doThrow(new NotFoundException("Sala não encontrada"))
            .when(salaService).update(eq(999L), any(Sala.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"numero\":3,\"capacidade\":250,\"poltronasIds\":[1,2,3,4]}")
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
            .body("{\"numero\":null,\"capacidade\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(salaService, never()).create(any(Sala.class));
    }
}