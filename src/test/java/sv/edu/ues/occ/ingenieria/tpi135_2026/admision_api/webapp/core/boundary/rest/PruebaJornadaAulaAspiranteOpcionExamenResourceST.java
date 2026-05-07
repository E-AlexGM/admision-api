package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaJornadaAulaAspiranteOpcionExamenResourceST extends AbstractIntegrationTest {

    @Override
    public String getResourceName() {
        return "prueba";
    }

    private static final String AULA_PRINCIPAL = "LAB-EX";

    // Agrupa IDs de datos creados para cada escenario de prueba.
    private static class ContextoExamen {
        UUID idPrueba;
        UUID idJornada;
        String idAula;
        UUID idAspirante;
        UUID idAspiranteOpcion;
        UUID idPruebaClave;
    }

    // Raiz de API para setup E2E desde endpoints reales.
    private WebTarget apiRoot() {
        return cliente.target(String.format("http://localhost:%d/nuevoingreso/v1", liberty.getMappedPort(9080)));
    }

    private UUID crearTipoPruebaPorApi() {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setValor("TIPO-EX-ST-" + UUID.randomUUID());
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
        nuevaPrueba.setNombre("PRUEBA-EX-ST-" + UUID.randomUUID());
        nuevaPrueba.setIndicaciones("INDICACIONES ST EXAMEN");
        nuevaPrueba.setPuntajeMaximo(new BigDecimal("10.00"));
        nuevaPrueba.setNotaAprobacion(new BigDecimal("6.00"));
        nuevaPrueba.setDuracion(90);
        nuevaPrueba.setFechaCreacion(OffsetDateTime.now().withNano(0));
        nuevaPrueba.setIdTipoPrueba(tipo);

        Response response = apiRoot().path("prueba")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevaPrueba));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearJornadaPorApi() {
        Jornada nuevaJornada = new Jornada();
        nuevaJornada.setNombre("JORNADA-EX-ST-" + UUID.randomUUID());
        nuevaJornada.setFechaInicio(OffsetDateTime.now().withNano(0));
        nuevaJornada.setFechaFin(OffsetDateTime.now().plusDays(1).withNano(0));

        Response response = apiRoot().path("jornada")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevaJornada));

        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearAspirantePorApi() {
        UUID uniq = UUID.randomUUID();
        Aspirante nuevoAspirante = new Aspirante();
        nuevoAspirante.setNombres("Aspirante ST Examen");
        nuevoAspirante.setApellidos("Sistema");
        nuevoAspirante.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevoAspirante.setDocumentoIdentidad("DOC-EX-" + uniq.toString().substring(0, 8));
        nuevoAspirante.setCorreo("st-ex-" + uniq + "@mail.com");
        nuevoAspirante.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response response = apiRoot().path("aspirante")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevoAspirante));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearAspiranteOpcionPorApi(UUID idAspirante) {
        Aspirante aspirante = new Aspirante();
        aspirante.setIdAspirante(idAspirante);

        AspiranteOpcion nuevaOpcion = new AspiranteOpcion();
        nuevaOpcion.setIdOpcion("OPCION-EX-ST-" + UUID.randomUUID().toString().substring(0, 8));
        nuevaOpcion.setPrioridad(1);
        nuevaOpcion.setFechaCreacion(OffsetDateTime.now().withNano(0));
        nuevaOpcion.setIdAspirante(aspirante);

        Response response = apiRoot()
                .path("aspirante")
                .path(idAspirante.toString())
                .path("opcion")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevaOpcion));

        Assertions.assertEquals(201, response.getStatus());
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private void crearPruebaJornadaPorApi(UUID idPrueba, UUID idJornada) {
        PruebaJornada pj = new PruebaJornada();
        Jornada j = new Jornada();
        j.setIdJornada(idJornada);
        pj.setIdJornada(j);

        Response response = apiRoot().path("prueba")
            .path(idPrueba.toString())
            .path("jornada")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(pj));

        Assertions.assertEquals(201, response.getStatus());
    }

    private void crearJornadaAulaPorApi(UUID idJornada, String idAula) {
        JornadaAula ja = new JornadaAula();
        ja.setIdAula(idAula);

        Response response = apiRoot().path("jornada")
            .path(idJornada.toString())
            .path("aula")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(ja));

        Assertions.assertEquals(201, response.getStatus());
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

    // Crea el recurso padre necesario para operar con /examen.
    private void crearRelacionPorApi(ContextoExamen contexto) {
        PruebaJornadaAulaAspiranteOpcion relacion = new PruebaJornadaAulaAspiranteOpcion();
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(contexto.idAspiranteOpcion);
        relacion.setIdAspiranteOpcion(opcion);
        relacion.setActivo(true);

        Response response = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(relacion));

        Assertions.assertEquals(201, response.getStatus());
    }

    // Inserta un examen inicial para pruebas de lectura, duplicado, update y delete.
    private void crearExamenPorApi(ContextoExamen contexto, UUID idPruebaClave, BigDecimal resultado) {
        PruebaJornadaAulaAspiranteOpcionExamen examen = new PruebaJornadaAulaAspiranteOpcionExamen();
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(idPruebaClave);
        examen.setIdPruebaClave(pruebaClave);
        examen.setResultado(resultado);

        Response response = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(examen));

        Assertions.assertEquals(201, response.getStatus());
    }

    // Prepara un contexto funcional completo por API + SQL segun disponibilidad de endpoints.
    private ContextoExamen crearContextoExamen(boolean incluirExamen) throws SQLException {
        ContextoExamen contexto = new ContextoExamen();
        UUID idTipoPrueba = crearTipoPruebaPorApi();
        contexto.idPrueba = crearPruebaPorApi(idTipoPrueba);
        contexto.idJornada = crearJornadaPorApi();
        contexto.idAula = AULA_PRINCIPAL + "-" + UUID.randomUUID().toString().substring(0, 8);
        contexto.idAspirante = crearAspirantePorApi();
        contexto.idAspiranteOpcion = crearAspiranteOpcionPorApi(contexto.idAspirante);

        crearPruebaJornadaPorApi(contexto.idPrueba, contexto.idJornada);
        crearJornadaAulaPorApi(contexto.idJornada, contexto.idAula);
        contexto.idPruebaClave = crearPruebaClavePorApi(contexto.idPrueba);

        crearRelacionPorApi(contexto);

        if (incluirExamen) {
            crearExamenPorApi(contexto, contexto.idPruebaClave, new BigDecimal("8.50"));
        }

        return contexto;
    }

    @Order(1)
    @Test
    public void crearTest() throws SQLException {
        // Crea examen con padre y prueba_clave existentes.
        System.out.println("crear en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);
        PruebaJornadaAulaAspiranteOpcionExamen nuevo = new PruebaJornadaAulaAspiranteOpcionExamen();
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(contexto.idPruebaClave);
        nuevo.setIdPruebaClave(pruebaClave);
        nuevo.setResultado(new BigDecimal("9.25"));

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));
    }

    @Order(2)
    @Test
    public void crearParametrosInvalidosTest() throws SQLException {
        // Debe rechazar body incompleto.
        System.out.println("crearParametrosInvalidos en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);
        PruebaJornadaAulaAspiranteOpcionExamen nuevo = new PruebaJornadaAulaAspiranteOpcionExamen();

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearPruebaClaveNoEncontradaTest() throws SQLException {
        // Debe responder 404 cuando idPruebaClave no existe.
        System.out.println("crearPruebaClaveNoEncontrada en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);
        PruebaJornadaAulaAspiranteOpcionExamen nuevo = new PruebaJornadaAulaAspiranteOpcionExamen();
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(UUID.randomUUID());
        nuevo.setIdPruebaClave(pruebaClave);
        nuevo.setResultado(new BigDecimal("8.00"));

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(4)
    @Test
    public void crearDuplicadoTest() throws SQLException {
        // Debe responder conflicto cuando ya existe examen para ese padre.
        System.out.println("crearDuplicado en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(true);
        PruebaJornadaAulaAspiranteOpcionExamen nuevo = new PruebaJornadaAulaAspiranteOpcionExamen();
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(contexto.idPruebaClave);
        nuevo.setIdPruebaClave(pruebaClave);
        nuevo.setResultado(new BigDecimal("7.50"));

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(409, respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void buscarPorPadreTest() throws SQLException {
        // Lectura exitosa del examen existente por path padre.
        System.out.println("buscarPorPadre en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(true);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        PruebaJornadaAulaAspiranteOpcionExamen examen =
                respuesta.readEntity(PruebaJornadaAulaAspiranteOpcionExamen.class);
        Assertions.assertNotNull(examen);
        Assertions.assertEquals(contexto.idPrueba, examen.getIdPrueba().getIdPrueba());
        Assertions.assertEquals(contexto.idJornada, examen.getIdJornada().getIdJornada());
        Assertions.assertEquals(contexto.idAula, examen.getIdAula());
        Assertions.assertEquals(contexto.idAspiranteOpcion, examen.getIdAspiranteOpcion().getIdAspiranteOpcion());
    }

    @Order(6)
    @Test
    public void buscarPorPadreNoEncontradoTest() throws SQLException {
        // Debe responder 404 cuando el examen no existe.
        System.out.println("buscarPorPadreNoEncontrado en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(7)
    @Test
    public void actualizarTest() throws SQLException {
        // Actualiza resultado manteniendo la identidad del recurso.
        System.out.println("actualizar en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(true);
        PruebaJornadaAulaAspiranteOpcionExamen actualizar = new PruebaJornadaAulaAspiranteOpcionExamen();
        actualizar.setResultado(new BigDecimal("9.75"));
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(contexto.idPruebaClave);
        actualizar.setIdPruebaClave(pruebaClave);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(200, respuesta.getStatus());
        PruebaJornadaAulaAspiranteOpcionExamen actualizado =
                respuesta.readEntity(PruebaJornadaAulaAspiranteOpcionExamen.class);
        Assertions.assertNotNull(actualizado);
        Assertions.assertEquals(new BigDecimal("9.75"), actualizado.getResultado());
    }

    @Order(8)
    @Test
    public void actualizarNoEncontradoTest() throws SQLException {
        // No debe actualizar si el recurso objetivo no existe.
        System.out.println("actualizarNoEncontrado en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);
        PruebaJornadaAulaAspiranteOpcionExamen actualizar = new PruebaJornadaAulaAspiranteOpcionExamen();
        actualizar.setResultado(new BigDecimal("6.25"));
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(contexto.idPruebaClave);
        actualizar.setIdPruebaClave(pruebaClave);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(9)
    @Test
    public void actualizarPruebaClaveNoEncontradaTest() throws SQLException {
        // Debe responder 404 cuando se intenta apuntar a una prueba_clave inexistente.
        System.out.println("actualizarPruebaClaveNoEncontrada en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(true);
        PruebaJornadaAulaAspiranteOpcionExamen actualizar = new PruebaJornadaAulaAspiranteOpcionExamen();
        actualizar.setResultado(new BigDecimal("8.25"));
        PruebaClave pruebaClave = new PruebaClave();
        pruebaClave.setIdPruebaClave(UUID.randomUUID());
        actualizar.setIdPruebaClave(pruebaClave);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(10)
    @Test
    public void eliminarTest() throws SQLException {
        // Elimina y verifica que la lectura posterior ya no lo encuentra.
        System.out.println("eliminar en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(true);

        Response eliminarRespuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(204, eliminarRespuesta.getStatus());

        Response buscarRespuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, buscarRespuesta.getStatus());
    }

    @Order(11)
    @Test
    public void eliminarNoEncontradoTest() throws SQLException {
        // Eliminar recurso inexistente debe responder 404.
        System.out.println("eliminarNoEncontrado en PruebaJornadaAulaAspiranteOpcionExamenResource");

        ContextoExamen contexto = crearContextoExamen(false);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .path("jornada")
                .path(contexto.idJornada.toString())
                .path("aula")
                .path(contexto.idAula)
                .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .path("examen")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(404, respuesta.getStatus());
    }
}
