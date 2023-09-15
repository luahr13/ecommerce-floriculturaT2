package br.luahr;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EnderecoResponseDTO;
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.MunicipioDTO;
import br.luahr.topicos1.model.Municipio;
import br.luahr.topicos1.service.EnderecoService;
import br.luahr.topicos1.service.EstadoService;
import br.luahr.topicos1.service.MunicipioService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class EnderecoResourceTeste {

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
        EnderecoService enderecoService;

        @Inject
        MunicipioService municipioService;

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
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();

                EnderecoDTO enderecoDTO = new EnderecoDTO(
                                "Norte",
                                "13",
                                "05",
                                "11111-111",
                                idMunicipio);
                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(enderecoDTO)
                                .when().post("/enderecos")
                                .then()
                                .statusCode(201)
                                .body("id", notNullValue(),
                                                "bairro", is("Norte"),
                                                "numero", is("13"),
                                                "complemento", is("05"),
                                                "cep", is("11111-111"),
                                                "municipio", notNullValue(Municipio.class));
        }

        @Test
        public void testUpdate() {
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();

                EnderecoDTO enderecoDTO = new EnderecoDTO(
                                "Norte",
                                "13",
                                "05",
                                "11111-111",
                                idMunicipio);
                Long idEdLong = enderecoService.create(enderecoDTO).id();

                EnderecoDTO enderecoUpDto = new EnderecoDTO(
                                "Centro",
                                "13",
                                "05",
                                "11111-112",
                                1L);
                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(enderecoUpDto)
                                .when().put("/enderecos/" + idEdLong)
                                .then()
                                .statusCode(204);
                // Verificando se os dados foram atualizados no banco de dados
                EnderecoResponseDTO estadoResponseDTO = enderecoService.findById(idEdLong);
                assertThat(estadoResponseDTO.bairro(), is("Centro"));
                assertThat(estadoResponseDTO.numero(), is("13"));
                assertThat(estadoResponseDTO.complemento(), is("05"));
                assertThat(estadoResponseDTO.cep(), is("11111-112"));
                assertThat(estadoResponseDTO.municipio(), notNullValue());
        }

        @Test
        public void testDelete() {
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();

                EnderecoDTO enderecoDTO = new EnderecoDTO(
                                "Norte",
                                "13",
                                "05",
                                "11111-111",
                                idMunicipio);
                Long idELong = enderecoService.create(enderecoDTO).id();
                given()
                                .header("Authorization", "Bearer " + token)
                                .when().delete("/enderecos/" + idELong)
                                .then()
                                .statusCode(204);

                assertThrows(NotFoundException.class, () -> enderecoService.findById(idELong));
        }
}
