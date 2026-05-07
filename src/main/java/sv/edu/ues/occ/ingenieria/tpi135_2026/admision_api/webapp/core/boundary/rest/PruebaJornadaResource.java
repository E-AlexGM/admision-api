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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaPK;

@Path("prueba/{id_prueba}/jornada")
public class PruebaJornadaResource implements Serializable{


    @Inject
    PruebaDAO pDAO;

    @Inject
    JornadaDAO jDAO;

    @Inject
    PruebaJornadaDAO pJDAO;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listarJornadas(@PathParam("id_prueba") UUID idPrueba, 
                                @QueryParam("first") @DefaultValue("0") int first,
                                @QueryParam("max") @DefaultValue("10") int max) {

        if(idPrueba != null && first >= 0 && max > 0){
            try {
                List<Jornada> jornadas = jDAO.listarPorIdPrueba(idPrueba, first, max);
                
                if(jornadas != null){
                    return Response.status(Response.Status.OK).entity(jornadas).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "jornadas").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPrueba o first o max").build(); 
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(@PathParam("id_prueba") UUID idPrueba, PruebaJornada pruebaJornada, @Context UriInfo uriInfo){

        if(idPrueba != null && pruebaJornada != null && pruebaJornada.getIdJornada() != null && pruebaJornada.getIdJornada().getIdJornada() != null){
            try {
                Prueba p = pDAO.buscarPorId(idPrueba);
                Jornada j = jDAO.buscarPorId(pruebaJornada.getIdJornada().getIdJornada());
                if(p != null && j != null){
                    pruebaJornada.setIdPrueba(p);
                    pruebaJornada.setIdJornada(j);
                    pJDAO.crear(pruebaJornada);
                    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                    uriBuilder.path(pruebaJornada.getIdJornada().getIdJornada().toString());
                    return Response.created(uriBuilder.build()).build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @DELETE
    @Path("/{id_jornada}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response eliminar(@PathParam("id_prueba") UUID idPrueba, @PathParam("id_jornada") UUID idJornada){
        if(idPrueba != null && idJornada != null){
            try {
                Prueba p = pDAO.buscarPorId(idPrueba);
                Jornada j = jDAO.buscarPorId(idJornada);
                PruebaJornada pj = new PruebaJornada(p, j);
                PruebaJornadaPK pk = new PruebaJornadaPK(idPrueba, idJornada);
                PruebaJornada pJExistente = pJDAO.buscarPorId(pk);
                if(p != null && j != null && pJExistente != null){
                    pJDAO.eliminar(pj);
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "prueba o jornada").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(ResponseHeaders.PROCESS_ERROR.toString(), e.getMessage()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPrueba o idJornada").build();
    }

   
}
