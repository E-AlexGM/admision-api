package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionExamenDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamenPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionPK;

@Path("prueba/{id_prueba}/jornada/{id_jornada}/aula/{id_aula}/aspirante_opcion/{id_aspirante_opcion}/examen")
public class PruebaJornadaAulaAspiranteOpcionExamenResource implements Serializable {

    @Inject
    PruebaJornadaAulaAspiranteOpcionExamenDAO pruebaJornadaAulaAspiranteOpcionExamenDAO;

    @Inject
    PruebaJornadaAulaAspiranteOpcionDAO pruebaJornadaAulaAspiranteOpcionDAO;

    @Inject
    PruebaClaveDAO pruebaClaveDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id_aspirante_opcion") UUID idAspiranteOpcion,
            PruebaJornadaAulaAspiranteOpcionExamen entity,
            @Context UriInfo uriInfo) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null
                || entity == null || entity.getResultado() == null
                || entity.getIdPruebaClave() == null || entity.getIdPruebaClave().getIdPruebaClave() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(), "ID Padre, cuerpo o resultado faltantes")
                    .build();
        }

        PruebaJornadaAulaAspiranteOpcion padre = pruebaJornadaAulaAspiranteOpcionDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        PruebaClave pruebaClave = pruebaClaveDAO.buscarPorId(entity.getIdPruebaClave().getIdPruebaClave());
        if (padre == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Padre no encontrado").build();
        }
        if (pruebaJornadaAulaAspiranteOpcionExamenDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionExamenPK(idPrueba, idJornada, idAula, idAspiranteOpcion)) != null) {
            return Response.status(Response.Status.CONFLICT).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Ya existe examen para este aspirante").build();
        }
        if (pruebaClave == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Prueba clave no encontrada").build();
        }

        entity.setIdPrueba(padre.getIdPrueba());
        entity.setIdJornada(padre.getIdJornada());
        entity.setIdAula(idAula);
        entity.setIdAspiranteOpcion(padre.getIdAspiranteOpcion());
        entity.setIdPruebaClave(pruebaClave);
        pruebaJornadaAulaAspiranteOpcionExamenDAO.crear(entity);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(entity.getIdPruebaClave().getIdPruebaClave().toString());
        return Response.created(uriBuilder.build())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorPadre(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id_aspirante_opcion") UUID idAspiranteOpcion) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos").build();
        }

        PruebaJornadaAulaAspiranteOpcionExamen encontrado = pruebaJornadaAulaAspiranteOpcionExamenDAO
            .buscarPorId(new PruebaJornadaAulaAspiranteOpcionExamenPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        return encontrado != null
            ? Response.ok(encontrado).build()
            : Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "No encontrado").build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id_aspirante_opcion") UUID idAspiranteOpcion,
            PruebaJornadaAulaAspiranteOpcionExamen entity) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null || entity == null
                || entity.getIdPruebaClave() == null || entity.getIdPruebaClave().getIdPruebaClave() == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Datos insuficientes para actualizar").build();
        }

        PruebaJornadaAulaAspiranteOpcionExamen existente = pruebaJornadaAulaAspiranteOpcionExamenDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionExamenPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "No encontrado").build();
        }

        PruebaClave pruebaClave = pruebaClaveDAO.buscarPorId(entity.getIdPruebaClave().getIdPruebaClave());
        if (pruebaClave == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "Prueba clave no encontrada").build();
        }
        entity.setIdPruebaClave(pruebaClave);

        entity.setIdPrueba(existente.getIdPrueba());
        entity.setIdJornada(existente.getIdJornada());
        entity.setIdAula(idAula);
        entity.setIdAspiranteOpcion(existente.getIdAspiranteOpcion());
        pruebaJornadaAulaAspiranteOpcionExamenDAO.actualizar(entity);
        return Response.ok(entity).build();
    }

    @DELETE
    public Response eliminar(
            @PathParam("id_prueba") UUID idPrueba,
            @PathParam("id_jornada") UUID idJornada,
            @PathParam("id_aula") String idAula,
            @PathParam("id_aspirante_opcion") UUID idAspiranteOpcion) {

        if (idPrueba == null || idJornada == null || idAula == null || idAspiranteOpcion == null) {
            return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "IDs requeridos").build();
        }

        PruebaJornadaAulaAspiranteOpcionExamen existente = pruebaJornadaAulaAspiranteOpcionExamenDAO
                .buscarPorId(new PruebaJornadaAulaAspiranteOpcionExamenPK(idPrueba, idJornada, idAula, idAspiranteOpcion));
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "No encontrado").build();
        }
        pruebaJornadaAulaAspiranteOpcionExamenDAO.eliminar(existente);
        return Response.noContent().build();
    }

}