package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Premio;
import br.unitins.service.PremioService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class PremioResourceHttpContractTest {

    private static final String BASE_URL = "/premios";

    @InjectMock
    PremioService premioService;

    @BeforeEach
    void setUp() {
        reset(premioService);
    }

    private Premio mockPremio(Long id, String nome) {
        Premio premio = new Premio();
        premio.setId(id);
        premio.setNome(nome);
        premio.setAno(2024);
        premio.setCategoria("Melhor Filme");
        return premio;
    }

    @Test
    void deveListarPremiosComStatus200() {
        when(premioService.findAll()).thenReturn(List.of(
            mockPremio(1L, "Oscar"),
            mockPremio(2L, "Globo de Ouro")
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
            .body("[0].nome", equalTo("Oscar"));
    }

    @Test
    void deveBuscarPremioPorIdComStatus200() {
        when(premioService.findById(1L)).thenReturn(mockPremio(1L, "Oscar"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Oscar"));
    }

    @Test
    void deveRetornar404QuandoBuscarPremioPorIdInexistente() {
        when(premioService.findById(999L)).thenThrow(new NotFoundException("Prêmio não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarPremioComStatus201() {
        Premio premioCriado = mockPremio(10L, "BAFTA");
        when(premioService.create(any(Premio.class))).thenReturn(premioCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"BAFTA\",\"ano\":2024,\"categoria\":\"Melhor Filme\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("BAFTA"));
        
        verify(premioService, times(1)).create(any(Premio.class));
    }

    @Test
    void deveAtualizarPremioComStatus200() {
        doNothing().when(premioService).update(eq(10L), any(Premio.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"BAFTA\",\"ano\":2024,\"categoria\":\"Melhor Diretor\"}")
        .when()
            .put(BASE_URL + "/10")
        .then()
            .statusCode(200);
        
        verify(premioService, times(1)).update(eq(10L), any(Premio.class));
    }

    @Test
    void deveRemoverPremioComStatus204() {
        doNothing().when(premioService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(premioService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarPremioInexistente() {
        doThrow(new NotFoundException("Prêmio não encontrado"))
            .when(premioService).update(eq(999L), any(Premio.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Oscar\",\"ano\":2024,\"categoria\":\"Melhor Filme\"}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoAnoFuturo() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Oscar\",\"ano\":2030,\"categoria\":\"Melhor Filme\"}")
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
            .body("{\"nome\":\"\",\"ano\":null,\"categoria\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(premioService, never()).create(any(Premio.class));
    }
}