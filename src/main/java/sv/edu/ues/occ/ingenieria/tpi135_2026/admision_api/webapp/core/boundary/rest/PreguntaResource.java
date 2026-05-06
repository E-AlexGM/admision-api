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
public class PreguntaResource implements Serializable{

    @Inject
    PreguntaDAO pDAO;  

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(Pregunta p, @Context UriInfo uriInfo){
        if(p != null){
            try {
                p.setIdPregunta(UUID.randomUUID());
                pDAO.crear(p);
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                builder.path(p.getIdPregunta().toString());
                return Response.status(Response.Status.CREATED).location(builder.build()).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "PREGUNTA").build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") UUID idPregunta){
        if(idPregunta != null){
            try {
                Pregunta p = pDAO.buscarPorId(idPregunta);
                if(p != null){
                    pDAO.eliminar(p);
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "PREGUNTA").build();
            } catch (Exception e) {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "ID").build();
        
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response actualizar(Pregunta p, @PathParam("id") UUID idPregunta){
        if(p != null && idPregunta != null){
            try {
                Pregunta pExistente = pDAO.buscarPorId(idPregunta);
                if(pExistente != null){
                    p.setIdPregunta(idPregunta);
                    pDAO.actualizar(p);
                    return Response.status(Response.Status.OK).entity(p).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "PREGUNTA").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "ID o PREGUNTA").build();
    }

     @GET
     @Path("{id}")
     @Produces({MediaType.APPLICATION_JSON})
     public Response buscarPorId(@PathParam("id") UUID idPregunta){
         if(idPregunta != null){
             try {
                 Pregunta p = pDAO.buscarPorId(idPregunta);
                 if(p != null){
                     return Response.status(Response.Status.OK).entity(p).build();
                 }
                 return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "PREGUNTA").build();
             } catch (Exception e) {
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
             }
         }
         return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "ID").build();
     }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(@QueryParam("first") @DefaultValue("0") int first, @QueryParam("size") @DefaultValue("50") int hasta){
        if(first >= 0 && hasta > 0){
            try {
                List<Pregunta> preguntas = pDAO.buscarPorRango(first, hasta);
                if(preguntas != null && !preguntas.isEmpty()){
                    return Response.status(Response.Status.OK).entity(preguntas).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "PREGUNTAS").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "FIRST o SIZE").build();

    }
}
