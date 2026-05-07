package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.sql.SQLException;
import java.time.LocalDate;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaJornadaAulaAspiranteOpcionResourceST extends AbstractIntegrationTest {

    @Override
    public String getResourceName() {
        return "prueba";
    }

    private static final String AULA_PRINCIPAL = "LAB-A";

    // Encapsula los IDs generados para cada escenario y evita variables sueltas por test.
    private static class ContextoSistema {
        UUID idTipoPrueba;
        UUID idPrueba;
        UUID idJornada;
        String idAula;
        UUID idAspirante;
        UUID idAspiranteOpcion;
    }

    private WebTarget apiRoot() {
        return cliente.target(String.format("http://localhost:%d/nuevoingreso/v1", liberty.getMappedPort(9080)));
    }

    private UUID crearTipoPruebaPorApi() {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setValor("TIPO-ST-" + UUID.randomUUID());
        tipo.setActivo(true);

        Response response = apiRoot().path("tipo_prueba")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(tipo));

        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearPruebaPorApi(UUID idTipoPrueba) {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setIdTipoPrueba(idTipoPrueba);

        Prueba nuevaPrueba = new Prueba();
        nuevaPrueba.setNombre("PRUEBA-ST-" + UUID.randomUUID());
        nuevaPrueba.setIndicaciones("INDICACIONES ST");
        nuevaPrueba.setPuntajeMaximo(new java.math.BigDecimal("10.00"));
        nuevaPrueba.setNotaAprobacion(new java.math.BigDecimal("6.00"));
        nuevaPrueba.setDuracion(90);
        nuevaPrueba.setFechaCreacion(OffsetDateTime.now().withNano(0));
        nuevaPrueba.setIdTipoPrueba(tipo);

        Response response = apiRoot().path("prueba")
                .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(nuevaPrueba));

        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearJornadaPorApi() {
        Jornada nuevaJornada = new Jornada();
        nuevaJornada.setNombre("JORNADA-ST-" + UUID.randomUUID());
        nuevaJornada.setFechaInicio(OffsetDateTime.now().withNano(0));
        nuevaJornada.setFechaFin(OffsetDateTime.now().plusDays(1).withNano(0));

        Response response = apiRoot().path("jornada")
                .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(nuevaJornada));

        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearAspirantePorApi() {
        UUID uniq = UUID.randomUUID();
        Aspirante nuevoAspirante = new Aspirante();
        nuevoAspirante.setNombres("Aspirante ST");
        nuevoAspirante.setApellidos("Sistema");
        nuevoAspirante.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevoAspirante.setDocumentoIdentidad("DOC-" + uniq.toString().substring(0, 8));
        nuevoAspirante.setCorreo("st-" + uniq + "@mail.com");
        nuevoAspirante.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response response = apiRoot().path("aspirante")
                .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(nuevoAspirante));

        Assertions.assertEquals(201, response.getStatus());
        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
        String location = response.getHeaderString("Location");
        return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
    }

    private UUID crearAspiranteOpcionPorApi(UUID idAspirante) {
        Aspirante aspirante = new Aspirante();
        aspirante.setIdAspirante(idAspirante);

        AspiranteOpcion nuevaOpcion = new AspiranteOpcion();
        nuevaOpcion.setIdOpcion("OPCION-ST-" + UUID.randomUUID().toString().substring(0, 8));
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
        Assertions.assertTrue(response.getHeaders().containsKey("Location"));
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

    private void crearRelacionPorApi(ContextoSistema contexto, boolean activo) {
        PruebaJornadaAulaAspiranteOpcion relacion = new PruebaJornadaAulaAspiranteOpcion();
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(contexto.idAspiranteOpcion);
        relacion.setIdAspiranteOpcion(opcion);
        relacion.setActivo(activo);

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

    // Prepara dependencias por API.
    private ContextoSistema crearContextoSistema(boolean incluirRelacion) throws SQLException {
        ContextoSistema contexto = new ContextoSistema();
        contexto.idTipoPrueba = crearTipoPruebaPorApi();
        contexto.idPrueba = crearPruebaPorApi(contexto.idTipoPrueba);
        contexto.idJornada = crearJornadaPorApi();
        contexto.idAula = AULA_PRINCIPAL + "-" + UUID.randomUUID().toString().substring(0, 8);
        contexto.idAspirante = crearAspirantePorApi();
        contexto.idAspiranteOpcion = crearAspiranteOpcionPorApi(contexto.idAspirante);

        crearPruebaJornadaPorApi(contexto.idPrueba, contexto.idJornada);
        crearJornadaAulaPorApi(contexto.idJornada, contexto.idAula);

        if (incluirRelacion) {
            crearRelacionPorApi(contexto, true);
        }

        return contexto;
    }

    @Order(1)
    @Test
    public void buscarPorRangoTest() throws SQLException {
        // Caso base: contexto válido sin registros hijos, debe responder lista vacía y total 0.
        System.out.println("buscarPorRango en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);
        int esperadoHttp = 200;
        int esperadoTotal = 0;

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .queryParam("first", 0)
                .queryParam("max", 50)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertNotNull(respuesta);
        Assertions.assertEquals(esperadoHttp, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey(ResponseHeaders.TOTAL_RECORDS.toString()));
        Assertions.assertEquals(
                esperadoTotal,
                Integer.parseInt(respuesta.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString())));

        List<PruebaJornadaAulaAspiranteOpcion> registros =
                respuesta.readEntity(new GenericType<List<PruebaJornadaAulaAspiranteOpcion>>() {
                });
        Assertions.assertNotNull(registros);
        Assertions.assertEquals(0, registros.size());
    }

    @Order(2)
    @Test
    public void buscarPorRangoParametrosInvalidosTest() throws SQLException {
        // Cobertura de validación: paginación inválida debe retornar 400.
        System.out.println("buscarPorRangoParametrosInvalidos en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .queryParam("first", -1)
                .queryParam("max", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearTest() throws SQLException {
        // Flujo exitoso de creación con dependencias existentes.
        System.out.println("crear en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);
        PruebaJornadaAulaAspiranteOpcion nuevo = new PruebaJornadaAulaAspiranteOpcion();
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(contexto.idAspiranteOpcion);
        nuevo.setIdAspiranteOpcion(opcion);
        nuevo.setActivo(true);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));

        String location = respuesta.getHeaderString("Location");
        UUID idCreado = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));
        Assertions.assertEquals(contexto.idAspiranteOpcion, idCreado);
    }

    @Order(4)
    @Test
    public void crearParametrosInvalidosTest() throws SQLException {
        // El body sin idAspiranteOpcion es inválido para crear.
        System.out.println("crearParametrosInvalidos en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);
        PruebaJornadaAulaAspiranteOpcion nuevo = new PruebaJornadaAulaAspiranteOpcion();

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void crearDependenciaNoEncontradaTest() throws SQLException {
        // El recurso debe rechazar referencias a aspirante_opcion inexistente.
        System.out.println("crearDependenciaNoEncontrada en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);
        PruebaJornadaAulaAspiranteOpcion nuevo = new PruebaJornadaAulaAspiranteOpcion();
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(UUID.randomUUID());
        nuevo.setIdAspiranteOpcion(opcion);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(6)
    @Test
    public void buscarPorIdTest() throws SQLException {
        // Consulta puntual de un registro existente.
        System.out.println("buscarPorId en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(true);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        PruebaJornadaAulaAspiranteOpcion encontrado = respuesta.readEntity(PruebaJornadaAulaAspiranteOpcion.class);
        Assertions.assertNotNull(encontrado);
        Assertions.assertEquals(contexto.idPrueba, encontrado.getIdPrueba().getIdPrueba());
        Assertions.assertEquals(contexto.idJornada, encontrado.getIdJornada().getIdJornada());
        Assertions.assertEquals(contexto.idAula, encontrado.getIdAula());
        Assertions.assertEquals(contexto.idAspiranteOpcion, encontrado.getIdAspiranteOpcion().getIdAspiranteOpcion());
    }

    @Order(7)
    @Test
    public void buscarPorIdNoEncontradoTest() throws SQLException {
        // Consulta de id inexistente debe responder 404.
        System.out.println("buscarPorIdNoEncontrado en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(8)
    @Test
    public void actualizarTest() throws SQLException {
        // Actualización parcial del campo activo conservando la clave compuesta del recurso.
        System.out.println("actualizar en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(true);

        PruebaJornadaAulaAspiranteOpcion actualizar = new PruebaJornadaAulaAspiranteOpcion();
        actualizar.setActivo(false);
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(contexto.idAspiranteOpcion);
        actualizar.setIdAspiranteOpcion(opcion);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(200, respuesta.getStatus());
        PruebaJornadaAulaAspiranteOpcion actualizado = respuesta.readEntity(PruebaJornadaAulaAspiranteOpcion.class);
        Assertions.assertNotNull(actualizado);
        Assertions.assertFalse(Boolean.TRUE.equals(actualizado.getActivo()));
        Assertions.assertEquals(contexto.idAspiranteOpcion, actualizado.getIdAspiranteOpcion().getIdAspiranteOpcion());
    }

    @Order(9)
    @Test
    public void actualizarAspiranteOpcionNoEncontradoTest() throws SQLException {
        // Si el id del body no coincide con el id del path, debe responder 400.
        System.out.println("actualizarAspiranteOpcionNoEncontrado en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(true);

        PruebaJornadaAulaAspiranteOpcion actualizar = new PruebaJornadaAulaAspiranteOpcion();
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(UUID.randomUUID());
        actualizar.setIdAspiranteOpcion(opcion);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(10)
    @Test
    public void actualizarNoEncontradoTest() throws SQLException {
        // No debe actualizar cuando la combinación de IDs del recurso no existe.
        System.out.println("actualizarNoEncontrado en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);
        UUID idNoExistente = UUID.randomUUID();

        PruebaJornadaAulaAspiranteOpcion actualizar = new PruebaJornadaAulaAspiranteOpcion();
        actualizar.setActivo(false);
        AspiranteOpcion opcion = new AspiranteOpcion();
        opcion.setIdAspiranteOpcion(idNoExistente);
        actualizar.setIdAspiranteOpcion(opcion);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(idNoExistente.toString())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(404, respuesta.getStatus());
    }

    @Order(11)
    @Test
    public void eliminarTest() throws SQLException {
        // Eliminación exitosa y verificación posterior de ausencia del recurso.
        System.out.println("eliminar en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(true);

        Response eliminarRespuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(contexto.idAspiranteOpcion.toString())
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
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, buscarRespuesta.getStatus());
    }

    @Order(12)
    @Test
    public void eliminarNoEncontradoTest() throws SQLException {
        // Eliminar un recurso inexistente debe responder 404.
        System.out.println("eliminarNoEncontrado en PruebaJornadaAulaAspiranteOpcionResource");

        ContextoSistema contexto = crearContextoSistema(false);

        Response respuesta = target.path(contexto.idPrueba.toString())
            .path("jornada")
            .path(contexto.idJornada.toString())
            .path("aula")
            .path(contexto.idAula)
            .path("aspirante_opcion")
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(404, respuesta.getStatus());
    }
}
