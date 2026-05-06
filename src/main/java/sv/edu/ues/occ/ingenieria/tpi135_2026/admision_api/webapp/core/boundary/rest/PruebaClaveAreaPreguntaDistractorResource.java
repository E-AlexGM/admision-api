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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractorPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaPK;

@Path("prueba_clave/{id_prueba_clave}/area/{id_area}/pregunta/{id_pregunta}/distractor")
public class PruebaClaveAreaPreguntaDistractorResource implements Serializable {

    @Inject
    PruebaClaveAreaPreguntaDistractorDAO pruebaClaveAreaPreguntaDistractorDAO;

    @Inject
    PruebaClaveAreaPreguntaDAO pruebaClaveAreaPreguntaDAO;

    @Inject
    DistractorDAO distractorDAO;

    @POST
    @Path("{id_distractor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea,
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id_distractor") UUID idDistractor,
            @Context UriInfo uriInfo) {
        if (idPruebaClave == null || idArea == null || idPregunta == null || idDistractor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos")
                    .build();
        }
        try {
            PruebaClaveAreaPregunta padre = pruebaClaveAreaPreguntaDAO.buscarPorId(
                    new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta));
            Distractor distractor = distractorDAO.buscarPorId(idDistractor);
            if (padre == null || distractor == null) {
                String message = (padre == null) ? "Padre no encontrado" : "Distractor no encontrado";
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), message).build();
            }
            PruebaClaveAreaPreguntaDistractor entity = new PruebaClaveAreaPreguntaDistractor();
            entity.setIdPruebaClave(padre.getIdPruebaClave());
            entity.setIdArea(padre.getIdArea());
            entity.setIdPregunta(padre.getIdPregunta());
            entity.setIdDistractor(distractor);
            pruebaClaveAreaPreguntaDistractorDAO.crear(entity);
            return Response.created(uriInfo.getAbsolutePath()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorRango(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea,
            @PathParam("id_pregunta") UUID idPregunta,
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {
        if (idPruebaClave == null || idArea == null || idPregunta == null ||
                first < 0 || max <= 0 || max > 50) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Parámetros inválidos").build();
        }
        try {
            List<PruebaClaveAreaPreguntaDistractor> registros = pruebaClaveAreaPreguntaDistractorDAO
                    .buscarPorPadreRango(idPruebaClave, idArea, idPregunta, first, max);
            Long total = pruebaClaveAreaPreguntaDistractorDAO.contarPorPadre(idPruebaClave, idArea, idPregunta);

            return Response.ok(registros).header(ResponseHeaders.TOTAL_RECORDS.toString(), total).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea,
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id") UUID idDistractor) {
        if (idPruebaClave == null || idArea == null || idPregunta == null || idDistractor == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos").build();
        }
        try {
            PruebaClaveAreaPreguntaDistractorPK pk = new PruebaClaveAreaPreguntaDistractorPK(idPruebaClave, idArea, idPregunta, idDistractor);
            PruebaClaveAreaPreguntaDistractor encontrado = pruebaClaveAreaPreguntaDistractorDAO.buscarPorId(pk);

            return (encontrado != null)
                    ? Response.ok(encontrado).build()
                    : Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "No encontrado").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea,
            @PathParam("id_pregunta") UUID idPregunta,
            @PathParam("id") UUID idDistractor) {

        if (idPruebaClave == null || idArea == null || idPregunta == null || idDistractor == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos").build();
        }
        try {
            PruebaClaveAreaPreguntaDistractorPK pk = new PruebaClaveAreaPreguntaDistractorPK(idPruebaClave, idArea, idPregunta, idDistractor);
            PruebaClaveAreaPreguntaDistractor existente = pruebaClaveAreaPreguntaDistractorDAO.buscarPorId(pk);

            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "No encontrado").build();
            }
            pruebaClaveAreaPreguntaDistractorDAO.eliminar(existente);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
        }
    }
}