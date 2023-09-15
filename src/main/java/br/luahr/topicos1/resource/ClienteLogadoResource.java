package br.luahr.topicos1.resource;

import java.io.IOException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.luahr.topicos1.application.Result;
import br.luahr.topicos1.dto.ClienteResponseDTO;
import br.luahr.topicos1.form.ImageForm;
import br.luahr.topicos1.service.ClienteService;
import br.luahr.topicos1.service.FileService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

@Path("/usuariologado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteLogadoResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ClienteService clienteService;

    @Inject
    FileService fileService;

    @GET
    @RolesAllowed({ "Admin", "User" })
    public Response getUsuario() {
        // obtendo o login a partir do token
        String login = jwt.getSubject();
        ClienteResponseDTO clienteResponseDTO = clienteService.findByLogin(login);

        return Response.ok(clienteResponseDTO).build();
    }

    @PATCH
    @Path("/novaimagem")
    @RolesAllowed({ "Admin", "User" })
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(@MultipartForm ImageForm form) {
        String nomeImagem = "";

        try {
            nomeImagem = fileService.salvarImagemUsuario(form.getImagem(), form.getNomeImagem());
        } catch (IOException e) {
            Result result = new Result(e.getMessage());
            return Response.status(Status.CONFLICT).entity(result).build();
        }

        String login = jwt.getSubject();
        ClienteResponseDTO cliente = clienteService.findByLogin(login);

        cliente = clienteService.update(cliente.id(), nomeImagem);

        return Response.ok(cliente).build();
    }

    @GET
    @Path("/download/{nomeImagem}")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        ResponseBuilder response = Response.ok(fileService.download(nomeImagem));
        response.header("Content-Disposition", "attachment;filename=" + nomeImagem);
        return response.build();
    }
}
