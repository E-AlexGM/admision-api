package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PreguntaResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_PREGUNTA = "pregunta";

    Pregunta pregunta = new Pregunta();
    Pregunta pregunta2 = new Pregunta();

    String idPregunta;
    String idPregunta2;

    @Override
    public String getResourceName(){
        return RESOURCE_NAME_PREGUNTA;
    }

    @BeforeEach
    public void setUp(){
        pregunta.setValor("¿Cuánto es 2 + 2?");
        pregunta.setActivo(true);

        pregunta2.setValor("¿Cuánto es 3 + 3?");
        pregunta2.setActivo(true);
    }

    @Order(1)
    @Test
    public void testCrear(){
        Response respuesta = target.request(MediaType.APPLICATION_JSON).post(Entity.json(pregunta));
        assertNotNull(respuesta.getLocation());
        idPregunta = respuesta.getLocation().toString().split(RESOURCE_NAME_PREGUNTA + "/")[1];
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());

        Response respuesta2 = target.request(MediaType.APPLICATION_JSON).post(Entity.json(pregunta2));
        assertNotNull(respuesta2.getLocation());
        idPregunta2 = respuesta2.getLocation().toString().split(RESOURCE_NAME_PREGUNTA + "/")[1];
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta2.getStatus());
    }

    @Order(2)
    @Test
    public void testBuscarPorId(){
        Response respuesta = target.path(idPregunta)
                .request().get();
        pregunta.setIdPregunta(UUID.fromString(idPregunta));
        assertEquals(pregunta, respuesta.readEntity(Pregunta.class));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void testBuscarPorRango(){
        Response respuesta = target.queryParam("first", 0).queryParam("max", 10).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        List<Pregunta> preguntas = respuesta.readEntity(new GenericType<List<Pregunta>>() {});
        assertTrue(preguntas.size() >= 2);
    }

    @Order(4)
    @Test
    public void testActualizar(){
        pregunta2.setValor("¿Cuánto es 40 + 40?");
        Response respuesta = target.path(idPregunta2).request(MediaType.APPLICATION_JSON).put(Entity.json(pregunta2));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void testEliminar(){
        Response respuesta = target.path(idPregunta2).request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }

    @Order(6)
    @Test
    public void testBuscarPorRangoVacio(){
        Response respuesta = target.queryParam("first", 9999).queryParam("size", 10).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        List<Pregunta> preguntas = respuesta.readEntity(new GenericType<List<Pregunta>>() {});
        assertTrue(preguntas.isEmpty());
    }
}
