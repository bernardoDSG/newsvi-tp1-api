package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.model.TipoSessao;
import br.unitins.service.SessaoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class SessaoResourceHttpContractTest {

    private static final String BASE_URL = "/sessoes";

    @InjectMock
    SessaoService sessaoService;

    @BeforeEach
    void setUp() {
        reset(sessaoService);
    }

    private Sessao mockSessao(Long id) {
        Sessao sessao = new Sessao();
        sessao.setId(id);
        sessao.setInicio(LocalDateTime.of(2024, 12, 20, 20, 0));
        sessao.setFim(LocalDateTime.of(2024, 12, 20, 22, 30));
        sessao.setCapacidadeTotal(150);
        sessao.setCapacidadeDisponivel(150);
        sessao.setStatus(StatusSessao.EM_BREVE);
        sessao.setTipo(TipoSessao.DUBLADA);
        return sessao;
    }

    @Test
    void deveListarSessoesComStatus200() {
        when(sessaoService.findAll()).thenReturn(List.of(
            mockSessao(1L),
            mockSessao(2L)
        ));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(2))
            .body("[0].id", equalTo(1));
    }

    @Test
    void deveBuscarSessaoPorIdComStatus200() {
        when(sessaoService.findById(1L)).thenReturn(mockSessao(1L));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1));
    }

    @Test
    void deveRetornar404QuandoBuscarSessaoPorIdInexistente() {
        when(sessaoService.findById(999L)).thenThrow(new NotFoundException("Sessão não encontrada"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveCriarSessaoComStatus201() {
        Sessao sessaoCriada = mockSessao(10L);
        when(sessaoService.create(any(Sessao.class))).thenReturn(sessaoCriada);

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"inicio\":\"2024-12-20T20:00:00\",\"fim\":\"2024-12-20T22:30:00\",\"capacidadeTotal\":150,\"capacidadeDisponivel\":150,\"statusId\":1,\"tipoSessaoId\":1,\"filmeId\":1,\"cinemaId\":1,\"salasIds\":[1]}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10));
        
        verify(sessaoService, times(1)).create(any(Sessao.class));
    }

    @Test
    void deveAtualizarSessaoComStatus200() {
        doNothing().when(sessaoService).update(eq(10L), any(Sessao.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"inicio\":\"2024-12-20T21:00:00\",\"fim\":\"2024-12-20T23:30:00\",\"capacidadeTotal\":150,\"capacidadeDisponivel\":100,\"statusId\":2,\"tipoSessaoId\":2,\"filmeId\":1,\"cinemaId\":1,\"salasIds\":[1,2]}")
        .when()
            .put(BASE_URL + "/10")
        .then()
            .statusCode(200);
        
        verify(sessaoService, times(1)).update(eq(10L), any(Sessao.class));
    }

    @Test
    void deveRemoverSessaoComStatus204() {
        doNothing().when(sessaoService).delete(1L);

        given()
            .accept(ContentType.JSON)
        .when()
            .delete(BASE_URL + "/1")
        .then()
            .statusCode(204);
        
        verify(sessaoService, times(1)).delete(1L);
    }

    @Test
    void deveRetornar404AoAtualizarSessaoInexistente() {
        doThrow(new NotFoundException("Sessão não encontrada"))
            .when(sessaoService).update(eq(999L), any(Sessao.class));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"inicio\":\"2024-12-20T21:00:00\",\"fim\":\"2024-12-20T23:30:00\",\"capacidadeTotal\":150,\"capacidadeDisponivel\":100,\"statusId\":2,\"tipoSessaoId\":2,\"filmeId\":1,\"cinemaId\":1,\"salasIds\":[1,2]}")
        .when()
            .put(BASE_URL + "/999")
        .then()
            .statusCode(404);
    }

    @Test
    void deveRetornar422QuandoHorarioInicioAposFim() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"inicio\":\"2024-12-20T22:30:00\",\"fim\":\"2024-12-20T20:00:00\",\"capacidadeTotal\":150,\"capacidadeDisponivel\":150,\"statusId\":1,\"tipoSessaoId\":1,\"filmeId\":1,\"cinemaId\":1,\"salasIds\":[1]}")
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
            .body("{\"inicio\":null,\"fim\":null,\"capacidadeTotal\":null,\"capacidadeDisponivel\":null}")
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("type", equalTo("http://localhost:8080/errors/validation-error"))
            .body("title", equalTo("Erro de validação"))
            .body("status", equalTo(422))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(sessaoService, never()).create(any(Sessao.class));
    }
}