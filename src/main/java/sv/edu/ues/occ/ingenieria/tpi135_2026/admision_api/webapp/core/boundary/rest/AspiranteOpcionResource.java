package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;

@Path("aspirante/{id_aspirante}/opcion")
public class AspiranteOpcionResource implements Serializable {

    @Inject
    AspiranteOpcionDAO aspiranteOpcionDAO;

    @Inject
    AspiranteDAO aspiranteDAO;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crear(
            @PathParam("id_aspirante") UUID idAspirante,
            AspiranteOpcion aspiranteOpcion,
            @Context UriInfo uriInfo) {
        if (aspiranteOpcion == null || idAspirante == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y idAspirante no puede ser nulo").build();
        }
        try {
            Aspirante aspirante = aspiranteDAO.buscarPorId(idAspirante);
            if (aspirante == null) {
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Aspirante no encontrado").build();
            }
            aspiranteOpcion.setIdAspiranteOpcion(UUID.randomUUID());
            aspiranteOpcion.setIdAspirante(aspirante);
            if (aspiranteOpcion.getFechaCreacion() == null) {
                aspiranteOpcion.setFechaCreacion(OffsetDateTime.now());
            }
            aspiranteOpcionDAO.crear(aspiranteOpcion);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(aspiranteOpcion.getIdAspiranteOpcion().toString());
            return Response.created(uriBuilder.build()).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(
            @PathParam("id_aspirante") UUID idAspirante,
            @QueryParam("first") @DefaultValue("0") int firstResult,
            @QueryParam("max") @DefaultValue("50") int maxResults) {
        if (idAspirante == null || firstResult < 0 || maxResults <= 0 || maxResults > 50) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50").build();
        }
        try {
            List<AspiranteOpcion> registros = aspiranteOpcionDAO.buscarPorAspiranteRango(idAspirante, firstResult, maxResults);
            Long total = aspiranteOpcionDAO.contarPorAspirante(idAspirante);
            return Response.ok(registros)
                    .header(ResponseHeaders.TOTAL_RECORDS.toString(), total)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_aspirante") UUID idAspirante,
            @PathParam("id") UUID id) {
        if (id == null || idAspirante == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idAspirante e id no pueden ser nulos").build();
        }
        try {
            AspiranteOpcion encontrado = aspiranteOpcionDAO.buscarPorIdYAspirante(id, idAspirante);
            if (encontrado != null) {
                return Response.ok(encontrado).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(
            @PathParam("id_aspirante") UUID idAspirante,
            @PathParam("id") UUID id,
            AspiranteOpcion aspiranteOpcion) {
        if (aspiranteOpcion == null || idAspirante == null || id == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso o los identificadores no pueden ser nulos").build();
        }
        try {
            AspiranteOpcion existente = aspiranteOpcionDAO.buscarPorIdYAspirante(id, idAspirante);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
            }
            aspiranteOpcion.setIdAspiranteOpcion(id);
            aspiranteOpcion.setIdAspirante(existente.getIdAspirante());
            if (aspiranteOpcion.getFechaCreacion() == null) {
                aspiranteOpcion.setFechaCreacion(existente.getFechaCreacion());
            }
            aspiranteOpcionDAO.actualizar(aspiranteOpcion);
            return Response.ok(aspiranteOpcion).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(
            @PathParam("id_aspirante") UUID idAspirante,
            @PathParam("id") UUID id) {
        if (id == null || idAspirante == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idAspirante e id no pueden ser nulos").build();
        }
        try {
            AspiranteOpcion aspiranteOpcion = aspiranteOpcionDAO.buscarPorIdYAspirante(id, idAspirante);
            if (aspiranteOpcion != null) {
                aspiranteOpcionDAO.eliminar(aspiranteOpcion);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }
}
