package br.luahr;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.MunicipioDTO;
import br.luahr.topicos1.dto.MunicipioResponseDTO;
import br.luahr.topicos1.model.Estado;
import br.luahr.topicos1.service.EstadoService;
import br.luahr.topicos1.service.MunicipioService;
import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

@QuarkusTest
public class MunicipioResourceTeste {

        private String token;

        @BeforeEach
        public void setUp() {
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
        MunicipioService municipioService;

        @Inject
        EstadoService estadoService;

        @Test
        public void testGetAll() {
                given()
                                .header("Authorization", "Bearer " + token)
                                .when().get("/municipios")
                                .then()
                                .statusCode(200);
        }

        @Test
        public void testeCreatMunicipio() {
                Long idE = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                MunicipioDTO municipioDTO = new MunicipioDTO(
                                "Palmas",
                                idE);

                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(municipioDTO)
                                .when().post("/municipios")
                                .then()
                                .statusCode(201)
                                .body("id", notNullValue(),
                                "nome", is("Palmas"),
                                "estado", notNullValue(Estado.class));
        }

        @Test
        public void testUpdate() {
                // Adicionando uma estado no banco de dados
                EstadoDTO estadoDTO = new EstadoDTO(
                                "Tocantins",
                                "TO");

                Long idEstado = estadoService.create(estadoDTO).id();
                // Adicionando uma municipio no banco de dados
                MunicipioDTO municipioDTO = new MunicipioDTO(
                                "Palmas",
                                idEstado);
                Long id = municipioService.create(municipioDTO).id();

                // Criando outro cidade para atuailzacao
                MunicipioDTO municipioUpDTO = new MunicipioDTO(
                                "Palmas_TO",
                                idEstado);

                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(municipioUpDTO)
                                .when().put("/municipios/" + id)
                                .then()
                                .statusCode(204);

                // Verificando se os dados foram atualizados no banco de dados
                MunicipioResponseDTO municipioResponseDTO = municipioService.findById(id);
                assertThat(municipioResponseDTO.nome(), is("Palmas_TO"));
                assertThat(municipioResponseDTO.estado(), notNullValue());
        }

        @Test
        public void testDelete() {
                // Adicionando um município no banco de dados
                MunicipioDTO municipioDTO = new MunicipioDTO(
                                "Palmas",
                                1L);
                Long id = municipioService.create(municipioDTO).id();
                // Deletando o município
                given()
                                .header("Authorization", "Bearer " + token)
                                .when().delete("/municipios/" + id)
                                .then()
                                .statusCode(204);

                // Verificando se o município foi excluído do banco de dados
                assertThrows(NotFoundException.class, () -> municipioService.findById(id));
        }

}
