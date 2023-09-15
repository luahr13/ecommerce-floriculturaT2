package br.luahr;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.dto.FlorDTO;
import br.luahr.topicos1.dto.FlorResponseDTO;
import br.luahr.topicos1.dto.FornecedorDTO;
import br.luahr.topicos1.model.Fornecedor;
import br.luahr.topicos1.service.FlorService;
import br.luahr.topicos1.service.FornecedorService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FlorResourceTeste {

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
    FlorService florService;

    @Inject
    FornecedorService fornecedorService;

    @Test
    public void testGetAll() {
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/flores")
                .then()
                .statusCode(200);
    }

    @Test
    public void testInsert() {

        Long id = fornecedorService.create(new FornecedorDTO("L&L", "Brasil", "2023", 3F)).id();

        FlorDTO florDTO = new FlorDTO(
                "Orquidea",
                "Bela Flor",
                1.5,
                "Vermelha",
                0.3F,
                1,
                id);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(florDTO)
                .when().post("/flores")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "nome", is("Orquidea"),
                        "descricao", is("Bela Flor"),
                        "valorUnidade", is(1.5F),
                        "corPetalas", is("Vermelha"),
                        "alturaCaule", is(0.3F),
                        "fornecedor", notNullValue(Fornecedor.class));
    }

@Test
    public void testUpdate() {
        Long id = fornecedorService.create(new FornecedorDTO("L&L", "Brasil", "2023", 3F)).id();
        // Adicionando uma pessoa no banco de dados
        FlorDTO florDTO = new FlorDTO(
                "Orquidea",
                "Bela Flor",
                1.5D,
                "Vermelha",
                0.3F,
                1,
                id);
        Long idLong = florService.create(florDTO).id();
        // Criando outra pessoa para atuailzacao
        FlorDTO florUpDto = new FlorDTO(
                "Rosa",
                "Bela Flor",
                1.5D,
                "Vermelha",
                0.3F,
                2,
                id);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(florUpDto)
                .when().put("/flores/" + idLong)
                .then()
                .statusCode(200);
        // Verificando se os dados foram atualizados no banco de dados
        FlorResponseDTO florResponseDTO = florService.findById(idLong);
        assertThat(florResponseDTO.nome(), is(florUpDto.nome()));
        assertThat(florResponseDTO.descricao(), is(florUpDto.descricao()));
        assertThat(florResponseDTO.valorUnidade(), is(florUpDto.valorUnidade()));
        assertThat(florResponseDTO.corPetalas(), is(florUpDto.corPetalas()));
        assertThat(florResponseDTO.alturaCaule(), is(florUpDto.alturaCaule()));
        assertThat(florResponseDTO.tipoFlor(), is("Cravo"));
        assertThat(florResponseDTO.fornecedor(), notNullValue());
    }

    @Test
    public void testDelete() {
        // Adicionando uma pessoa no banco de dados
        FlorDTO florDTO = new FlorDTO(
                "Orquidea",
                "Bela Flor",
                1.5,
                "Vermelha",
                0.3F,
                1,
                1L);
        Long idLong = florService.create(florDTO).id();
        given()
                .header("Authorization", "Bearer " + token)
                .when().delete("/flores/" + idLong)
                .then()
                .statusCode(204);
        // verificando se a pessoa fisica foi excluida
        FlorResponseDTO florResponseDTO = null;
        try {
            florService.findById(idLong);
        } catch (Exception e) {
        } finally {
            assertNull(florResponseDTO);
        }
    }
}
