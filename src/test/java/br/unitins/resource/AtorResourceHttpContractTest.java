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

import br.unitins.model.Ator;
import br.unitins.model.Premio;
import br.unitins.repository.PremioRepository;
import br.unitins.service.AtorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class AtorResourceHttpContractTest {

    private static final String BASE_URL = "/atores";

    @InjectMock
    AtorService service;

    @InjectMock
    PremioRepository premioRepository;

    @BeforeEach
    void setUp() {
        reset(service, premioRepository);
    }

    @Test
    void deveListarAtoresComStatus200() {
        when(service.findAll()).thenReturn(List.of(ator(1L, "Ryan Reynolds"), ator(2L, "Hugh Jackman")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Ryan Reynolds"));
    }

    @Test
    void deveBuscarAtorPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(ator(1L, "Ryan Reynolds"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Ryan Reynolds"));
    }

    @Test
    void deveBuscarAtorPorNomeComStatus200() {
        when(service.findByNome("Ryan")).thenReturn(List.of(ator(1L, "Ryan Reynolds")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Ryan")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(1))
            .body("[0].nome", equalTo("Ryan Reynolds"));
    }

    @Test
    void deveBuscarAtorPorPremioComStatus200() {
        when(service.findByPremio("Oscar")).thenReturn(List.of(ator(1L, "Ryan Reynolds")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/premio/Oscar")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(1));
    }

    @Test
    void deveCriarAtorComStatus201() {
        when(premioRepository.findById(7L)).thenReturn(premio(7L, "Oscar"));
        when(service.create(any(Ator.class))).thenAnswer(invocation -> {
            Ator ator = invocation.getArgument(0);
            ator.setId(10L);
            return ator;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Ryan Reynolds","dataNascimento":"1976-10-23","nacionalidade":"Canadense","urlFoto":"https://image.tmdb.org/p/w500/ryanreynolds.jpg","premiosIds":[7]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Ryan Reynolds"))
            .body("premios", hasSize(1))
            .body("premios[0].id", equalTo(7));
    }

    @Test
    void deveAtualizarAtorComStatus200() {
        when(premioRepository.findById(7L)).thenReturn(premio(7L, "Oscar"));
        doNothing().when(service).update(any(Long.class), any(Ator.class));
        when(service.findById(1L)).thenReturn(atorComPremio(1L, "Ryan Reynolds", premio(7L, "Oscar")));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Ryan Reynolds","dataNascimento":"1976-10-23","nacionalidade":"Canadense","urlFoto":"https://image.tmdb.org/p/w500/ryanreynolds.jpg","premiosIds":[7]}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("premios[0].id", equalTo(7));
    }

    @Test
    void deveRemoverAtorComStatus204() {
        doNothing().when(service).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
    }

    @Test
    void deveRetornar404QuandoBuscarAtorInexistente() {
        when(service.findById(999L)).thenThrow(new NotFoundException("Ator não encontrado com ID: 999"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("title", equalTo("Recurso não encontrado"))
            .body("status", equalTo(404));
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"","dataNascimento":"2099-01-01","nacionalidade":"Canadense","urlFoto":"foto","premiosIds":[]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Ator.class));
    }

    private Ator ator(Long id, String nome) {
        Ator ator = new Ator();
        ator.setId(id);
        ator.setNome(nome);
        ator.setDataNascimento(LocalDate.of(1976, 10, 23));
        ator.setNacionalidade("Canadense");
        ator.setUrlFoto("https://image.tmdb.org/p/w500/ryanreynolds.jpg");
        return ator;
    }

    private Ator atorComPremio(Long id, String nome, Premio premio) {
        Ator ator = ator(id, nome);
        ator.setPremios(List.of(premio));
        return ator;
    }

    private Premio premio(Long id, String nome) {
        Premio premio = new Premio();
        premio.setId(id);
        premio.setNome(nome);
        premio.setAno(2024);
        premio.setCategoria("Melhor ator");
        return premio;
    }
}
