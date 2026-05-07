package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaArea;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PreguntaAreaResourceST extends AbstractIntegrationTest {

    @Override
    public String getResourceName() {
        return "pregunta";
    }

    private WebTarget apiRoot() {
        return cliente.target(String.format("http://localhost:%d/nuevoingreso/v1", liberty.getMappedPort(9080)));
    }

    private UUID crearPregunta() {
        Pregunta nueva = new Pregunta();
        nueva.setValor("PREGUNTA-PA-ST-" + UUID.randomUUID());
        nueva.setActivo(true);

        Response respuesta = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nueva));

        Assertions.assertEquals(201, respuesta.getStatus());
        String location = respuesta.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearArea() {
        Area nueva = new Area();
        nueva.setNombre("AREA-PA-ST-" + UUID.randomUUID());
        nueva.setActivo(true);

        Response respuesta = apiRoot().path("area")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nueva));

        Assertions.assertEquals(201, respuesta.getStatus());
        String location = respuesta.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private Response crearRelacion(UUID idPregunta, UUID idArea) {
        PreguntaArea relacion = new PreguntaArea();
        relacion.setIdArea(new Area(idArea));
        return target.path(idPregunta.toString())
            .path("area")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));
    }

    @Order(1)
    @Test
    public void buscarPorRangoTest() {
        System.out.println("buscarPorRango en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();
        UUID idArea = crearArea();
        Response crearRespuesta = crearRelacion(idPregunta, idArea);
        Assertions.assertEquals(201, crearRespuesta.getStatus());

        Response respuesta = target.path(idPregunta.toString())
                .path("area")
                .queryParam("first", 0)
                .queryParam("max", 50)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey(ResponseHeaders.TOTAL_RECORDS.toString()));
        List<PreguntaArea> registros = respuesta.readEntity(new GenericType<List<PreguntaArea>>() {});
        Assertions.assertNotNull(registros);
        Assertions.assertFalse(registros.isEmpty());
    }

    @Order(2)
    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("buscarPorRangoParametrosInvalidos en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();

        Response respuesta = target.path(idPregunta.toString())
                .path("area")
                .queryParam("first", -1)
                .queryParam("max", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearTest() {
        System.out.println("crear en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();
        UUID idArea = crearArea();

        Response respuesta = crearRelacion(idPregunta, idArea);

        Assertions.assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));
    }

    @Order(4)
    @Test
    public void crearPreguntaNoEncontradaTest() {
        System.out.println("crearPreguntaNoEncontrada en PreguntaAreaResource");

        UUID idArea = crearArea();

        Response respuesta = crearRelacion(UUID.randomUUID(), idArea);

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void buscarPorIdTest() {
        System.out.println("buscarPorId en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();
        UUID idArea = crearArea();
        Response crearRespuesta = crearRelacion(idPregunta, idArea);
        Assertions.assertEquals(201, crearRespuesta.getStatus());

        Response respuesta = target.path(idPregunta.toString())
                .path("area")
                .path(idArea.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        PreguntaArea encontrado = respuesta.readEntity(PreguntaArea.class);
        Assertions.assertNotNull(encontrado);
        Assertions.assertEquals(idPregunta, encontrado.getIdPregunta().getIdPregunta());
        Assertions.assertEquals(idArea, encontrado.getIdArea().getIdArea());
    }

    @Order(6)
    @Test
    public void eliminarTest() {
        System.out.println("eliminar en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();
        UUID idArea = crearArea();
        Response crearRespuesta = crearRelacion(idPregunta, idArea);
        Assertions.assertEquals(201, crearRespuesta.getStatus());

        Response eliminar = target.path(idPregunta.toString())
                .path("area")
                .path(idArea.toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(204, eliminar.getStatus());

        Response buscar = target.path(idPregunta.toString())
                .path("area")
                .path(idArea.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, buscar.getStatus());
    }

    @Order(7)
    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("eliminarNoEncontrado en PreguntaAreaResource");

        UUID idPregunta = crearPregunta();

        Response eliminar = target.path(idPregunta.toString())
                .path("area")
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(404, eliminar.getStatus());
    }
}
