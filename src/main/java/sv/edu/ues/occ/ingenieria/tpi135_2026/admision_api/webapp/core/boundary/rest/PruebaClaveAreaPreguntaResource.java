package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaPK;


@Path("prueba_clave/{id_prueba_clave}/area/{id_area}/pregunta")
public class PruebaClaveAreaPreguntaResource implements Serializable{

    @Inject
    PreguntaDAO preguntaDAO;

    @Inject
    AreaDAO areaDAO;

    @Inject
    PruebaClaveAreaDAO pruebaClaveAreaDAO;

    @Inject
    PruebaClaveDAO pruebaClaveDAO;

    @Inject
    PruebaClaveAreaPreguntaDAO pruebaClaveAreaPreguntaDAO;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(@PathParam("id_prueba_clave") UUID idPruebaClave,
                          @PathParam("id_area") UUID idArea,
                          PruebaClaveAreaPregunta pruebaClaveAreaPregunta,
                          @Context UriInfo uriInfo) {

        if(idPruebaClave != null && idArea != null && pruebaClaveAreaPregunta != null) {
            try {
                PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
                PruebaClaveArea pruebaClaveArea  = pruebaClaveAreaDAO.buscarPorId(pk);
                Pregunta pregunta = preguntaDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta());
                if(pruebaClaveArea != null && pregunta != null) {
                    pruebaClaveAreaPregunta.setIdPregunta(pregunta);
                    pruebaClaveAreaPregunta.setIdArea(pruebaClaveArea.getIdArea());
                    pruebaClaveAreaPregunta.setIdPruebaClave(pruebaClaveArea.getIdPruebaClave());
                    boolean isValid = pruebaClaveAreaPreguntaDAO.validarPorcentajePrueba(pruebaClaveArea.getIdPruebaClave(), pruebaClaveAreaPregunta);
                    if(isValid){
                        pruebaClaveAreaPreguntaDAO.crear(pruebaClaveAreaPregunta);
                        if (uriInfo != null) {
                            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                            uriBuilder.path(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta().toString());
                            return Response.created(uriBuilder.build()).build();
                        } else {
                            java.net.URI created = java.net.URI.create(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta().toString());
                            return Response.created(created).build();
                        }
                    }
                    return Response.status(Response.Status.CONFLICT)
                            .header(ResponseHeaders.VIOLATES_BUSINESS_RULES.toString(), "el porcentaje total excede el máximo permitido para esta prueba")
                            .build();

                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"pruebaClave, area o pregunta").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // Backwards-compatible overload used by unit tests (and by callers that don't have UriInfo)
    public Response crear(UUID idPruebaClave, UUID idArea, PruebaClaveAreaPregunta pruebaClaveAreaPregunta) {
        return crear(idPruebaClave, idArea, pruebaClaveAreaPregunta, null);
    }

    @DELETE
    @Path("{id_pregunta}")
    public Response eliminar(@PathParam("id_prueba_clave") UUID idPruebaClave,
                    @PathParam("id_area") UUID idArea, @PathParam("id_pregunta") UUID idPregunta) {

        if(idPruebaClave != null && idArea != null && idPregunta != null) {

            try {
                PruebaClaveAreaPreguntaPK pk = new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta);
                PruebaClaveAreaPregunta pruebaClaveAreaPregunta = pruebaClaveAreaPreguntaDAO.buscarPorId(pk);
                if(pruebaClaveAreaPregunta != null) {
                    pruebaClaveAreaPreguntaDAO.eliminar(pruebaClaveAreaPregunta);
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"pruebaClave, area o pregunta").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    public Response listar(@PathParam("id_prueba_clave") UUID idPruebaClave,
                    @PathParam("id_area") UUID idArea) {

        if(idPruebaClave != null && idArea != null ) {
            try {
                List<PruebaClaveAreaPregunta> pruebaClaveAreaPreguntas = pruebaClaveAreaPreguntaDAO.buscarPorClaveYArea(idPruebaClave, idArea);
               if(pruebaClaveAreaPreguntas != null && !pruebaClaveAreaPreguntas.isEmpty()){
                    return Response.status(Response.Status.OK).entity(pruebaClaveAreaPreguntas).build();
               }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"pruebaClave, area ").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("{id_pregunta}")
    public Response buscarPorId(@PathParam("id_prueba_clave") UUID idPruebaClave,
                                @PathParam("id_area") UUID idArea,
                                @PathParam("id_pregunta") UUID idPregunta) {

        if(idPruebaClave != null && idArea != null && idPregunta != null) {
            try {
                PruebaClaveAreaPreguntaPK pk = new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta);
                PruebaClaveAreaPregunta pruebaClaveAreaPregunta = pruebaClaveAreaPreguntaDAO.buscarPorId(pk);
                if(pruebaClaveAreaPregunta != null) {
                    return Response.status(Response.Status.OK).entity(pruebaClaveAreaPregunta).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"pruebaClave, area o pregunta").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id_prueba_clave") UUID idPruebaClave,
                           @PathParam("id_area") UUID idArea,
                           PruebaClaveAreaPregunta pruebaClaveAreaPregunta){

        if(idPruebaClave != null && idArea != null && pruebaClaveAreaPregunta != null) {
            try {
                PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
                PruebaClaveArea pruebaClaveArea  = pruebaClaveAreaDAO.buscarPorId(pk);
                Pregunta pregunta = preguntaDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta());
                if(pruebaClaveArea != null && pregunta != null) {
                    pruebaClaveAreaPregunta.setIdPregunta(pregunta);
                    pruebaClaveAreaPregunta.setIdArea(pruebaClaveArea.getIdArea());
                    pruebaClaveAreaPregunta.setIdPruebaClave(pruebaClaveArea.getIdPruebaClave());
                    boolean isValid = pruebaClaveAreaPreguntaDAO.validarPorcentajePrueba(pruebaClaveArea.getIdPruebaClave(), pruebaClaveAreaPregunta);
                    if(isValid){
                        pruebaClaveAreaPreguntaDAO.actualizar(pruebaClaveAreaPregunta);
                        return Response.status(Response.Status.OK).build();
                    }
                    return Response.status(Response.Status.CONFLICT)
                            .header(ResponseHeaders.VIOLATES_BUSINESS_RULES.toString(), "el porcentaje total excede el máximo permitido para esta prueba")
                            .build();

                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"pruebaClave, area o pregunta").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}