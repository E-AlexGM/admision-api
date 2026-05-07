package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.DistractorArea;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DistractorAreaResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_DISTRACTOR = "distractor";
    private final String RESOURCE_NAME_AREA = "area";

    DistractorArea distractorArea = new DistractorArea();
    DistractorArea distractorArea2 = new DistractorArea();
    DistractorArea distractorArea3 = new DistractorArea();

    Area area = new Area();
    String idArea;
    Area area2 = new Area();
    String idArea2;
    Distractor distractor = new Distractor();
    String idDistractor;
    Distractor distractor2 = new Distractor();
    String idDistractor2;

    @Override
    public String getResourceName(){
        return "";
    }

    @BeforeEach
    public void setUp(){
        distractor.setValor("2");
        distractor.setActivo(true);

        distractor2.setValor("2");
        distractor2.setActivo(true);

        area.setNombre("MATEMATICAS");
        area.setActivo(true);
        area.setDescripcion("Ciencia de los números");

        area2.setNombre("FISICA");
        area2.setActivo(true);
        area2.setDescripcion("Ciencia de lo que compone el universo");
    }

    public void crearContexto(){
        Response respuestaArea = target.path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(area));
        idArea = respuestaArea.getLocation().toString().split(RESOURCE_NAME_AREA + "/")[1];

        Response respuestaArea2 = target.path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(area2));
        idArea2 = respuestaArea2.getLocation().toString().split(RESOURCE_NAME_AREA + "/")[1];

        Response respuestaDistractor = target.path(RESOURCE_NAME_DISTRACTOR)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractor));
        idDistractor = respuestaDistractor.getLocation().toString().split(RESOURCE_NAME_DISTRACTOR + "/")[1];

        Response respuestaDistractor2 = target.path(RESOURCE_NAME_DISTRACTOR)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractor2));
        idDistractor2 = respuestaDistractor2.getLocation().toString().split(RESOURCE_NAME_DISTRACTOR + "/")[1];


        area.setIdArea(UUID.fromString(idArea));
        area2.setIdArea(UUID.fromString(idArea2));
        distractor.setIdDistractor(UUID.fromString(idDistractor));
        distractor2.setIdDistractor(UUID.fromString(idDistractor2));

    }

    @Order(1)
    @Test
    public void testCrear(){
        crearContexto();

        distractorArea.setIdArea(area);
        Response respuesta = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor).path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractorArea));
        distractorArea2.setIdArea(area2);
        Response respuesta2 = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor).path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractorArea2));

        distractorArea3.setIdArea(area2);
        Response respuesta3 = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor2).path(RESOURCE_NAME_AREA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractorArea3));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta2.getStatus());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta3.getStatus());

        // Configure IDs from created entities for local comparison
        distractorArea.setIdDistractor(distractor);
        distractorArea.setIdArea(area);
        distractorArea2.setIdDistractor(distractor);
        distractorArea2.setIdArea(area2);
        distractorArea3.setIdDistractor(distractor2);
        distractorArea3.setIdArea(area2);
    }

    @Order(2)
    @Test
    public void testBuscarPorId(){
        Response respuesta = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor).path(RESOURCE_NAME_AREA).path(idArea)
                .request().get();
        DistractorArea encontrado = respuesta.readEntity(DistractorArea.class);
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(distractor.getIdDistractor(), encontrado.getIdDistractor().getIdDistractor());
        assertEquals(area.getIdArea(), encontrado.getIdArea().getIdArea());

    }

    @Order(3)
    @Test
    public void testBuscarPorRango(){
        Response respuesta = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor).path(RESOURCE_NAME_AREA)
                .request().get();
        List<DistractorArea> encontrados = respuesta.readEntity(new GenericType<List<DistractorArea>>() {});
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(2, encontrados.size());
        boolean found1 = encontrados.stream().anyMatch(e ->
                e.getIdArea() != null && e.getIdDistractor() != null &&
                        e.getIdArea().getIdArea().equals(area.getIdArea()) &&
                        e.getIdDistractor().getIdDistractor().equals(distractor.getIdDistractor())
        );
        boolean found2 = encontrados.stream().anyMatch(e ->
                e.getIdArea() != null && e.getIdDistractor() != null &&
                        e.getIdArea().getIdArea().equals(area2.getIdArea()) &&
                        e.getIdDistractor().getIdDistractor().equals(distractor.getIdDistractor())
        );
        assertTrue(found1);
        assertTrue(found2);
    }

    @Order(4)
    @Test
    public void testEliminar(){
        Response respuesta = target.path(RESOURCE_NAME_DISTRACTOR).path(idDistractor).path(RESOURCE_NAME_AREA).path(idArea)
                .request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }
}
