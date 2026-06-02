package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;

@Path("aspirante")
public class AspiranteResource implements Serializable {

    @Inject
    AspiranteDAO aspiranteDAO;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crear(Aspirante aspirante, @Context UriInfo uriInfo) {

        if (aspirante != null && aspirante.getIdAspirante() == null) {
            aspirante.setIdAspirante(UUID.randomUUID());
            aspiranteDAO.crear(aspirante);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(aspirante.getIdAspirante().toString());
            return Response.created(uriBuilder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y no debe tener un ID asignado").build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        if (id != null) {
            Aspirante aspirante = aspiranteDAO.buscarPorId(id);
            if (aspirante != null) {
                aspiranteDAO.eliminar(aspirante);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El ID no puede ser nulo").build();
    }

    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(
            @QueryParam("correo") String correo,
            @QueryParam("apellidos") String apellidos) {
        if (correo != null && !correo.isBlank()) {

            Aspirante encontrado = aspiranteDAO.buscarPorCorreo(correo);

            if (encontrado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "No se encontró el aspirante").build();
            }
            return Response.ok(encontrado).type(MediaType.APPLICATION_JSON).build();
        }
        if (apellidos != null && !apellidos.isBlank()) {
            List<Aspirante> resultado =
                    aspiranteDAO.buscarPorApellidos(apellidos);
            if (resultado == null || resultado.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "No se encontraron aspirantes").build();
            }
            return Response.ok(resultado).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .header(ResponseHeaders.WRONG_PARAMETER.toString(), "Debe especificar al menos un criterio de búsqueda").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(
            @QueryParam("first")
            @DefaultValue("0")
            int firstResult,
            @QueryParam("max")
            @DefaultValue("50")
            int maxResults) {
        if (firstResult >= 0 && maxResults > 0 && maxResults <= 50) {
            List<Aspirante> aspirantes = aspiranteDAO.buscarPorRango(firstResult, maxResults);
            Long total = aspiranteDAO.contar();
            Response.ResponseBuilder responseBuilder = Response.ok(aspirantes)
                    .header(ResponseHeaders.TOTAL_RECORDS.toString(), total)
                    .type(MediaType.APPLICATION_JSON);
            return responseBuilder.build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50").build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") UUID id) {
        if (id != null) {
            Aspirante encontrado = aspiranteDAO.buscarPorId(id);
            if (encontrado != null) {
                Response.ResponseBuilder builder = Response.ok(encontrado).type(MediaType.APPLICATION_JSON);
                return builder.build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "id: " + id).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") UUID id, Aspirante aspirante) {
        if (aspirante != null && id != null) {
            Aspirante existente = aspiranteDAO.buscarPorId(id);
            if (existente != null) {
                aspirante.setIdAspirante(id);
                aspiranteDAO.actualizar(aspirante);
                return Response.ok(aspirante).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y debe tener un ID asignado").build();
    }
}