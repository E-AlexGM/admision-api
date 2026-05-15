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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;

@Path("prueba/{id_prueba}/clave")
public class PruebaClaveResource implements Serializable{
    

    @Inject
    PruebaDAO pDAO;

    @Inject
    PruebaClaveDAO pCDAO;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response asignarClave(@PathParam("id_prueba") UUID idPrueba, PruebaClave pC, @Context UriInfo uriInfo) {
        if(idPrueba != null && pC != null) {
            Prueba p = pDAO.buscarPorId(idPrueba);
            if(p != null){
                pC.setIdPruebaClave(UUID.randomUUID());
                pC.setIdPrueba(p);
                pCDAO.crear(pC);
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                uriBuilder.path(pC.getIdPruebaClave().toString());
                return Response.status(Response.Status.CREATED)
                        .header("Location", uriBuilder.build().toString())
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listarClaves(@PathParam("id_prueba") UUID idPrueba) {
        if(idPrueba != null) {
            Prueba p = pDAO.buscarPorId(idPrueba);
            if(p != null){
                List<PruebaClave> claves = pCDAO.listarClavesPorPrueba(idPrueba);
                if(claves != null && !claves.isEmpty()) {
                    return Response.status(Response.Status.OK).entity(claves).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{id_clave}")
    public Response eliminarClave(@PathParam("id_prueba") UUID idPrueba, @PathParam("id_clave") UUID idPruebaClave) { 
        if(idPruebaClave != null && idPrueba != null) {
            Prueba p = pDAO.buscarPorId(idPrueba);
            if(p != null){
                PruebaClave pC = pCDAO.buscarPorId(idPruebaClave);
                if(pC != null && pC.getIdPrueba().getIdPrueba().equals(idPrueba)) {
                    pCDAO.eliminar(pC);
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"clave").build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"prueba").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idClave o idPrueba").build();
    }


    @GET
    @Path("/{id_clave}")
    public Response buscarPorId(@PathParam("id_prueba") UUID idPrueba, @PathParam("id_clave") UUID idPruebaClave){
        if(idPrueba != null && idPruebaClave != null){
            PruebaClave encontrado = pCDAO.buscarPorId(idPruebaClave);
            if(encontrado != null){
                return Response.status(Response.Status.OK).entity(encontrado).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(),"clave").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPrueba o idClave").build();
    }

}
