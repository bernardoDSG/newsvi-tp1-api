package br.unitins.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.unitins.model.Cinema;
import br.unitins.model.Filme;
import br.unitins.model.Sala;
import br.unitins.model.Sessao;
import br.unitins.model.StatusSessao;
import br.unitins.model.TipoSessao;
import br.unitins.repository.CinemaRepository;
import br.unitins.repository.FilmeRepository;
import br.unitins.repository.SalaRepository;
import br.unitins.service.SessaoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class SessaoResourceHttpContractTest {

    private static final String BASE_URL = "/sessoes";

    @InjectMock
    SessaoService service;

    @InjectMock
    FilmeRepository filmeRepository;

    @InjectMock
    SalaRepository salaRepository;

    @InjectMock
    CinemaRepository cinemaRepository;

    @BeforeEach
    void setUp() {
        reset(service, filmeRepository, salaRepository, cinemaRepository);
    }

    @Test
    void deveListarSessoesComStatus200() {
        when(service.findAll()).thenReturn(List.of(sessao(1L), sessao(2L)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("size()", is(2));
    }

    @Test
    void deveBuscarSessaoPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(sessao(1L));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("status", equalTo("EM_EXIBICAO"));
    }

    @Test
    void deveBuscarSessaoPorFilmeComStatus200() {
        when(service.findByFilme(3L)).thenReturn(List.of(sessao(1L)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/filme/3")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarSessaoPorCinemaComStatus200() {
        when(service.findByCinema(4L)).thenReturn(List.of(sessao(1L)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/cinema/4")
        .then()
            .statusCode(200);
    }

    @Test
    void deveBuscarSessaoPorStatusComStatus200() {
        when(service.findByStatus(2L)).thenReturn(List.of(sessao(1L)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/status/2")
        .then()
            .statusCode(200);
    }

    @Test
    void deveBuscarSessoesEmExibicaoComStatus200() {
        when(service.findSessoesEmExibicao(isNull())).thenReturn(List.of(sessao(1L)));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/em-exibicao")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarSessaoComStatus201() {
        mockRelacionamentosSessao();
        when(service.existsBySalaAndHorario(eq(5L), any(LocalDateTime.class), any(LocalDateTime.class), isNull())).thenReturn(false);
        when(service.create(any(Sessao.class))).thenAnswer(invocation -> {
            Sessao sessao = invocation.getArgument(0);
            sessao.setId(10L);
            return sessao;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(sessaoPayload())
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .body("id", equalTo(10))
            .body("salasIds[0]", equalTo(5));
    }

    @Test
    void deveAtualizarSessaoComStatus200() {
        mockRelacionamentosSessao();
        when(service.existsBySalaAndHorario(eq(5L), any(LocalDateTime.class), any(LocalDateTime.class), eq(1L))).thenReturn(false);
        doNothing().when(service).update(any(Long.class), any(Sessao.class));
        when(service.findById(1L)).thenReturn(sessao(1L));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(sessaoPayload())
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }

    @Test
    void deveRemoverSessaoComStatus204() {
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
                {"inicio":null,"fim":null,"capacidadeTotal":null,"capacidadeDisponivel":null,"statusId":null,"tipoSessaoId":null,"filmeId":null,"cinemaId":null,"salasIds":null}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Sessao.class));
    }

    private void mockRelacionamentosSessao() {
        when(filmeRepository.findById(3L)).thenReturn(filme(3L, "Deadpool 3"));
        when(cinemaRepository.findById(4L)).thenReturn(cinema(4L, "Cinemark"));
        when(salaRepository.findById(5L)).thenReturn(sala(5L, 1));
    }

    private String sessaoPayload() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(20).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fim = inicio.plusHours(2);
        return """
            {"inicio":"%s","fim":"%s","capacidadeTotal":150,"capacidadeDisponivel":150,"statusId":2,"tipoSessaoId":1,"filmeId":3,"cinemaId":4,"salasIds":[5]}
            """.formatted(inicio, fim);
    }

    private Sessao sessao(Long id) {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(20).withMinute(0).withSecond(0).withNano(0);
        Sessao sessao = new Sessao();
        sessao.setId(id);
        sessao.setInicio(inicio);
        sessao.setFim(inicio.plusHours(2));
        sessao.setCapacidadeTotal(150);
        sessao.setCapacidadeDisponivel(150);
        sessao.setStatus(StatusSessao.EM_EXIBICAO);
        sessao.setTipo(TipoSessao.DUBLADA);
        sessao.setFilme(filme(3L, "Deadpool 3"));
        sessao.setCinema(cinema(4L, "Cinemark"));
        sessao.setSalas(List.of(sala(5L, 1)));
        return sessao;
    }

    private Filme filme(Long id, String nome) {
        Filme filme = new Filme();
        filme.setId(id);
        filme.setNome(nome);
        return filme;
    }

    private Cinema cinema(Long id, String nome) {
        Cinema cinema = new Cinema();
        cinema.setId(id);
        cinema.setNome(nome);
        return cinema;
    }

    private Sala sala(Long id, Integer numero) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setNumero(numero);
        return sala;
    }
}
