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
import br.luahr.topicos1.dto.EstadoDTO;
import br.luahr.topicos1.dto.EstadoResponseDTO;
import br.luahr.topicos1.service.EstadoService;

@Path("/estados")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EstadoResource {
    @Inject
    EstadoService estadoService;

    private static final Logger LOG = Logger.getLogger(EstadoResource.class);

    @GET
    @RolesAllowed({ "Admin", "User" })
    public List<EstadoResponseDTO> getAll() {
        LOG.info("Buscando todos os estados.");
        LOG.debug("Debug de busca de lista de estados.");
        return estadoService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "Admin", "User" })
    public EstadoResponseDTO findById(@PathParam("id") Long id) {
        LOG.info("Buscando ID de estados.");
        return estadoService.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({ "Admin" })
    public Response insert(EstadoDTO estadoDTO) {
        LOG.info("Inserindo um estado.");
        try {
            EstadoResponseDTO estado = estadoService.create(estadoDTO);
            return Response.status(Status.CREATED).entity(estado).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de inserção de estados.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "Admin" })
    public Response update(@PathParam("id") Long id, EstadoDTO estadoDTO) {
        LOG.info("Atualiza um estado.");
        try {
            estadoService.update(id, estadoDTO);
            return Response.status(Status.NO_CONTENT).build();
        } catch (ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de updat de estados.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response delete(@PathParam("id") Long id) {
        LOG.info("deleta um estado.");
        estadoService.delete(id);
        LOG.debug("Debug de deletar estados.");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({ "Admin", "User" })
    public long count() {
        LOG.info("Conta estados.");
        return estadoService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({ "Admin", "User" })
    public List<EstadoResponseDTO> search(@PathParam("nome") String nome) {
        LOG.info("Busca nome de estados.");
        return estadoService.findByNome(nome);
    }
}
