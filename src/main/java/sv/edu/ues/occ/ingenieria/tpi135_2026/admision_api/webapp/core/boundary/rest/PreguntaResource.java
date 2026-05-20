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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;

@Path("pregunta")
public class PreguntaResource implements Serializable {

    @Inject
    PreguntaDAO preguntaDAO;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crear(Pregunta pregunta, @Context UriInfo uriInfo) {
        if (pregunta != null && pregunta.getIdPregunta() == null) {
            pregunta.setIdPregunta(UUID.randomUUID());
            preguntaDAO.crear(pregunta);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(pregunta.getIdPregunta().toString());
            return Response.created(uriBuilder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y no debe tener un ID asignado").build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        if (id != null) {
            Pregunta pregunta = preguntaDAO.buscarPorId(id);
            if (pregunta != null) {
                preguntaDAO.eliminar(pregunta);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El ID no puede ser nulo").build();
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
            List<Pregunta> preguntas = preguntaDAO.buscarPorRango(firstResult, maxResults);
            Long total = preguntaDAO.contar();
            Response.ResponseBuilder responseBuilder = Response.ok(preguntas)
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
            Pregunta encontrado = preguntaDAO.buscarPorId(id);
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
    public Response actualizar(@PathParam("id") UUID id, Pregunta pregunta) {
        if (pregunta != null && id != null) {
            Pregunta existente = preguntaDAO.buscarPorId(id);
            if (existente != null) {
                pregunta.setIdPregunta(id);
                preguntaDAO.actualizar(pregunta);
                return Response.ok(pregunta).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y debe tener un ID asignado").build();
    }
}