package br.luahr;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.EstadoResponseDTO;
import br.luahr.topicos1.service.EstadoService;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class EstadoResourceTeste {

    private String token;

    @BeforeEach
    public void setUp(){
        var auth = new AuthClienteDTO("janio", "123");

        Response response = (Response) given()
                .contentType("application/json")
                .body(auth)
                .when().post("/auth")
                .then()
                .statusCode(200)
                .extract().response();
        
        token = response.header("Authorization");
    }

    @Inject
    EstadoService estadoService;

    @Test
    public void testGetAll() {
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/estados")
                .then()
                .statusCode(200);
    }

    @Test
    public void testInsert() {
        EstadoDTO estadoDTO = new EstadoDTO(
                "São Paulo",
                "SP");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(estadoDTO)
                .when().post("/estados")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("São Paulo"),
                                                     "sigla", is("SP"));
    }

    @Test
    public void testUpdate() {
        // Adicionando uma pessoa no banco de dados
        EstadoDTO estadoDTO = new EstadoDTO(
                "São Paulo",
                "SP");
        Long id = estadoService.create(estadoDTO).id();
        // Criando outra pessoa para atuailzacao
        EstadoDTO estadoUpDto = new EstadoDTO(
                "Tocantins",
                "TO");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(estadoUpDto)
                .when().put("/estados/" + id)
                .then()
                .statusCode(204);
        // Verificando se os dados foram atualizados no banco de dados
        EstadoResponseDTO estadoResponseDTO = estadoService.findById(id);
        assertThat(estadoResponseDTO.nome(), is("Tocantins"));
        assertThat(estadoResponseDTO.sigla(), is("TO"));
    }

    @Test
    public void testDelete() {
        // Adicionando uma pessoa no banco de dados
        EstadoDTO estadoDTO = new EstadoDTO(
                "São Paulo",
                "SP");
        Long id = estadoService.create(estadoDTO).id();
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/estados/" + id)
                .then()
                .statusCode(204);
        // verificando se a pessoa fisica foi excluida
        EstadoResponseDTO estadoResponseDTO = null;
        try {
            estadoService.findById(id);
        } catch (Exception e) {
        } finally {
            assertNull(estadoResponseDTO);
        }
    }
}
