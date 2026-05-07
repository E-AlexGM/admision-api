package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JornadaAulaResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME = "jornada";

    Jornada jornada = new Jornada();

    UUID idAula = UUID.randomUUID();
    String idJornada;

    @Override
    public String getResourceName(){
        return RESOURCE_NAME;
    }

    @BeforeEach
    public void setup(){
        jornada.setNombre("MATUTINA_INGRESO_UNIVERSITARIO");
        jornada.setFechaInicio(OffsetDateTime.now());
        jornada.setFechaFin(OffsetDateTime.now().plusDays(1));
    }

    @Order(1)
    @Test
    public void testCrear(){
        // El path de la solicitud POST es /jornada/{id_jornada}/aula con idAula en el body.

        Response respuestaContexto = target.request(MediaType.APPLICATION_JSON)
            .post(Entity.json(jornada));
        idJornada = respuestaContexto.getHeaderString("Location").split(RESOURCE_NAME + "/")[1];
        JornadaAula ja = new JornadaAula();
        ja.setIdAula(idAula.toString());
        Response response = target.path(idJornada).path("aula")
            .request(MediaType.APPLICATION_JSON).post(Entity.json(ja));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaContexto.getStatus());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
    }

    @Order(2)
    @Test
    public void testListarAulaPorJornada(){

        Response response = target.path(idJornada).path("aula")
            .request().get();
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Order(3)
    @Test
    public void testEliminarAulaDeJornada(){

        Response response = target.path(idJornada).path("aula").path(idAula.toString())
            .request().delete();
        assertNotNull(response);
        assert(response.getStatus() == Response.Status.NO_CONTENT.getStatusCode());}
}   
