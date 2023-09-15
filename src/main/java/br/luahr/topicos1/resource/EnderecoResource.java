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
import br.luahr.topicos1.dto.EnderecoDTO;
import br.luahr.topicos1.dto.EnderecoResponseDTO;
import br.luahr.topicos1.service.EnderecoService;

@Path("/enderecos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnderecoResource {
    @Inject
    EnderecoService enderecoService;

    private static final Logger LOG = Logger.getLogger(EnderecoResource.class);

    @GET
    @RolesAllowed({"Admin", "User"})
    public List<EnderecoResponseDTO> getAll() {
        LOG.info("Buscando todos os enderecos.");
        LOG.debug("Debug de busca de lista de enderecos.");
         return enderecoService.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"Admin", "User"})
    public EnderecoResponseDTO findById(@PathParam("id") Long id) {
        LOG.info("Buscando um endereco por ID.");
        LOG.debug("Debug de busca de ID de enderecos.");
        return enderecoService.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"Admin"})
    public Response insert(EnderecoDTO enderecoDTO) {
        LOG.info("Inserindo um endereco.");
        try {
            EnderecoResponseDTO endereco = enderecoService.create(enderecoDTO);
            return Response.status(Status.CREATED).entity(endereco).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de inserção de enderecos.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({"Admin"})
    public Response update(@PathParam("id") Long id, EnderecoDTO enderecoDTO) {
        LOG.info("Atualiza um endereco.");
        try {
            enderecoService.update(id, enderecoDTO);
            return Response.status(Status.NO_CONTENT).build();
        } catch(ConstraintViolationException e) {
            Result result = new Result(e.getConstraintViolations());
            LOG.debug("Debug de updat de enderecos.");
            return Response.status(Status.NOT_FOUND).entity(result).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("id") Long id) {
        LOG.info("deleta um endereco.");
        enderecoService.delete(id);
        LOG.debug("Debug de deletar enderecos.");
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({"Admin", "User"})
    public long count(){
        LOG.info("Conta enderecos.");
        return enderecoService.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({"Admin", "User"})
    public List<EnderecoResponseDTO> search(@PathParam("nome") String nome){
        LOG.info("Busca nome de enderecos.");
        return enderecoService.findByNome(nome);
    }
}
