package br.luahr.topicos1.resource;

import java.util.List;

import org.jboss.logging.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import br.luahr.topicos1.application.Result;
import br.luahr.topicos1.dto.UsuarioDTO;
import br.luahr.topicos1.dto.UsuarioResponseDTO;
import br.luahr.topicos1.service.UsuarioService;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    private static final Logger LOG = Logger.getLogger(UsuarioResource.class);

    @GET
    @RolesAllowed({"Admin", "User"})
    public List<UsuarioResponseDTO> getAll() {
        LOG.info("Buscando todos os usuarios.");
        LOG.debug("Debug de busca de lista de usuarios.");
        return usuarioService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public UsuarioResponseDTO findById(@PathParam("id") Long id) {
        LOG.info("Buscando um usuario por ID.");
        LOG.debug("Debug de busca de ID de usuarios.");
        return usuarioService.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"Admin"})
    public Response insert(UsuarioDTO usuarioDTO) {
        LOG.info("Inserindo um usuario.");
        try {
            UsuarioResponseDTO usuario = usuarioService.create(usuarioDTO);
            return Response.status(Status.CREATED).entity(usuario).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de inserção de usuarios.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, UsuarioDTO usuarioDTO) {
        LOG.info("Atualiza um usuario.");
        try {
            UsuarioResponseDTO usuario = usuarioService.update(id, usuarioDTO);
            return Response.status(Status.NO_CONTENT).entity(usuario).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de updat de usuarios.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        LOG.info("deleta um usuario.");
        usuarioService.delete(id);
        LOG.debug("Debug de deletar usuarios.");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin", "User"})
    public long count(){
        LOG.info("Conta usuarios.");
        return usuarioService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin", "User"})
    public List<UsuarioResponseDTO> search(@PathParam("nome") String nome){
        LOG.info("Busca nome de usuarios.");
        return usuarioService.findByNome(nome);
    }
    
}
