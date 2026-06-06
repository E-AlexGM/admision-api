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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.dto.AulaDto;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AulaClientMock;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaAulaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;

@Path("jornada/{id_jornada}/aula")
public class JornadaAulaResource implements Serializable {

    @Inject
    JornadaAulaDAO jADAO;

    @Inject
    JornadaDAO jDAO;

    @Inject
    AulaClientMock aulaClientMock;

    /**
     * Lista las aulas asociadas a una jornada específica
     * 
     * @param idJornada de la jornada especifica
     * @return lista de aulas asociadas a la jornada
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarAulaJornadas(@PathParam("id_jornada") UUID idJornada) {

        if (idJornada == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        List<JornadaAula> aulas = jADAO.listarPorJornada(idJornada);

        List<AulaDto> aulasDtoMock = aulas.stream()
                .map(aula -> aulaClientMock.findById(aula.getIdAula()))
                .toList();

        return Response
                .ok(aulas)
                .entity(aulasDtoMock)
                .build();
    }

    /**
     * ELIMINA UN AULA ASOCIADA A UNA JORNADA ESPECÍFICA
     * 
     * @param idJornada EL ID DE LA JORNADA DE LA QUE SE DESEA ELIMINAR EL AULA
     * @param idAula    EL ID DEL AULA QUE SE DESEA ELIMINAR
     * @return
     */
    @DELETE
    @Path("/{id_aula}")
    public Response eliminar(@PathParam("id_jornada") UUID idJornada, @PathParam("id_aula") String idAula) {

        if (idJornada != null && idAula != null) {
            JornadaAula jA = jADAO.buscarPorJornadaYAula(idJornada, idAula);
            if (jA != null) {
                jADAO.eliminar(jA);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró el aula para la jornada especificada.").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * ASOCIA UN AULA A UNA JORNADA ESPECÍFICA
     * 
     * @param idJornada EL ID DE LA JORNADA A LA QUE SE DESEA ASOCIAR EL AULA
     * @return
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response crear(@PathParam("id_jornada") UUID idJornada, JornadaAula jornadaAula, @Context UriInfo uriInfo) {
        if (idJornada != null && jornadaAula != null && jornadaAula.getIdAula() != null) {
            Jornada j = jDAO.buscarPorId(idJornada);
            if (j != null) {
                jornadaAula.setIdJornada(j);
                if (jornadaAula.getIdJornadaAula() == null) {
                    jornadaAula.setIdJornadaAula(UUID.randomUUID());
                }
                jADAO.crear(jornadaAula);
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                uriBuilder.path(jornadaAula.getIdJornadaAula().toString());
                return Response.created(uriBuilder.build()).build();
            }
            return Response.status(Response.Status.NOT_FOUND).header(ResponseHeaders.NOT_FOUND.toString(), "jornada")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
