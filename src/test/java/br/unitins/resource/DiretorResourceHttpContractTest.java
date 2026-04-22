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

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Diretor;
import br.unitins.service.DiretorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class DiretorResourceHttpContractTest {

    private static final String BASE_URL = "/diretores";

    @InjectMock
    DiretorService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @Test
    void deveListarDiretoresComStatus200() {
        when(service.findAll()).thenReturn(List.of(diretor(1L, "Shawn Levy"), diretor(2L, "Christopher Nolan")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].nome", equalTo("Shawn Levy"));
    }

    @Test
    void deveBuscarDiretorPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(diretor(1L, "Shawn Levy"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("nome", equalTo("Shawn Levy"));
    }

    @Test
    void deveBuscarDiretorPorNomeComStatus200() {
        when(service.findByNome("Shawn")).thenReturn(List.of(diretor(1L, "Shawn Levy")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Shawn")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarDiretorPorNacionalidadeComStatus200() {
        when(service.findByNacionalidade("Canadense")).thenReturn(List.of(diretor(1L, "Shawn Levy")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/nacionalidade/Canadense")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarDiretorComStatus201() {
        when(service.create(any(Diretor.class))).thenAnswer(invocation -> {
            Diretor diretor = invocation.getArgument(0);
            diretor.setId(10L);
            return diretor;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Shawn Levy","dataNascimento":"1968-07-23","nacionalidade":"Canadense","urlFoto":"foto"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("nome", equalTo("Shawn Levy"));
    }

    @Test
    void deveAtualizarDiretorComStatus200() {
        doNothing().when(service).update(any(Long.class), any(Diretor.class));
        when(service.findById(1L)).thenReturn(diretor(1L, "Shawn Levy"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Shawn Levy","dataNascimento":"1968-07-23","nacionalidade":"Canadense","urlFoto":"foto"}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverDiretorComStatus204() {
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
                {"nome":"","dataNascimento":"2099-01-01","nacionalidade":"Canadense","urlFoto":"foto"}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("title", equalTo("Erro de validação"))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Diretor.class));
    }

    private Diretor diretor(Long id, String nome) {
        Diretor diretor = new Diretor();
        diretor.setId(id);
        diretor.setNome(nome);
        diretor.setDataNascimento(LocalDate.of(1968, 7, 23));
        diretor.setNacionalidade("Canadense");
        diretor.setUrlFoto("foto");
        return diretor;
    }
}
