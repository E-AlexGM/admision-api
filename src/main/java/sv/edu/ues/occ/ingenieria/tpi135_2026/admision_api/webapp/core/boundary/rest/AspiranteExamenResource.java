package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenDefaultStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioPrimeraRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioSegundaRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionExamenDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;

@Path("aspirante/{id_aspirante}/prueba")
public class AspiranteExamenResource implements Serializable {

    @Inject
    PruebaJornadaAulaAspiranteOpcionExamenDAO pruebaJornadaAulaAspiranteOpcionExamenDAO;

    @Inject
    IngresoUniversitarioPrimeraRondaStrategy ingresoUniversitarioPrimeraRondaStrategy;

    @Inject
    IngresoUniversitarioSegundaRondaStrategy ingresoUniversitarioSegundaRondaStrategy;

    @Inject
    ExamenDefaultStrategy examenDefaultStrategy;

    @Inject
    PruebaDAO pruebaDAO;

    Map<String, ExamenStrategy> estrategiasEstado;

    @PostConstruct
    public void inicializar() {
        estrategiasEstado = new HashMap<>();
        estrategiasEstado.put(TipoPruebaEnum.INGRESO_UNIVERSITARIO_PRIMERA_RONDA.name(), ingresoUniversitarioPrimeraRondaStrategy);
        estrategiasEstado.put(TipoPruebaEnum.INGRESO_UNIVERSITARIO_SEGUNDA_RONDA.name(), ingresoUniversitarioSegundaRondaStrategy);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id_prueba}")
    public Response buscarResultadoExamen(@PathParam("id_aspirante") UUID idAspirante, @PathParam("id_prueba") UUID idPrueba) {
        if (idAspirante == null || idPrueba == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los IDs de aspirante y prueba no pueden ser nulos")
                    .build();
        }
        PruebaJornadaAulaAspiranteOpcionExamen examen = pruebaJornadaAulaAspiranteOpcionExamenDAO
            .obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba);
        if (examen == null || examen.getIdPrueba() == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .header(ResponseHeaders.NOT_FOUND.toString(), "Resultado no encontrado para el aspirante y prueba especificados")
                .build();
        }
        Prueba prueba = examen.getIdPrueba();
        String tipoPruebaValor = prueba.getIdTipoPrueba().getValor();
        ExamenStrategy estrategiaAplicar = estrategiasEstado.getOrDefault(tipoPruebaValor, examenDefaultStrategy);
        ExamenResultadosEnum estadoExamen = estrategiaAplicar.obtenerEstado(prueba.getNotaAprobacion(), examen.getResultado());
        return Response.ok(estadoExamen).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id_prueba}/examen")
    public Response actualizarResultado(
            @PathParam("id_aspirante") UUID idAspirante,
            @PathParam("id_prueba") UUID idPrueba,
            PruebaJornadaAulaAspiranteOpcionExamen entity) {

        if (idAspirante == null || idPrueba == null || entity == null || entity.getResultado() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los IDs de aspirante y prueba, y resultado son requeridos")
                    .build();
        }

        PruebaJornadaAulaAspiranteOpcionExamen examen = pruebaJornadaAulaAspiranteOpcionExamenDAO
            .obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba);

        if (examen == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .header(ResponseHeaders.NOT_FOUND.toString(), "Examen no encontrado para el aspirante y prueba especificados")
                .build();
        }

        examen.setResultado(entity.getResultado());
        examen.setFechaResultado(OffsetDateTime.now());
        pruebaJornadaAulaAspiranteOpcionExamenDAO.actualizar(examen);
        return Response.ok(examen).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listarPruebasPorAspirante(@PathParam("id_aspirante") UUID idAspirante){
        if(idAspirante == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .header(ResponseHeaders.WRONG_PARAMETER.toString(), "El ID de aspirante no puede ser nulo")
                    .build();
        }
        List<Prueba> pruebas = pruebaDAO.findByIdAspirante(idAspirante);
        Response.ResponseBuilder responseBuilder = Response.ok(pruebas)
            .type(MediaType.APPLICATION_JSON);
        return responseBuilder.build();

    }


}
