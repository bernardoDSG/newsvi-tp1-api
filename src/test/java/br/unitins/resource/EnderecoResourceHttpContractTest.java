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

import br.unitins.model.Endereco;
import br.unitins.service.EnderecoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class EnderecoResourceHttpContractTest {

    private static final String BASE_URL = "/enderecos";

    @InjectMock
    EnderecoService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @Test
    void deveListarEnderecosComStatus200() {
        when(service.findAll()).thenReturn(List.of(endereco(1L, "77000000"), endereco(2L, "77000001")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarEnderecoPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(endereco(1L, "77000000"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("cep", equalTo("77000000"));
    }

    @Test
    void deveBuscarEnderecoPorCepComStatus200() {
        when(service.findByCep("77000000")).thenReturn(endereco(1L, "77000000"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/cep/77000000")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveCriarEnderecoComStatus201() {
        when(service.create(any(Endereco.class))).thenAnswer(invocation -> {
            Endereco endereco = invocation.getArgument(0);
            endereco.setId(10L);
            return endereco;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"logradouro":"Rua A","numero":"10","complemento":"Casa","bairro":"Centro","cidade":"Palmas","estado":"TO","cep":"77000000"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("cidade", equalTo("Palmas"));
    }

    @Test
    void deveAtualizarEnderecoComStatus200() {
        doNothing().when(service).update(any(Long.class), any(Endereco.class));
        when(service.findById(1L)).thenReturn(endereco(1L, "77000000"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"logradouro":"Rua A","numero":"10","complemento":"Casa","bairro":"Centro","cidade":"Palmas","estado":"TO","cep":"77000000"}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverEnderecoComStatus204() {
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
                {"logradouro":"","numero":"","complemento":"Casa","bairro":"","cidade":"","estado":"T","cep":""}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("title", equalTo("Erro de validação"))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Endereco.class));
    }

    private Endereco endereco(Long id, String cep) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Rua A");
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Palmas");
        endereco.setEstado("TO");
        endereco.setCep(cep);
        return endereco;
    }
}
