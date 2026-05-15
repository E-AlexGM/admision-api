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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaAreaPK;

@Path("pregunta/{id_pregunta}/area")
public class PreguntaAreaResource implements Serializable {

    @Inject
    PreguntaAreaDAO preguntaAreaDAO;

    @Inject
    PreguntaDAO preguntaDAO;

    @Inject
    AreaDAO areaDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(
            @PathParam("id_pregunta") UUID idPregunta,
            PreguntaArea preguntaArea,
            @Context UriInfo uriInfo) {
        if (idPregunta == null || preguntaArea == null || preguntaArea.getIdArea() == null
                || preguntaArea.getIdArea().getIdArea() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(),
                            "Se requiere idPregunta e idArea")
                    .build();
        }
        Pregunta pregunta = preguntaDAO.buscarPorId(idPregunta);
        Area area = areaDAO.buscarPorId(preguntaArea.getIdArea().getIdArea());
        if (pregunta == null || area == null) {
            String mensaje = pregunta == null ? "Pregunta no encontrada" : "Area no encontrada";
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), mensaje).build();
        }
        preguntaArea.setIdPregunta(pregunta);
        preguntaArea.setIdArea(area);
        preguntaAreaDAO.crear(preguntaArea);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(preguntaArea.getIdArea().getIdArea().toString());
        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorRango(
            @PathParam("id_pregunta") UUID idPregunta,
            @QueryParam("first") @DefaultValue("0") int firstResult,
            @QueryParam("max") @DefaultValue("50") int maxResults) {
        if (idPregunta != null && firstResult >= 0 && maxResults > 0 && maxResults <= 50) {
            List<PreguntaArea> registros = preguntaAreaDAO.buscarPorPreguntaRango(idPregunta, firstResult, maxResults);
            Long total = preguntaAreaDAO.contarPorPregunta(idPregunta);
            return Response.ok(registros)
                .header(ResponseHeaders.TOTAL_RECORDS.toString(), total)
                .type(MediaType.APPLICATION_JSON)
                .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
            .header(ResponseHeaders.WRONG_PARAMETER.toString(),
                "Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50")
            .build();
    }

    @GET
    @Path("{id_area}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id_area") UUID idArea) {
        if (idPregunta != null && idArea != null) {
            PreguntaArea encontrado = preguntaAreaDAO.buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
            if (encontrado != null) {
                return Response.ok(encontrado).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPregunta e idArea no pueden ser nulos")
                .build();
    }

    @DELETE
    @Path("{id_area}")
    public Response eliminar(
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id_area") UUID idArea) {
        if (idPregunta != null && idArea != null) {
            PreguntaArea preguntaArea = preguntaAreaDAO.buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
            if (preguntaArea != null) {
                preguntaAreaDAO.eliminar(preguntaArea);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPregunta e idArea no pueden ser nulos")
                .build();
    }
}
