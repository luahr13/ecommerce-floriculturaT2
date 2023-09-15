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
import br.luahr.topicos1.dto.ClienteDTO;
import br.luahr.topicos1.dto.ClienteResponseDTO;
import br.luahr.topicos1.service.ClienteService;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService clienteService;

    private static final Logger LOG = Logger.getLogger(ClienteResource.class);

    @GET
    @RolesAllowed({"Admin", "User"})
    public List<ClienteResponseDTO> getAll() {
        LOG.info("Buscando todos os clientes.");
        LOG.debug("Debug de busca de lista de clientes.");
        return clienteService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public ClienteResponseDTO findById(@PathParam("id") Long id) {
        LOG.info("Buscando um cliente por ID.");
        LOG.debug("Debug de busca de ID de clientes.");
        return clienteService.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"Admin"})
    public Response insert(ClienteDTO clienteDTO) {
        LOG.info("Inserindo um cliente.");
        try {
            ClienteResponseDTO cliente = clienteService.create(clienteDTO);
            return Response.status(Status.CREATED).entity(cliente).build();
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
    public Response update(@PathParam("id") Long id, ClienteDTO clienteDTO) {
        LOG.info("Atualiza um cliente.");
        try {
            ClienteResponseDTO cliente = clienteService.update(id, clienteDTO);
            return Response.status(Status.NO_CONTENT).entity(cliente).build();
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
        clienteService.delete(id);
        LOG.debug("Debug de deletar clientes.");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin", "User"})
    public long count(){
        LOG.info("Conta clientes.");
        return clienteService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin", "User"})
    public List<ClienteResponseDTO> search(@PathParam("nome") String nome){
        LOG.info("Busca nome de clientes.");
        return clienteService.findByNome(nome);
    }
    
}
