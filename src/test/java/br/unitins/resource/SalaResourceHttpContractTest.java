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
import br.unitins.model.Sala;
import br.unitins.repository.PoltronaRepository;
import br.unitins.service.SalaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class SalaResourceHttpContractTest {

    private static final String BASE_URL = "/salas";

    @InjectMock
    SalaService service;

    @InjectMock
    PoltronaRepository poltronaRepository;

    @BeforeEach
    void setUp() {
        reset(service, poltronaRepository);
    }

    @Test
    void deveListarSalasComStatus200() {
        when(service.findAll()).thenReturn(List.of(sala(1L, 1), sala(2L, 2)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarSalaPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(sala(1L, 1));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveBuscarSalaPorNumeroComStatus200() {
        when(service.findByNumero(1)).thenReturn(sala(1L, 1));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/numero/1")
        .then()
            .statusCode(200)
            .body("numero", equalTo(1));
    }

    @Test
    void deveCriarSalaComStatus201() {
        when(poltronaRepository.findById(5L)).thenReturn(poltrona(5L, "A1"));
        when(service.create(any(Sala.class))).thenAnswer(invocation -> {
            Sala sala = invocation.getArgument(0);
            sala.setId(10L);
            return sala;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"numero":1,"capacidade":120,"poltronasIds":[5]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("poltronasCodigos[0]", equalTo("A1"));
    }

    @Test
    void deveAtualizarSalaComStatus200() {
        when(poltronaRepository.findById(5L)).thenReturn(poltrona(5L, "A1"));
        doNothing().when(service).update(any(Long.class), any(Sala.class));
        when(service.findById(1L)).thenReturn(sala(1L, 1));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"numero":1,"capacidade":120,"poltronasIds":[5]}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverSalaComStatus204() {
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
                {"numero":0,"capacidade":0,"poltronasIds":[]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Sala.class));
    }

    private Sala sala(Long id, Integer numero) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setNumero(numero);
        sala.setCapacidade(120);
        sala.setPoltronas(List.of(poltrona(5L, "A1")));
        return sala;
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
