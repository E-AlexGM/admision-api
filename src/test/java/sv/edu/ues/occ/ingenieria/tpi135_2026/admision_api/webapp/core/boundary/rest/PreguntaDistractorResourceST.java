package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PreguntaDistractorResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_PREGUNTA = "pregunta";
    private final String RESOURCE_NAME_DISTRACTOR = "distractor";

    Pregunta pregunta = new Pregunta();
    String idPregunta;
    Pregunta pregunta2 = new Pregunta();
    String idPregunta2;
    Distractor distractor = new Distractor();
    String idDistractor;
    Distractor distractor2 = new Distractor();
    String idDistractor2;

    PreguntaDistractor preguntaDistractor = new PreguntaDistractor();
    PreguntaDistractor preguntaDistractor2 = new PreguntaDistractor();
    PreguntaDistractor preguntaDistractor3 = new PreguntaDistractor();


    @Override
    public String getResourceName(){
        return "";
    }

    @BeforeEach
    public void setUp(){
        pregunta.setValor("¿Cuánto es 3 + 1?");
        pregunta.setActivo(true);

        pregunta2.setValor("¿Cuánto es 58 + 3?");
        pregunta2.setActivo(true);

        distractor.setValor("4");
        distractor.setActivo(true);

        distractor2.setValor("5");
        distractor2.setActivo(true);

        preguntaDistractor.setCorrecto(true);
        preguntaDistractor2.setCorrecto(false);
        preguntaDistractor3.setCorrecto(false);
    }

    public void crearContexto(){
        Response respuestaPregunta = target.path(RESOURCE_NAME_PREGUNTA).request().post(Entity.json(pregunta));
        Response respuestaPregunta2 = target.path(RESOURCE_NAME_PREGUNTA).request().post(Entity.json(pregunta2));

        Response respuestaDistractor = target.path(RESOURCE_NAME_DISTRACTOR).request().post(Entity.json(distractor));
        Response respuestaDistractor2 = target.path(RESOURCE_NAME_DISTRACTOR).request().post(Entity.json(distractor2));

        idPregunta = respuestaPregunta.getLocation().toString().split(RESOURCE_NAME_PREGUNTA + "/")[1];
        pregunta = new Pregunta(UUID.fromString(idPregunta));

        idPregunta2 = respuestaPregunta2.getLocation().toString().split(RESOURCE_NAME_PREGUNTA + "/")[1];
        pregunta2 = new Pregunta(UUID.fromString(idPregunta2));

        idDistractor = respuestaDistractor.getLocation().toString().split(RESOURCE_NAME_DISTRACTOR + "/")[1];
        distractor = new Distractor(UUID.fromString(idDistractor));

        idDistractor2 = respuestaDistractor2.getLocation().toString().split(RESOURCE_NAME_DISTRACTOR + "/")[1];
        distractor2 = new Distractor(UUID.fromString(idDistractor2));

    }

    @Order(1)
    @Test
    public void testCrear(){
        crearContexto();
        preguntaDistractor.setIdDistractor(distractor);
        Response respuesta = target.path(RESOURCE_NAME_PREGUNTA).path(idPregunta).path(RESOURCE_NAME_DISTRACTOR)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(preguntaDistractor));

        preguntaDistractor2.setIdDistractor(distractor2);
        Response respuesta2 = target.path(RESOURCE_NAME_PREGUNTA).path(idPregunta).path(RESOURCE_NAME_DISTRACTOR)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(preguntaDistractor2));

        preguntaDistractor3.setIdDistractor(distractor2);
        Response respuesta3 = target.path(RESOURCE_NAME_PREGUNTA).path(idPregunta2).path(RESOURCE_NAME_DISTRACTOR)
            .request(MediaType.APPLICATION_JSON).post(Entity.json(preguntaDistractor3));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta2.getStatus());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta3.getStatus());
        assertNotNull(preguntaDistractor2);
        assertNotNull(preguntaDistractor3);

        preguntaDistractor.setIdPregunta(pregunta);
        preguntaDistractor2.setIdPregunta(pregunta);
        preguntaDistractor3.setIdPregunta(pregunta2);

    }

    @Order(2)
    @Test
    public void testListar(){
        Response respuesta = target.path(RESOURCE_NAME_PREGUNTA).path(idPregunta).path(RESOURCE_NAME_DISTRACTOR)
                .request(MediaType.APPLICATION_JSON).get();
        List<PreguntaDistractor> encontrados = respuesta.readEntity(new GenericType<List<PreguntaDistractor>>() {});

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(2, encontrados.size());
        assertTrue(encontrados.contains(preguntaDistractor));
        assertTrue(encontrados.contains(preguntaDistractor2));
    }

    @Order(3)
    @Test
    public void testEliminar(){
        Response respuesta = target.path(RESOURCE_NAME_PREGUNTA).path(idPregunta)
                .path(RESOURCE_NAME_DISTRACTOR)
                .path(idDistractor)
                .request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }
}
