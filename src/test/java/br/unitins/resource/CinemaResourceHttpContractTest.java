package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Cinema;
import br.unitins.model.Endereco;
import br.unitins.service.CinemaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class CinemaResourceHttpContractTest {

    private static final String BASE_URL = "/cinemas";

    @InjectMock
    CinemaService cinemaService;

    @BeforeEach
    void setUp() {
        reset(cinemaService);
    }

    private Cinema mockCinema(Long id, String nome) {
        Cinema cinema = new Cinema();
        cinema.setId(id);
        cinema.setNome(nome);
        cinema.setCnpj("12.345.678/0001-90");
        cinema.setTelefone("(11) 3456-7890");
        
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        cinema.setEndereco(endereco);
        
        return cinema;
    }

    @Test
    void deveListarCinemasComStatus200() {
        when(cinemaService.findAll()).thenReturn(List.of(
            mockCinema(1L, "Cinemark Shopping Eldorado"),
            mockCinema(2L, "Cinépolis JK Iguatemi")
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
            .body("[0].nome", equalTo("Cinemark Shopping Eldorado"));
    }

    @Test
    void deveBuscarCinemaPorIdComStatus200() {
        when(cinemaService.findById(1L)).thenReturn(mockCinema(1L, "Cinemark Shopping Eldorado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("nome", equalTo("Cinemark Shopping Eldorado"));
    }

    @Test
    void deveRetornar404QuandoBuscarCinemaPorIdInexistente() {
        when(cinemaService.findById(999L)).thenThrow(new NotFoundException("Cinema não encontrado"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarCinemaComStatus201() {
        Cinema cinemaCriado = mockCinema(10L, "Cinemark Shopping Eldorado");
        when(cinemaService.create(any(Cinema.class))).thenReturn(cinemaCriado);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Cinemark Shopping Eldorado\",\"cnpj\":\"12.345.678/0001-90\",\"telefone\":\"(11)3456-7890\",\"enderecoId\":1,\"salasIds\":[]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("nome", equalTo("Cinemark Shopping Eldorado"));
        
        verify(cinemaService, times(1)).create(any(Cinema.class));
    }

    @Test
    void deveAtualizarCinemaComStatus200() {
        doNothing().when(cinemaService).update(eq(1L), any(Cinema.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Cinemark Shopping Eldorado Premium\",\"cnpj\":\"12.345.678/0001-90\",\"telefone\":\"(11)3456-7890\",\"enderecoId\":1,\"salasIds\":[1,2]}")
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200);
        
        verify(cinemaService, times(1)).update(eq(1L), any(Cinema.class));
    }

    @Test
    void deveRemoverCinemaComStatus204() {
        doNothing().when(cinemaService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(cinemaService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarCinemaInexistente() {
        doThrow(new NotFoundException("Cinema não encontrado"))
            .when(cinemaService).update(eq(999L), any(Cinema.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"nome\":\"Cinema Teste\",\"cnpj\":\"12.345.678/0001-90\",\"telefone\":\"(11)3456-7890\",\"enderecoId\":1,\"salasIds\":[]}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar404AoRemoverCinemaInexistente() {
        doThrow(new NotFoundException("Cinema não encontrado"))
            .when(cinemaService).delete(999L);

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
            .body("{\"nome\":\"\",\"cnpj\":\"\",\"telefone\":\"\",\"enderecoId\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(cinemaService, never()).create(any(Cinema.class));
    }
}