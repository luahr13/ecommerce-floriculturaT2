package br.luahr;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.dto.ClienteDTO;
import br.luahr.topicos1.dto.CompraDTO;
import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.FlorDTO;
import br.luahr.topicos1.dto.FornecedorDTO;
import br.luahr.topicos1.dto.MunicipioDTO;
import br.luahr.topicos1.dto.TelefoneDTO;
import br.luahr.topicos1.model.Cliente;
import br.luahr.topicos1.model.Produto;
import br.luahr.topicos1.service.ClienteService;
import br.luahr.topicos1.service.CompraService;
import br.luahr.topicos1.service.EnderecoService;
import br.luahr.topicos1.service.EstadoService;
import br.luahr.topicos1.service.FlorService;
import br.luahr.topicos1.service.FornecedorService;
import br.luahr.topicos1.service.MunicipioService;
import br.luahr.topicos1.service.TelefoneService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class CompraResourceTeste {

    private String token;

    @Inject
    FornecedorService fornecedorService;

    @Inject
    FlorService florService;

    @Inject
    ClienteService clienteService;

    @Inject
    EnderecoService enderecoService;

    @Inject
    MunicipioService municipioService;

    @Inject
    EstadoService estadoService;

    @Inject
    TelefoneService telefoneService;

    @Inject
    CompraService compraService;

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

    @Test
    public void testGetAll() {
        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/compras")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCompraInsert() {

        Long idEstado = estadoService.create(new EstadoDTO("Tocantins", "TO")).id();
        Long idMunicipio = municipioService.create(new MunicipioDTO("Palmas", idEstado)).id();
        Long idEndereco = enderecoService.create(new EnderecoDTO("Norte", "13", "QI05", "77002-023", idMunicipio)).id();

        Long idTelefone = telefoneService.create(new TelefoneDTO("63", "(63) 99999-9999")).id();

        Long idCliente = clienteService
                .create(new ClienteDTO("Luahr", "luahr", "123", "11111111111-11", 1, idTelefone, idEndereco)).id();
        Long idFornecedor = fornecedorService.create(new FornecedorDTO("L&L", "BR", "2023", 10F)).id();
        Long idProduto = florService
                .create(new FlorDTO("Orquidea", "Bela Flor", 1.5, "Vermelha", 0.3F, 1, idFornecedor)).id();

        Integer quantiProd = 10;
        CompraDTO compraDTO = new CompraDTO(idCliente, idProduto, quantiProd, 10D);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(compraDTO)
                .when().post("/compras")
                .then()
                .statusCode(201)
                .body("id", notNullValue(),
                        "cliente", notNullValue(Cliente.class),
                        "produto", notNullValue(Produto.class),
                        "quantidadeProduto", is(10),
                        "totalCompra", is(10F));
    }
}
