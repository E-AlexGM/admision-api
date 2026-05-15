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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;


@Path("tipo_prueba")
public class TipoPruebaResource implements Serializable{

    @Inject
    TipoPruebaDAO tpDAO;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(TipoPrueba tp, @Context UriInfo uriInfo){
        if(tp != null){
            tp.setIdTipoPrueba(UUID.randomUUID());
            tpDAO.crear(tp);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(tp.getIdTipoPrueba().toString());
            return Response.created(uriBuilder.build()).build();
            
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(@QueryParam("first") @DefaultValue("0") int first, @QueryParam("max") @DefaultValue("10") int max){
        if(first < 0 || max <= 0){
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los parámetros 'first' y 'max' deben ser números enteros positivos").build();
        }
        List<TipoPrueba> tipoPruebas = tpDAO.buscarPorRango(first, max);
        if(tipoPruebas == null || tipoPruebas.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "TIPO_PRUEBA").build();
        }
        return Response.ok(tipoPruebas).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response actualizar(TipoPrueba tp, @PathParam("id") UUID idTipoPrueba){
        if(tp != null && idTipoPrueba != null){
            TipoPrueba tpExistente = tpDAO.buscarPorId(idTipoPrueba);
            if(tpExistente != null){
                tp.setIdTipoPrueba(idTipoPrueba);
                tpDAO.actualizar(tp);
                return Response.ok(tp).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "TIPO_PRUEBA").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
        
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam ("id") UUID idTipoPrueba){
        if(idTipoPrueba != null){
            TipoPrueba tpExistente = tpDAO.buscarPorId(idTipoPrueba);
            if(tpExistente != null){
                tpDAO.eliminar(tpExistente);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "TIPO_PRUEBA").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }    

}
