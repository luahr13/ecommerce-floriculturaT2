package br.luahr.topicos1.resource;

import java.util.List;

import org.jboss.logging.Logger;

import br.luahr.topicos1.application.Result;
import br.luahr.topicos1.dto.CompraDTO;
import br.luahr.topicos1.dto.CompraResponseDTO;
import br.luahr.topicos1.service.CompraService;
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

@Path("/compras")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompraResource {

    @Inject
    CompraService compraService;

    private static final Logger LOG = Logger.getLogger(ClienteResource.class);

    @GET
    @RolesAllowed({"Admin", "User"})
    public List<CompraResponseDTO> getAll() {
        LOG.info("Buscando todos os clientes.");
        LOG.debug("Debug de busca de lista de clientes.");
        return compraService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public CompraResponseDTO findById(@PathParam("id") Long id) {
        LOG.info("Buscando um cliente por ID.");
        LOG.debug("Debug de busca de ID de clientes.");
        return compraService.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"Admin"})
    public Response insert(CompraDTO compraDTO) {
        LOG.info("Inserindo um cliente.");
        try {
            CompraResponseDTO compra = compraService.create(compraDTO);
            return Response.status(Status.CREATED).entity(compra).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de inserção de clientes.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, CompraDTO compraDTO) {
        LOG.info("Atualiza um cliente.");
        try {
            CompraResponseDTO compra = compraService.update(id, compraDTO);
            return Response.status(Status.NO_CONTENT).entity(compra).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de updat de clientes.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        LOG.info("deleta um cliente.");
        compraService.delete(id);
        LOG.debug("Debug de deletar clientes.");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin", "User"})
    public long count(){
        LOG.info("Conta clientes.");
        return compraService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin", "User"})
    public List<CompraResponseDTO> search(@PathParam("nome") String nome){
        LOG.info("Busca nome de clientes.");
        return compraService.findByNome(nome);
    }
    
}
