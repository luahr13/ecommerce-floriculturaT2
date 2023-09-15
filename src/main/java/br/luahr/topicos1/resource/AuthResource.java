package br.luahr.topicos1.resource;

import org.eclipse.microprofile.jwt.JsonWebToken;

import br.luahr.topicos1.dto.AuthClienteDTO;
import br.luahr.topicos1.model.Cliente;
import br.luahr.topicos1.service.ClienteService;
import br.luahr.topicos1.service.HashService;
import br.luahr.topicos1.service.TokenJwtService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    HashService hashService;

    @Inject
    ClienteService clienteService;

    @Inject
    TokenJwtService tokenService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(AuthClienteDTO authDTO) {
        String hash = hashService.getHashSenha(authDTO.senha());

        Cliente cliente = clienteService.findByLoginAndSenha(authDTO.login(), hash);

        if (cliente == null) {
            return Response.status(Status.NO_CONTENT)
                .entity("Usuario não encontrado").build();
        } 
        return Response.ok()
            .header("Authorization", tokenService.generateJwt(cliente))
            .build();
    }
}