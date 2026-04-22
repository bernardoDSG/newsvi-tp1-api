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

import br.unitins.model.Cinema;
import br.unitins.model.Endereco;
import br.unitins.model.Sala;
import br.unitins.repository.EnderecoRepository;
import br.unitins.repository.SalaRepository;
import br.unitins.service.CinemaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class CinemaResourceHttpContractTest {

    private static final String BASE_URL = "/cinemas";

    @InjectMock
    CinemaService service;

    @InjectMock
    EnderecoRepository enderecoRepository;

    @InjectMock
    SalaRepository salaRepository;

    @BeforeEach
    void setUp() {
        reset(service, enderecoRepository, salaRepository);
    }

    @Test
    void deveListarCinemasComStatus200() {
        when(service.findAll()).thenReturn(List.of(cinema(1L, "Cinemark"), cinema(2L, "Cinepolis")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Cinemark"));
    }

    @Test
    void deveBuscarCinemaPorIdComStatus200() {
        when(service.findById(1L)).thenReturn(cinema(1L, "Cinemark"));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("endereco.id", equalTo(1));
    }

    @Test
    void deveBuscarCinemaPorNomeComStatus200() {
        when(service.findByNome("Cinemark")).thenReturn(List.of(cinema(1L, "Cinemark")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/find/Cinemark")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveBuscarCinemaPorCidadeComStatus200() {
        when(service.findByCidade("Palmas")).thenReturn(List.of(cinema(1L, "Cinemark")));

        given()
            .accept(ContentType.JSON)
        .when()
            .get(BASE_URL + "/cidade/Palmas")
        .then()
            .statusCode(200)
            .body("size()", is(1));
    }

    @Test
    void deveCriarCinemaComStatus201() {
        when(enderecoRepository.findById(1L)).thenReturn(endereco(1L));
        when(salaRepository.findById(5L)).thenReturn(sala(5L, 1));
        when(service.create(any(Cinema.class))).thenAnswer(invocation -> {
            Cinema cinema = invocation.getArgument(0);
            cinema.setId(10L);
            return cinema;
        });

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Cinemark","cnpj":"12.345.678/0001-90","telefone":"(63) 9999-9999","enderecoId":1,"salasIds":[5]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10))
            .body("enderecoId", equalTo(1))
            .body("salasNumeros[0]", equalTo("1"));
    }

    @Test
    void deveAtualizarCinemaComStatus200() {
        when(enderecoRepository.findById(1L)).thenReturn(endereco(1L));
        when(salaRepository.findById(5L)).thenReturn(sala(5L, 1));
        doNothing().when(service).update(any(Long.class), any(Cinema.class));
        when(service.findById(1L)).thenReturn(cinema(1L, "Cinemark"));

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {"nome":"Cinemark","cnpj":"12.345.678/0001-90","telefone":"(63) 9999-9999","enderecoId":1,"salasIds":[5]}
                """)
        .when()
            .put(BASE_URL + "/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("enderecoId", equalTo(1));
    }

    @Test
    void deveRemoverCinemaComStatus204() {
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
                {"nome":"","cnpj":"","telefone":"(63) 9999-9999","enderecoId":null,"salasIds":[]}
                """)
        .when()
            .post(BASE_URL)
        .then()
            .statusCode(422)
            .contentType(ContentType.JSON)
            .body("title", equalTo("Erro de validação"))
            .body("errors", hasSize(greaterThanOrEqualTo(1)));

        verify(service, never()).create(any(Cinema.class));
    }

    private Cinema cinema(Long id, String nome) {
        Cinema cinema = new Cinema();
        cinema.setId(id);
        cinema.setNome(nome);
        cinema.setCnpj("12.345.678/0001-90");
        cinema.setTelefone("(63) 9999-9999");
        cinema.setEndereco(endereco(1L));
        cinema.setSalas(List.of(sala(5L, 1)));
        return cinema;
    }

    private Endereco endereco(Long id) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Rua A");
        endereco.setNumero("10");
        endereco.setBairro("Centro");
        endereco.setCidade("Palmas");
        endereco.setEstado("TO");
        endereco.setCep("77000000");
        return endereco;
    }

    private Sala sala(Long id, Integer numero) {
        Sala sala = new Sala();
        sala.setId(id);
        sala.setNumero(numero);
        sala.setCapacidade(100);
        return sala;
    }
}
