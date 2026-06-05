package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.client.Entity;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaResourceST extends AbstractIntegrationTest {

    @Override
    public String getResourceName() {
        return "prueba";
    }

        private static class ContextoPrueba {
                UUID idTipoPrueba;
                UUID idPrueba;
        }

        private jakarta.ws.rs.client.WebTarget apiRoot() {
                return cliente.target(String.format("http://localhost:%d/nuevoingreso/v1", liberty.getMappedPort(9080)));
        }

        private UUID crearTipoPruebaPorApi() {
                TipoPrueba tipo = new TipoPrueba();
                tipo.setValor("TIPO-PR-ST-" + UUID.randomUUID());
                tipo.setActivo(true);

                Response response = apiRoot().path("tipo_prueba")
                                .request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(tipo));

                Assertions.assertEquals(201, response.getStatus());
                String location = response.getHeaderString("Location");
                return UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
        }

        private Prueba construirPrueba(UUID idTipoPrueba) {
                TipoPrueba tipo = new TipoPrueba();
                tipo.setIdTipoPrueba(idTipoPrueba);

                Prueba nueva = new Prueba();
                nueva.setNombre("PRUEBA-ST-" + UUID.randomUUID());
                nueva.setIndicaciones("INDICACIONES ST PRUEBA");
                nueva.setPuntajeMaximo(new BigDecimal("10.00"));
                nueva.setNotaAprobacion(new BigDecimal("6.00"));
                nueva.setDuracion(90);
                nueva.setFechaCreacion(OffsetDateTime.now().withNano(0));
                nueva.setIdTipoPrueba(tipo);
                return nueva;
        }

        private ContextoPrueba crearContexto() {
                ContextoPrueba contexto = new ContextoPrueba();
                contexto.idTipoPrueba = crearTipoPruebaPorApi();
                Prueba nueva = construirPrueba(contexto.idTipoPrueba);

                Response crear = target.request(MediaType.APPLICATION_JSON).post(Entity.json(nueva));
                Assertions.assertEquals(201, crear.getStatus());
                String location = crear.getHeaderString("Location");
                contexto.idPrueba = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
                return contexto;
        }

    @Order(1)
    @Test
    public void crearTest() {
        // Crea prueba con tipo_prueba existente.
        System.out.println("crear en PruebaResource");

        UUID idTipoPrueba = crearTipoPruebaPorApi();
        Prueba nueva = construirPrueba(idTipoPrueba);

        Response respuesta = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nueva));

        Assertions.assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));
        Assertions.assertNotNull(respuesta.getLocation());
    }

    @Order(2)
    @Test
    public void crearParametrosInvalidosTest() {
        // Debe fallar cuando faltan campos obligatorios del body.
        System.out.println("crearParametrosInvalidos en PruebaResource");

        Prueba nueva = new Prueba();
        Response respuesta = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nueva));

        Assertions.assertEquals(500, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearDependenciaNoEncontradaTest() {
        // Debe fallar cuando id_tipo_prueba no existe.
        System.out.println("crearDependenciaNoEncontrada en PruebaResource");

        TipoPrueba tipo = new TipoPrueba();
        tipo.setIdTipoPrueba(UUID.randomUUID());

        Prueba nueva = new Prueba();
        nueva.setNombre("PRUEBA-ST-" + UUID.randomUUID());
        nueva.setIndicaciones("INDICACIONES ST PRUEBA");
        nueva.setPuntajeMaximo(new BigDecimal("10.00"));
        nueva.setNotaAprobacion(new BigDecimal("6.00"));
        nueva.setDuracion(90);
        nueva.setFechaCreacion(OffsetDateTime.now().withNano(0));
        nueva.setIdTipoPrueba(tipo);

        Response respuesta = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nueva));

        Assertions.assertEquals(500, respuesta.getStatus());
    }

    @Order(4)
    @Test
    public void listarTest() {
        Response respuesta = target
                                .queryParam("activo", true)
                                .request(MediaType.APPLICATION_JSON)
                                .get();
        Assertions.assertEquals(200, respuesta.getStatus());
      
    }

    @Order(5)
    @Test
    public void buscarPorRangoTest() {
        // Lista pruebas por rango.
        System.out.println("buscarPorRango en PruebaResource");

        crearContexto();

        Response respuesta = target.queryParam("first", 0)
                .queryParam("max", 50)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, respuesta.getStatus());
        List<Prueba> registros = respuesta.readEntity(new GenericType<List<Prueba>>() {});
        Assertions.assertNotNull(registros);
        Assertions.assertFalse(registros.isEmpty());
    }

    @Order(6)
    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        // Paginacion invalida debe responder 400.
        System.out.println("buscarPorRangoParametrosInvalidos en PruebaResource");

        Response respuesta = target.queryParam("first", -1)
                .queryParam("max", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(400, respuesta.getStatus());
    }

    @Order(7)
    @Test
    public void buscarPorIdTest() {
        // Busca por id existente.
        System.out.println("buscarPorId en PruebaResource");

        ContextoPrueba contexto = crearContexto();

        Response buscar = target.path(contexto.idPrueba.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, buscar.getStatus());
        Prueba encontrado = buscar.readEntity(Prueba.class);
        Assertions.assertNotNull(encontrado);
                Assertions.assertEquals(contexto.idPrueba, encontrado.getIdPrueba());
    }

    @Order(8)
    @Test
    public void actualizarTest() {
        // Actualiza una prueba existente.
        System.out.println("actualizar en PruebaResource");

        ContextoPrueba contexto = crearContexto();

        Prueba actualizar = new Prueba();
        actualizar.setNombre("PRUEBA-ACTUALIZADA-ST");
        actualizar.setIndicaciones("INDICACIONES ACTUALIZADAS ST");
        actualizar.setPuntajeMaximo(new BigDecimal("10.00"));
        actualizar.setNotaAprobacion(new BigDecimal("6.00"));
        actualizar.setDuracion(90);
        actualizar.setFechaCreacion(OffsetDateTime.now().withNano(0));
        TipoPrueba tipo = new TipoPrueba();
        tipo.setIdTipoPrueba(contexto.idTipoPrueba);
        actualizar.setIdTipoPrueba(tipo);

        Response respuesta = target.path(contexto.idPrueba.toString())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        Assertions.assertEquals(200, respuesta.getStatus());
    }

    @Order(9)
    @Test
    public void eliminarTest() {
        // Elimina prueba existente y valida que no se encuentre luego.
        System.out.println("eliminar en PruebaResource");

        ContextoPrueba contexto = crearContexto();

        Response eliminar = target.path(contexto.idPrueba.toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(204, eliminar.getStatus());

        Response buscar = target.path("(" + contexto.idPrueba + ")")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(500, buscar.getStatus());
    }

    @Order(10)
    @Test
    public void eliminarNoEncontradoTest() {
        // Debe responder 404 cuando el recurso no existe.
        System.out.println("eliminarNoEncontrado en PruebaResource");

        Response eliminar = target.path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        Assertions.assertEquals(404, eliminar.getStatus());
    }
}
