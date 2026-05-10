package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractorPK;

@Path("pregunta/{id_pregunta}/distractor")
public class PreguntaDistractorResource implements Serializable {

    @Inject
    PreguntaDAO pDAO;

    @Inject
    DistractorDAO dDAO;

    @Inject
    PreguntaDistractorDAO pDDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crear(@PathParam("id_pregunta") UUID idPregunta, PreguntaDistractor pD, @Context UriInfo uriInfo){
        if(idPregunta != null && pD != null && pD.getIdDistractor() != null && pD.getIdDistractor().getIdDistractor() != null){
            try {
                Pregunta p = pDAO.buscarPorId(idPregunta);
                Distractor d = dDAO.buscarPorId(pD.getIdDistractor().getIdDistractor());

                if(p != null && d != null ){
                    pD.setIdPregunta(p);
                    pD.setIdDistractor(d);
                    pDDAO.crear(pD);

                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    uriBuilder.path(pD.getIdDistractor().getIdDistractor().toString());
                    return Response.created(uriBuilder.build()).build();
                }

                return Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "pregunta o distractor o preguntaDistractor")
                        .build();

            } catch (Exception e) {
                // 1. Escarbamos hasta encontrar la causa raíz (Root Cause) de la excepción
                Throwable rootCause = e;
                while (rootCause.getCause() != null && rootCause != rootCause.getCause()) {
                    rootCause = rootCause.getCause();
                }

                // 2. Verificamos si el mensaje original contiene la frase clave de nuestro Trigger
                if (rootCause.getMessage() != null && rootCause.getMessage().contains("Violación de negocio")) {

                    // Limpiamos un poco el mensaje de PostgreSQL (opcional, para quitar códigos técnicos)
                    String mensajeUsuario = rootCause.getMessage().split("\n")[0];

                    // Retornamos un 400 Bad Request o 409 Conflict, avisando que es error de regla de negocio
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"" + mensajeUsuario + "\"}") // Enviamos el JSON con el error
                            .build();
                }

                // 3. Si es cualquier otro error del servidor (Base de datos caída, error de sintaxis, etc.)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage())
                        .build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id_distractor}")
    public Response eliminar(@PathParam("id_pregunta") UUID idPregunta, @PathParam("id_distractor") UUID idDistractor){
        if(idPregunta != null && idDistractor != null){
            try {
                    PreguntaDistractor eliminado = pDDAO.buscarPorId(new PreguntaDistractorPK(idPregunta, idDistractor));
                    if(eliminado != null){
                        pDDAO.eliminar(eliminado);
                        return Response.status(Response.Status.NO_CONTENT).build();
                    }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "pregunta o distractor").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(@PathParam("id_pregunta") UUID idPregunta){
        if(idPregunta != null){
            try {
                Pregunta p = pDAO.buscarPorId(idPregunta);
                if(p != null){
                    List<PreguntaDistractor> preguntaDistractores = pDDAO.buscarPorIdPregunta(idPregunta);
                    if(preguntaDistractores != null && !preguntaDistractores.isEmpty() ){
                        return Response.status(Response.Status.OK).entity(preguntaDistractores).build();
                    }
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "pregunta o distractores").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}

