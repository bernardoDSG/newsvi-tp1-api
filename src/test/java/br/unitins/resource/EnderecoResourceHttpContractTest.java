package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Endereco;
import br.unitins.service.EnderecoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class EnderecoResourceHttpContractTest {

    private static final String BASE_URL = "/enderecos";

    @InjectMock
    EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        reset(enderecoService);
    }

    private Endereco mockEndereco(Long id) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Avenida Rebouças");
        endereco.setNumero("1234");
        endereco.setComplemento("Piso 3");
        endereco.setBairro("Pinheiros");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("05402-000");
        return endereco;
    }

    @Test
    void deveListarEnderecosComStatus200() {
        when(enderecoService.findAll()).thenReturn(List.of(
            mockEndereco(1L),
            mockEndereco(2L)
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
            .body("[0].cidade", equalTo("São Paulo"));
    }

    @Test
    void deveBuscarEnderecoPorIdComStatus200() {
        when(enderecoService.findById(1L)).thenReturn(mockEndereco(1L));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("cidade", equalTo("São Paulo"));
    }

    @Test
    void deveBuscarEnderecoPorCepComStatus200() {
        when(enderecoService.findByCep("05402-000")).thenReturn(mockEndereco(1L));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/cep/05402-000")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("cep", equalTo("05402-000"));
    }

    @Test
    void deveRetornar404QuandoBuscarEnderecoPorIdInexistente() {
        when(enderecoService.findById(999L)).thenThrow(new NotFoundException("Endereço não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar404QuandoBuscarEnderecoPorCepInexistente() {
        when(enderecoService.findByCep("00000-000")).thenThrow(new NotFoundException("Endereço não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/cep/00000-000")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarEnderecoComStatus201() {
        Endereco enderecoCriado = mockEndereco(10L);
        when(enderecoService.create(any(Endereco.class))).thenReturn(enderecoCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"logradouro\":\"Avenida Rebouças\",\"numero\":\"1234\",\"complemento\":\"Piso 3\",\"bairro\":\"Pinheiros\",\"cidade\":\"São Paulo\",\"estado\":\"SP\",\"cep\":\"05402-000\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("cidade", equalTo("São Paulo"));
        
        verify(enderecoService, times(1)).create(any(Endereco.class));
    }

    @Test
    void deveAtualizarEnderecoComStatus200() {
        doNothing().when(enderecoService).update(eq(1L), any(Endereco.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"logradouro\":\"Avenida Rebouças\",\"numero\":\"1500\",\"complemento\":\"Piso 4\",\"bairro\":\"Pinheiros\",\"cidade\":\"São Paulo\",\"estado\":\"SP\",\"cep\":\"05402-010\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(enderecoService, times(1)).update(eq(1L), any(Endereco.class));
    }

    @Test
    void deveRemoverEnderecoComStatus204() {
        doNothing().when(enderecoService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(enderecoService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarEnderecoInexistente() {
        doThrow(new NotFoundException("Endereço não encontrado"))
            .when(enderecoService).update(eq(999L), any(Endereco.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"logradouro\":\"Rua Teste\",\"numero\":\"123\",\"complemento\":\"\",\"bairro\":\"Centro\",\"cidade\":\"São Paulo\",\"estado\":\"SP\",\"cep\":\"01000-000\"}")
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
            .body("{\"logradouro\":\"\",\"numero\":\"\",\"bairro\":\"\",\"cidade\":\"\",\"estado\":\"\",\"cep\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(enderecoService, never()).create(any(Endereco.class));
    }
}