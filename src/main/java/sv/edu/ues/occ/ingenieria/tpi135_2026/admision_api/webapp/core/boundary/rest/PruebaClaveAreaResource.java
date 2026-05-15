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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPK;

@Path("prueba_clave/{id_prueba_clave}/area")
public class PruebaClaveAreaResource implements Serializable {

    @Inject
    PruebaClaveAreaDAO pruebaClaveAreaDAO;

    @Inject
    PruebaClaveDAO pruebaClaveDAO;

    @Inject
    AreaDAO areaDAO;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crear(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            PruebaClaveArea pruebaClaveArea,
            @Context UriInfo uriInfo) {
        if (pruebaClaveArea != null && idPruebaClave != null && pruebaClaveArea.getIdArea() != null
                && pruebaClaveArea.getIdArea().getIdArea() != null) {
            PruebaClave pruebaClave = pruebaClaveDAO.buscarPorId(idPruebaClave);
            Area area = areaDAO.buscarPorId(pruebaClaveArea.getIdArea().getIdArea());
            if (pruebaClave == null || area == null) {
                String mensaje = pruebaClave == null ? "PruebaClave no encontrada" : "Area no encontrada";
                return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), mensaje).build();
            }
            pruebaClaveArea.setIdPruebaClave(pruebaClave);
            pruebaClaveArea.setIdArea(area);
            pruebaClaveAreaDAO.crear(pruebaClaveArea);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            UUID idArea = pruebaClaveArea.getIdArea().getIdArea();
            if (idArea != null) {
                uriBuilder.path(idArea.toString());
            }
            return Response.created(uriBuilder.build()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y idPruebaClave no puede ser nulo").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @QueryParam("first") @DefaultValue("0") int firstResult,
            @QueryParam("max") @DefaultValue("50") int maxResults) {
        if (idPruebaClave != null && firstResult >= 0 && maxResults > 0 && maxResults <= 50) {
            List<PruebaClaveArea> registros = pruebaClaveAreaDAO.buscarPorPruebaClaveRango(idPruebaClave, firstResult, maxResults);
            Long total = pruebaClaveAreaDAO.contarPorPruebaClave(idPruebaClave);
            return Response.ok(registros)
                    .header(ResponseHeaders.TOTAL_RECORDS.toString(), total)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50").build();
    }

    @GET
    @Path("{id_area}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea) {
        if (idArea != null && idPruebaClave != null) {
            PruebaClaveArea encontrado = pruebaClaveAreaDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
            if (encontrado != null) {
                return Response.ok(encontrado).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPruebaClave e id no pueden ser nulos").build();
    }

    @PUT
    @Path("{id_area}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea,
            PruebaClaveArea pruebaClaveArea) {
        if (pruebaClaveArea != null && idPruebaClave != null && idArea != null && pruebaClaveArea.getIdArea() != null
                && pruebaClaveArea.getIdArea().getIdArea() != null) {
            if (!idArea.equals(pruebaClaveArea.getIdArea().getIdArea())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .header(ResponseHeaders.WRONG_PARAMETER.toString(), "El idArea del body debe coincidir con el id_area del path")
                        .build();
            }
            PruebaClaveArea existente = pruebaClaveAreaDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
            if (existente != null) {
                Area area = areaDAO.buscarPorId(pruebaClaveArea.getIdArea().getIdArea());
                if (area == null) {
                            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Area no encontrada").build();
                }
                pruebaClaveArea.setIdArea(area);
                pruebaClaveArea.setIdPruebaClave(new PruebaClave(idPruebaClave));
                pruebaClaveAreaDAO.actualizar(pruebaClaveArea);
                return Response.ok(pruebaClaveArea).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El recurso no puede ser nulo y idPruebaClave no puede ser nulo").build();
    }

    @DELETE
    @Path("{id_area}")
    public Response eliminar(
            @PathParam("id_prueba_clave") UUID idPruebaClave,
            @PathParam("id_area") UUID idArea) {
        if (idArea != null && idPruebaClave != null) {
            PruebaClaveArea pruebaClaveArea = pruebaClaveAreaDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
            if (pruebaClaveArea != null) {
                pruebaClaveAreaDAO.eliminar(pruebaClaveArea);
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "idPruebaClave e id no pueden ser nulos").build();
    }
}
