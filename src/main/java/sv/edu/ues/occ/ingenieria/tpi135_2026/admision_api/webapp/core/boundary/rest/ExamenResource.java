package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.dto.CardExamenDto;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenDefaultStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioPrimeraRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioSegundaRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionExamenDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionPK;

@Path("examen")
public class ExamenResource implements Serializable{

    @Inject
    PruebaJornadaAulaAspiranteOpcionDAO pruebaJornadaAulaAspiranteOpcionDAO;

    @Inject
    PruebaJornadaAulaAspiranteOpcionExamenDAO pruebaJornadaAulaAspiranteOpcionExamenDAO;

    @Inject
    IngresoUniversitarioPrimeraRondaStrategy ingresoUniversitarioPrimeraRondaStrategy;

    @Inject
    IngresoUniversitarioSegundaRondaStrategy ingresoUniversitarioSegundaRondaStrategy;

    @Inject
    ExamenDefaultStrategy examenDefaultStrategy;

    Map<String, ExamenStrategy> estrategiasEstado;

    @PostConstruct
    public void inicializar() {
        estrategiasEstado = new HashMap<>();
        estrategiasEstado.put(TipoPruebaEnum.INGRESO_UNIVERSITARIO_PRIMERA_RONDA.name(), ingresoUniversitarioPrimeraRondaStrategy);
        estrategiasEstado.put(TipoPruebaEnum.INGRESO_UNIVERSITARIO_SEGUNDA_RONDA.name(), ingresoUniversitarioSegundaRondaStrategy);
    }

    /**
     * 
     * @param correo
     * @return Una Response con una lista de examenes asociaciados al correo del aspirante
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarResultadosExamenPorCorreo(@QueryParam("correo") String correo){

        List<CardExamenDto> cardExamenDtos = new ArrayList<>();

        if(correo == null || correo.isBlank()){
            return Response.status(400).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Correo faltante").build();
        }

       List<PruebaJornadaAulaAspiranteOpcionExamen> examenes = pruebaJornadaAulaAspiranteOpcionExamenDAO.obtenerResultadoExamenPorCorreoAspirante(correo);

        if (examenes == null || examenes.isEmpty()) {

            return Response.status(404)
                .header(ResponseHeaders.NOT_FOUND.toString(), "Resultados no encontrados para el aspirante y prueba especificados")
                .build();
        }
    
        

        for(PruebaJornadaAulaAspiranteOpcionExamen examen : examenes){
            if(examen.getIdPrueba() != null){
                
                Prueba prueba = examen.getIdPrueba();
                PruebaJornadaAulaAspiranteOpcionPK f1Pk = new PruebaJornadaAulaAspiranteOpcionPK(
                    prueba.getIdPrueba(), examen.getIdJornada().getIdJornada(), examen.getIdAula(), examen.getIdAspiranteOpcion().getIdAspiranteOpcion());
                String tipoPruebaValor = prueba.getIdTipoPrueba().getValor();
                ExamenStrategy estrategiaAplicar = estrategiasEstado.getOrDefault(tipoPruebaValor, examenDefaultStrategy);
                ExamenResultadosEnum estadoExamen = estrategiaAplicar.obtenerEstado(prueba.getNotaAprobacion(), examen.getResultado());
                cardExamenDtos.add(new CardExamenDto(prueba.getNombre(), pruebaJornadaAulaAspiranteOpcionDAO.buscarPorId(f1Pk).getFecha(), estadoExamen));
            }
        }

        return Response.ok(cardExamenDtos).build();
    }
}
