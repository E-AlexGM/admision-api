package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
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
    @Path("{id_area}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id_area") UUID idArea,
            @Context UriInfo uriInfo) {
        if (idPregunta == null || idArea == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(),
                            "Se requiere idPregunta e idArea")
                    .build();
        }
        try {
            Pregunta pregunta = preguntaDAO.buscarPorId(idPregunta);
            Area area = areaDAO.buscarPorId(idArea);
            if (pregunta == null || area == null) {
                String mensaje = pregunta == null ? "Pregunta no encontrada" : "Area no encontrada";
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), mensaje).build();
            }
            PreguntaArea preguntaArea = new PreguntaArea();
            preguntaArea.setIdPregunta(pregunta);
            preguntaArea.setIdArea(area);
            preguntaAreaDAO.crear(preguntaArea);

            return Response.created(uriInfo.getAbsolutePath()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorRango(
            @PathParam("id_pregunta") UUID idPregunta,
            @QueryParam("first") @DefaultValue("0") int firstResult,
            @QueryParam("max") @DefaultValue("50") int maxResults) {
        try {
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
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("{id_area}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id_area") UUID idArea) {
        if (idPregunta != null && idArea != null) {
            try {
                PreguntaArea encontrado = preguntaAreaDAO.buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
                if (encontrado != null) {
                    return Response.ok(encontrado).type(MediaType.APPLICATION_JSON).build();
                }
                return Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado")
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage())
                        .build();
            }
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
            try {
                PreguntaArea preguntaArea = preguntaAreaDAO.buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
                if (preguntaArea != null) {
                    preguntaAreaDAO.eliminar(preguntaArea);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado")
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage())
                        .build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPregunta e idArea no pueden ser nulos")
                .build();
    }
}
