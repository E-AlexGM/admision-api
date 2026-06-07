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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;



@Path("prueba")
public class PruebaResource implements Serializable {

    @Inject
    PruebaDAO pDAO;


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crear(Prueba p, @Context UriInfo uriInfo) {
        if(p != null ) {
            p.setIdPrueba(UUID.randomUUID());
            pDAO.crear(p);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(p.getIdPrueba().toString());
            return Response.created(uriBuilder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "prueba").build();
    }

    @DELETE
    @Path("{id_prueba}")
    public Response eliminar(@PathParam("id_prueba") UUID idPrueba) {
        if(idPrueba != null) {
            Prueba p = pDAO.buscarPorId(idPrueba);
            if(p != null) {
                p.setIdPrueba(idPrueba);
                pDAO.eliminar(p);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPrueba").build();
    }

    @PUT
    @Path("{id_prueba}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response actualizar(@PathParam("id_prueba") UUID idPrueba, Prueba p) {
        if(p != null) {
            Prueba pruebaExistente = pDAO.buscarPorId(idPrueba);
            p.setIdPrueba(idPrueba);
            if(pruebaExistente != null) {
                // Actualizar los campos de la prueba existente con los valores del objeto p
                pDAO.actualizar(p);
                return Response.status(Response.Status.OK).entity(p).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "id").build();
    }   

    @GET
    @Path("{id_prueba}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorId(@PathParam("id_prueba") UUID idPrueba) {
        if(idPrueba != null) {
            Prueba p = pDAO.buscarPorId(idPrueba);
            if(p != null) {
                return Response.status(Response.Status.OK).entity(p).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "id").build();
    }

        
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listar(@QueryParam("activo") Boolean activo){
        
        List<Prueba> pruebas = pDAO.findActive(activo);

        if(pruebas.isEmpty() || pruebas == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } 

        return Response
            .ok()
            .entity(pruebas)
            .build();
    }
    
    
    @GET 
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(@QueryParam("first") @DefaultValue("0") int first, 
                        @QueryParam("max") @DefaultValue("50") int max, 
                        @QueryParam("activo") Boolean activo) {

        if(first >= 0 && max > 0) {
            List<Prueba> pruebas;
            
            if(activo != null) {
                pruebas = pDAO.findActive(activo);
            } else {
                pruebas = pDAO.buscarPorRango(first, max);
            }
            
            if(pruebas != null && !pruebas.isEmpty()) {
                return Response.status(Response.Status.OK).entity(pruebas).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "first o max").build();
    }
    



} 
