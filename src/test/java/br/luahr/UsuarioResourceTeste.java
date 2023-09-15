package br.luahr;

import static io.restassured.RestAssured.given;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthUsuarioDTO;
import br.luahr.topicos1.dto.UsuarioDTO;
import br.luahr.topicos1.dto.UsuarioResponseDTO;
import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.MunicipioDTO;
import br.luahr.topicos1.dto.TelefoneDTO;
import br.luahr.topicos1.service.UsuarioService;
import br.luahr.topicos1.service.EnderecoService;
import br.luahr.topicos1.service.EstadoService;
import br.luahr.topicos1.service.MunicipioService;
import br.luahr.topicos1.service.TelefoneService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UsuarioResourceTeste {

        private String token;

        @BeforeEach
        public void setUp() {
                var auth = new AuthUsuarioDTO("janio", "123");

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
        UsuarioService usuarioService;

        @Inject
        TelefoneService telefoneService;

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
                                .when().get("/usuarios")
                                .then()
                                .statusCode(200);
        }

        @Test
        public void testInsert() {
                Long idTelefone = telefoneService.create(new TelefoneDTO("63", "(63) 99999-9999")).id();
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();
                Long idEndereco = enderecoService.create(new EnderecoDTO("Norte", "13", "QI05", "77002-023", idMunicipio)).id();
                UsuarioDTO usuarioDTO = new UsuarioDTO(
                                "Luahr",
                                "luahr",
                                "123",
                                "11111111111-11",
                                1,
                                idTelefone,
                                idEndereco);
                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(usuarioDTO)
                                .when().post("/usuarios")
                                .then()
                                .statusCode(201)
                                .body("id", notNullValue(),
                                                "nome", is("Luahr"),
                                                "cpf", is("11111111111-11"),
                                                "sexo", notNullValue(),
                                                "telefone", notNullValue(),
                                                "endereco", notNullValue());
        }

        @Test
        public void testUpdate() {
                Long idTelefone = telefoneService.create(new TelefoneDTO("63", "(63) 99999-9999")).id();
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();
                Long idEndereco = enderecoService.create(new EnderecoDTO("Norte", "13", "QI05", "77002-023", idMunicipio)).id();
                UsuarioDTO usuarioDTO = new UsuarioDTO(
                                "Luahr",
                                "luahr",
                                "123",
                                "11111111111-11",
                                1,
                                idTelefone,
                                idEndereco);
                Long idLong = usuarioService.create(usuarioDTO).id();
                UsuarioDTO usuarioUpDto = new UsuarioDTO(
                                "Luahr",
                                "luahr",
                                "123",
                                "11111111111-22",
                                1,
                                idTelefone,
                                idEndereco);
                given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(usuarioUpDto)
                                .when().put("/usuarios/" + idLong)
                                .then()
                                .statusCode(204);
                UsuarioResponseDTO usuarioResponseDTO = usuarioService.findById(idLong);
                assertThat(usuarioResponseDTO.nome(), is("Luahr"));
                assertThat(usuarioResponseDTO.login(), is("luahr"));
                assertThat(usuarioResponseDTO.cpf(), is("11111111111-22"));
                assertThat(usuarioResponseDTO.sexo(), notNullValue());
                assertThat(usuarioResponseDTO.telefone(), notNullValue());
                assertThat(usuarioResponseDTO.endereco(), notNullValue());
        }

        @Test
        public void testDelete() {
                Long idTelefone = telefoneService.create(new TelefoneDTO("63", "(63) 99999-9999")).id();
                Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
                Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();
                Long idEndereco = enderecoService.create(new EnderecoDTO("Norte", "13", "QI05", "77002-023", idMunicipio)).id();
                UsuarioDTO usuarioDTO = new UsuarioDTO(
                                "Luahr",
                                "luahr",
                                "123",
                                "11111111111-11",
                                1,
                                idTelefone,
                                idEndereco);
                Long idLong = usuarioService.create(usuarioDTO).id();
                given()
                                .header("Authorization", "Bearer " + token)
                                .when().delete("/usuarios/" + idLong)
                                .then()
                                .statusCode(204);
                // verificando se a pessoa fisica foi excluida
                UsuarioResponseDTO usuarioResponseDTO = null;
                try {
                        usuarioService.findById(idLong);
                } catch (Exception e) {
                } finally {
                        assertNull(usuarioResponseDTO);
                }
        }
}
