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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaAulaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaPK;

@Path("prueba/{id_prueba}/jornada/{id_jornada}/aula/{id_aula}/aspirante_opcion")
public class PruebaJornadaAulaAspiranteOpcionResource implements Serializable {

    @Inject PruebaJornadaAulaAspiranteOpcionDAO pruebaJornadaAulaAspiranteOpcionDAO;
    @Inject PruebaJornadaDAO pruebaJornadaDAO;
    @Inject JornadaAulaDAO jornadaAulaDAO;
    @Inject AspiranteOpcionDAO aspiranteOpcionDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            PruebaJornadaAulaAspiranteOpcion entity,
            @Context UriInfo uriInfo) {

        if (idPrueba == null || idJornada == null || idAula == null || entity == null
                || entity.getIdAspiranteOpcion() == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs de ruta o AspiranteOpcion faltantes").build();
        }

        PruebaJornada prueba = pruebaJornadaDAO.buscarPorId(new PruebaJornadaPK(idPrueba, idJornada));
        JornadaAula aula = jornadaAulaDAO.buscarPorJornadaYAula(idJornada, idAula);
        AspiranteOpcion aspirante = aspiranteOpcionDAO.buscarPorId(entity.getIdAspiranteOpcion().getIdAspiranteOpcion());

        if (prueba == null || aula == null || aspirante == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Dependencia no encontrada").build();
        }

        // Validación de pertenencia a la misma jornada
        if (aula.getIdJornada() == null || prueba.getIdJornada() == null || 
            !aula.getIdJornada().getIdJornada().equals(prueba.getIdJornada().getIdJornada())) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Conflicto de Jornada").build();
        }

        entity.setIdPrueba(prueba.getIdPrueba());
        entity.setIdJornada(prueba.getIdJornada());
        entity.setIdAula(idAula);
        entity.setIdAspiranteOpcion(aspirante);
        
        pruebaJornadaAulaAspiranteOpcionDAO.crear(entity);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(aspirante.getIdAspiranteOpcion().toString());
        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorRango(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        if (idPrueba == null || idJornada == null || idAula == null || first < 0 || max <= 0 || max > 50) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Parámetros inválidos").build();
        }

        List<PruebaJornadaAulaAspiranteOpcion> registros = pruebaJornadaAulaAspiranteOpcionDAO.buscarPorPruebaJornadaYJornadaAulaRango(idPrueba, idJornada, idAula, first, max);
        Long total = pruebaJornadaAulaAspiranteOpcionDAO.contarPorPruebaJornadaYJornadaAula(idPrueba, idJornada, idAula);
        return Response.ok(registros).header(ResponseHeaders.TOTAL_RECORDS.toString(), total).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id") UUID idAspiranteOpcion) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos").build();
        }

        PruebaJornadaAulaAspiranteOpcion encontrado = pruebaJornadaAulaAspiranteOpcionDAO
            .buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        return (encontrado != null)
                ? Response.ok(encontrado).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .header(ResponseHeaders.NOT_FOUND.toString(), "Recurso no encontrado")
                        .build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id") UUID idAspiranteOpcion,
            PruebaJornadaAulaAspiranteOpcion entity) {

        if (idPrueba == null || idJornada == null || idAula == null || entity == null || idAspiranteOpcion == null
                || entity.getIdAspiranteOpcion() == null || entity.getIdAspiranteOpcion().getIdAspiranteOpcion() == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Datos incompletos").build();
        }

        if (!idAspiranteOpcion.equals(entity.getIdAspiranteOpcion().getIdAspiranteOpcion())) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "El idAspiranteOpcion del body debe coincidir con el id del path").build();
        }
        PruebaJornadaAulaAspiranteOpcion existente = pruebaJornadaAulaAspiranteOpcionDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        AspiranteOpcion aspirante = aspiranteOpcionDAO.buscarPorId(entity.getIdAspiranteOpcion().getIdAspiranteOpcion());
        if (aspirante == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "AspiranteOpcion no encontrada").build();
        }
        entity.setIdAspiranteOpcion(aspirante);

        entity.setIdPrueba(existente.getIdPrueba());
        entity.setIdJornada(existente.getIdJornada());
        entity.setIdAula(idAula);
        
        pruebaJornadaAulaAspiranteOpcionDAO.actualizar(entity);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminar(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id") UUID idAspiranteOpcion) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs faltantes").build();
        }

        PruebaJornadaAulaAspiranteOpcion existente = pruebaJornadaAulaAspiranteOpcionDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        pruebaJornadaAulaAspiranteOpcionDAO.eliminar(existente);
        return Response.noContent().build();
    }

}