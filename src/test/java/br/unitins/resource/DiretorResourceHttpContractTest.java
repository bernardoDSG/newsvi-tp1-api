package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Diretor;
import br.unitins.service.DiretorService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class DiretorResourceHttpContractTest {

    private static final String BASE_URL = "/diretores";

    @InjectMock
    DiretorService diretorService;

    @BeforeEach
    void setUp() {
        reset(diretorService);
    }

    private Diretor mockDiretor(Long id, String nome) {
        Diretor diretor = new Diretor();
        diretor.setId(id);
        diretor.setNome(nome);
        diretor.setDataNascimento(LocalDate.of(1968, 7, 23));
        diretor.setNacionalidade("Canadense");
        diretor.setUrlFoto("https://image.tmdb.org/p/w500/shawnlevy.jpg");
        return diretor;
    }

    @Test
    void deveListarDiretoresComStatus200() {
        when(diretorService.findAll()).thenReturn(List.of(
            mockDiretor(1L, "Shawn Levy"),
            mockDiretor(2L, "Christopher Nolan")
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
            .body("[0].nome", equalTo("Shawn Levy"));
    }

    @Test
    void deveBuscarDiretorPorIdComStatus200() {
        when(diretorService.findById(1L)).thenReturn(mockDiretor(1L, "Shawn Levy"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Shawn Levy"));
    }

    @Test
    void deveRetornar404QuandoBuscarDiretorPorIdInexistente() {
        when(diretorService.findById(999L)).thenThrow(new NotFoundException("Diretor não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarDiretorComStatus201() {
        Diretor diretorCriado = mockDiretor(10L, "Shawn Levy");
        when(diretorService.create(any(Diretor.class))).thenReturn(diretorCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Shawn Levy\",\"dataNascimento\":\"1968-07-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/shawnlevy.jpg\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Shawn Levy"));
        
        verify(diretorService, times(1)).create(any(Diretor.class));
    }

    @Test
    void deveAtualizarDiretorComStatus200() {
        doNothing().when(diretorService).update(eq(1L), any(Diretor.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Shawn Adam Levy\",\"dataNascimento\":\"1968-07-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/shawnlevy.jpg\"}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(diretorService, times(1)).update(eq(1L), any(Diretor.class));
    }

    @Test
    void deveRemoverDiretorComStatus204() {
        doNothing().when(diretorService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(diretorService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarDiretorInexistente() {
        doThrow(new NotFoundException("Diretor não encontrado"))
            .when(diretorService).update(eq(999L), any(Diretor.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Shawn Levy\",\"dataNascimento\":\"1968-07-23\",\"nacionalidade\":\"Canadense\",\"urlFoto\":\"https://image.tmdb.org/p/w500/shawnlevy.jpg\"}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar404AoRemoverDiretorInexistente() {
        doThrow(new NotFoundException("Diretor não encontrado"))
            .when(diretorService).delete(999L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoPayloadForInvalido() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"\",\"dataNascimento\":\"\",\"nacionalidade\":\"\"}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(diretorService, never()).create(any(Diretor.class));
    }

    @Test
    void deveRetornar400QuandoJsonForMalformado() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Shawn Levy\",\"dataNascimento\":\"1968-07-23\"")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(400);
    }
}