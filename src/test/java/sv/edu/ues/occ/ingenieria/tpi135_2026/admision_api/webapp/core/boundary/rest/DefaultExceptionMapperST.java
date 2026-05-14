package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.Rfc9457;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.DistractorArea;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DefaultExceptionMapperST extends AbstractIntegrationTest {

    private final String RESOURCE_NAME_DISTRACTOR = "distractor";
    private final String RESOURCE_NAME_AREA = "area";

    String idArea;
    String idDistractor;

    @Override
    public String getResourceName() {
        return "";
    }

    Area area = new Area();
    Distractor distractor = new Distractor();
    DistractorArea distractorArea = new DistractorArea();

    @BeforeEach
    public void setUp(){
        area.setNombre("Area de prueba");
        area.setActivo(true);
        area.setDescripcion("Descripcion de area de prueba");

        distractor.setValor("Enunciado de distractor de prueba");
        distractor.setActivo(true);
    }

    public void crearContexto() {
        Response respuestaArea = target.path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(area));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaArea.getStatus());
        idArea = respuestaArea.getLocation().toString().split(RESOURCE_NAME_AREA + "/")[1];

        Response respuestaDistractor = target.path(RESOURCE_NAME_DISTRACTOR)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractor));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaDistractor.getStatus());
        idDistractor = respuestaDistractor.getLocation().toString().split(RESOURCE_NAME_DISTRACTOR + "/")[1];
    }

    @Order(1)
    @Test
    public void testDefaultExceptionMapper(){
        crearContexto();
        distractorArea.setIdArea(new Area(UUID.fromString(idArea)));

        Response respuesta = target.path(RESOURCE_NAME_DISTRACTOR)
                .path(idDistractor.toString())
                .path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractorArea));

        assertEquals(201, respuesta.getStatus());
        
          Response respuesta2 = target.path(RESOURCE_NAME_DISTRACTOR)
                .path(idDistractor.toString())
                .path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractorArea));
        assertEquals(500, respuesta2.getStatus());
        Rfc9457 errorResponse = respuesta2.readEntity(Rfc9457.class);
        assertNotNull(errorResponse);
        assertEquals("Ha ocurrido un error en el servidor", errorResponse.getTitle());
        System.out.println("Error message: " + errorResponse.getDetail());
    }

    
}
