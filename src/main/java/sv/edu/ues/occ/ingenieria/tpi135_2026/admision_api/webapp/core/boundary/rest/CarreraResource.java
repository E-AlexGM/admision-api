package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.io.Serializable;
import java.util.List;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("carrera")
public class CarreraResource implements Serializable {

    public record CarreraResumen(String codigo, String nombre) {}

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response buscarPorRango(
            @QueryParam("first") @DefaultValue("0") int firstResult,
            @QueryParam("max") @DefaultValue("50") int maxResults) {

        if (firstResult >= 0 && maxResults > 0 && maxResults <= 50) {

            List<CarreraResumen> carrerasQuemadas = List.of(
                    new CarreraResumen("I30515", "Ingeniería de Sistemas"),
                    new CarreraResumen("I30501", "Ingeniería Industrial"),
                    new CarreraResumen("I10501", "Ingeniería Civil"),
                    new CarreraResumen("A10507", "Arquitectura")
            );
            int total = carrerasQuemadas.size();

            return Response.ok(carrerasQuemadas)
                    .header("Total-Records", total)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).header(ResponseHeaders.WRONG_PARAMETER.toString(), "Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50").build();
    }
}