package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaClaveAreaPreguntaDistractorResourceST extends AbstractIntegrationTest {

    @Override
    public String getResourceName() {
        return "prueba_clave";
    }

    // Contexto con IDs de todas las entidades necesarias para cada escenario.
    private static class ContextoDistractor {
        UUID idPrueba;
        UUID idPruebaClave;
        UUID idArea;
        UUID idPregunta;
        UUID idDistractor;
    }

    // Raiz API para setup E2E.
    private WebTarget apiRoot() {
        return cliente.target(String.format("http://localhost:%d/nuevoingreso/v1", liberty.getMappedPort(9080)));
    }

    private UUID crearTipoPruebaPorApi() {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setValor("TIPO-PCAPD-ST-" + UUID.randomUUID());
        tipo.setActivo(true);

        Response response = apiRoot().path("tipo_prueba")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(tipo));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearPruebaPorApi(UUID idTipoPrueba) {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setIdTipoPrueba(idTipoPrueba);

        Prueba nuevaPrueba = new Prueba();
        nuevaPrueba.setNombre("PRUEBA-PCAPD-ST-" + UUID.randomUUID());
        nuevaPrueba.setIndicaciones("INDICACIONES ST PCAPD");
        nuevaPrueba.setPuntajeMaximo(new BigDecimal("10.00"));
        nuevaPrueba.setNotaAprobacion(new BigDecimal("6.00"));
        nuevaPrueba.setDuracion(90);
        nuevaPrueba.setFechaCreacion(OffsetDateTime.now().withNano(0));
        nuevaPrueba.setIdTipoPrueba(tipo);

        Response response = apiRoot().path("prueba")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevaPrueba));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearAreaPorApi() {
        Area area = new Area();
        area.setNombre("AREA-PCAPD-ST-" + UUID.randomUUID());
        area.setActivo(true);

        Response response = apiRoot().path("area")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(area));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearPreguntaPorApi() {
        Pregunta pregunta = new Pregunta();
        pregunta.setValor("PREGUNTA-PCAPD-ST-" + UUID.randomUUID());
        pregunta.setActivo(true);

        Response response = apiRoot().path("pregunta")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pregunta));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearDistractorPorApi() {
        Distractor distractor = new Distractor();
        distractor.setValor("DISTRACTOR-PCAPD-ST-" + UUID.randomUUID());
        distractor.setActivo(true);

        Response response = apiRoot().path("distractor")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(distractor));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearPruebaClavePorApi(UUID idPrueba) {
        PruebaClave nuevaClave = new PruebaClave();
        nuevaClave.setNombreClave("CLAVE-ST-" + UUID.randomUUID().toString().substring(0, 8));

        Response response = apiRoot().path("prueba")
                .path(idPrueba.toString())
                .path("clave")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevaClave));

        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private void crearPreguntaAreaPorApi(UUID idPregunta, UUID idArea) {
        PreguntaArea relacion = new PreguntaArea();
        relacion.setIdArea(new Area(idArea));

        Response response = apiRoot().path("pregunta")
            .path(idPregunta.toString())
            .path("area")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    private void crearPruebaClaveAreaPorApi(UUID idPruebaClave, UUID idArea) {
        PruebaClaveArea relacion = new PruebaClaveArea();
        relacion.setIdArea(new Area(idArea));

        Response response = target.path(idPruebaClave.toString())
                .path("area")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    private void crearPruebaClaveAreaPreguntaPorApi(UUID idPruebaClave, UUID idArea, UUID idPregunta) {
        PruebaClaveAreaPregunta relacion = new PruebaClaveAreaPregunta();
        relacion.setIdPregunta(new Pregunta(idPregunta));
        relacion.setPorcentaje(new BigDecimal("5.00"));

        Response response = target.path(idPruebaClave.toString())
                .path("area")
                .path(idArea.toString())
                .path("pregunta")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    private void crearPreguntaDistractorPorApi(UUID idPregunta, UUID idDistractor) {
        PreguntaDistractor relacion = new PreguntaDistractor();
        relacion.setCorrecto(false);
        relacion.setIdDistractor(new Distractor(idDistractor));

        Response response = apiRoot().path("pregunta")
                .path(idPregunta.toString())
                .path("distractor")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    private void crearRelacionPorApi(ContextoDistractor contexto, UUID idDistractor) {
        PruebaClaveAreaPreguntaDistractor relacion = new PruebaClaveAreaPreguntaDistractor();
        relacion.setIdDistractor(new Distractor(idDistractor));

        Response response = target.path(contexto.idPruebaClave.toString())
            .path("area")
            .path(contexto.idArea.toString())
            .path("pregunta")
            .path(contexto.idPregunta.toString())
            .path("distractor")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    // Contexto completo: API para entidades base y relaciones intermedias.
    private ContextoDistractor crearContexto(boolean incluirRelacion) throws SQLException {
        ContextoDistractor contexto = new ContextoDistractor();
        UUID idTipoPrueba = crearTipoPruebaPorApi();
        contexto.idPrueba = crearPruebaPorApi(idTipoPrueba);
        contexto.idArea = crearAreaPorApi();
        contexto.idPregunta = crearPreguntaPorApi();
        contexto.idDistractor = crearDistractorPorApi();
        contexto.idPruebaClave = crearPruebaClavePorApi(contexto.idPrueba);

        crearPreguntaAreaPorApi(contexto.idPregunta, contexto.idArea);
        crearPreguntaDistractorPorApi(contexto.idPregunta, contexto.idDistractor);
        crearPruebaClaveAreaPorApi(contexto.idPruebaClave, contexto.idArea);
        crearPruebaClaveAreaPreguntaPorApi(contexto.idPruebaClave, contexto.idArea, contexto.idPregunta);

        if (incluirRelacion) {
            crearRelacionPorApi(contexto, contexto.idDistractor);
        }

        return contexto;
    }

    @Order(1)
    @Test
    public void buscarPorRangoTest() throws SQLException {
        // Lista por padre con total en header.
        System.out.println("buscarPorRango en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(true);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .queryParam("first", 0)
                .queryParam("max", 50)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey(ResponseHeaders.TOTAL_RECORDS.toString()));
        List<PruebaClaveAreaPreguntaDistractor> registros =
                respuesta.readEntity(new GenericType<List<PruebaClaveAreaPreguntaDistractor>>() {});
        Assertions.assertNotNull(registros);
        Assertions.assertFalse(registros.isEmpty());
    }

    @Order(2)
    @Test
    public void buscarPorRangoParametrosInvalidosTest() throws SQLException {
        // Paginacion invalida debe responder 400.
        System.out.println("buscarPorRangoParametrosInvalidos en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .queryParam("first", -1)
                .queryParam("max", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearTest() throws SQLException {
        // Crea distractor asociado a un padre valido.
        System.out.println("crear en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);
        PruebaClaveAreaPreguntaDistractor relacion = new PruebaClaveAreaPreguntaDistractor();
        relacion.setIdDistractor(new Distractor(contexto.idDistractor));

        Response respuesta = target.path(contexto.idPruebaClave.toString())
            .path("area")
            .path(contexto.idArea.toString())
            .path("pregunta")
            .path(contexto.idPregunta.toString())
            .path("distractor")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));
    }

    @Order(4)
    @Test
    public void crearParametrosInvalidosTest() throws SQLException {
        // Sin body con id de distractor debe rechazar la llamada.
        System.out.println("crearParametrosInvalidos en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(new PruebaClaveAreaPreguntaDistractor()));

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void crearPadreNoEncontradoTest() throws SQLException {
        // Si no existe el padre, debe responder 404.
        System.out.println("crearPadreNoEncontrado en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);
        PruebaClaveAreaPreguntaDistractor relacion = new PruebaClaveAreaPreguntaDistractor();
        relacion.setIdDistractor(new Distractor(UUID.randomUUID()));

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
            .path(UUID.randomUUID().toString())
            .path("distractor")
                .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(6)
    @Test
    public void crearDistractorNoEncontradoTest() throws SQLException {
        // idDistractor inexistente debe responder 404.
        System.out.println("crearDistractorNoEncontrado en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);
        PruebaClaveAreaPreguntaDistractor relacion = new PruebaClaveAreaPreguntaDistractor();
        relacion.setIdDistractor(new Distractor(UUID.randomUUID()));

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(7)
    @Test
    public void buscarPorIdTest() throws SQLException {
        // Consulta puntual del distractor asociado existente.
        System.out.println("buscarPorId en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(true);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .path(contexto.idDistractor.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        PruebaClaveAreaPreguntaDistractor encontrado = respuesta.readEntity(PruebaClaveAreaPreguntaDistractor.class);
        Assertions.assertNotNull(encontrado);
        Assertions.assertEquals(contexto.idPruebaClave, encontrado.getIdPruebaClave().getIdPruebaClave());
        Assertions.assertEquals(contexto.idArea, encontrado.getIdArea().getIdArea());
        Assertions.assertEquals(contexto.idPregunta, encontrado.getIdPregunta().getIdPregunta());
        Assertions.assertEquals(contexto.idDistractor, encontrado.getIdDistractor().getIdDistractor());
    }

    @Order(8)
    @Test
    public void buscarPorIdNoEncontradoTest() throws SQLException {
        // Debe responder 404 para distractor inexistente en ese padre.
        System.out.println("buscarPorIdNoEncontrado en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(9)
    @Test
    public void eliminarTest() throws SQLException {
        // Elimina y confirma que no se pueda consultar nuevamente.
        System.out.println("eliminar en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(true);

        Response eliminarRespuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .path(contexto.idDistractor.toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(204, eliminarRespuesta.getStatus());

        Response buscarRespuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .path(contexto.idDistractor.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, buscarRespuesta.getStatus());
    }

    @Order(10)
    @Test
    public void eliminarNoEncontradoTest() throws SQLException {
        // Eliminar un distractor no asociado debe responder 404.
        System.out.println("eliminarNoEncontrado en PruebaClaveAreaPreguntaDistractorResource");

        ContextoDistractor contexto = crearContexto(false);

        Response respuesta = target.path(contexto.idPruebaClave.toString())
                .path("area")
                .path(contexto.idArea.toString())
                .path("pregunta")
                .path(contexto.idPregunta.toString())
                .path("distractor")
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(404, respuesta.getStatus());
    }
}
