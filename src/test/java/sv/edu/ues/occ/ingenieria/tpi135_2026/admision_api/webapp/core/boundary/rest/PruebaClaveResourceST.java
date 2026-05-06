package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaClaveResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_PRUEBA = "prueba";
    private final String RESOURCE_NAME_CLAVE = "clave";

    String idPrueba;
    String idClave;
    String idClave2;

    TipoPrueba tipoPrueba = new TipoPrueba();
    Prueba prueba = new Prueba();
    PruebaClave pruebaClave = new PruebaClave();
    PruebaClave pruebaClave2 = new PruebaClave();

    @Override
    public String getResourceName(){
        return "";
    }

    @BeforeEach
    public void setUp(){
        tipoPrueba.setValor("INGRESO_UNIVERSITARIO_PRIMERA_RONDA");
        tipoPrueba.setActivo(true);

        prueba.setNombre("NUEVO_INGRESO_2026");
        prueba.setIndicaciones("Puntaje mayor igual 60 aprobado, menor a este y mayor o igual 30 segunda ronda");
        prueba.setPuntajeMaximo(new BigDecimal(100));
        prueba.setFechaCreacion(OffsetDateTime.now());
        prueba.setNotaAprobacion(new BigDecimal(60));
        prueba.setDuracion(120);


        pruebaClave.setNombreClave("PRIMERA_CLAVE");
        pruebaClave2.setNombreClave("SEGUNDA_CLAVE");

    }

    @Order(1)
    @Test
    public void testAsignarClave(){
        Response respuestaTipoPrueba = target.path("tipo_prueba").request(MediaType.APPLICATION_JSON)
                .post(Entity.json(tipoPrueba));

        assertEquals(Response.Status.CREATED.getStatusCode() ,respuestaTipoPrueba.getStatus());

        String idTipoPrueba = respuestaTipoPrueba.getLocation().toString().split("tipo_prueba/")[1];
        prueba.setIdTipoPrueba(new TipoPrueba(UUID.fromString(idTipoPrueba)));
        Response respuestaPrueba = target.path("prueba").request(MediaType.APPLICATION_JSON)
                .post(Entity.json(prueba));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPrueba.getStatus());
        assertNotNull(respuestaPrueba.getLocation());

        // TEST REAL, YA TENGO UNA PRUEBA EN LA BD
        idPrueba = respuestaPrueba.getLocation().toString().split("prueba/")[1];

        Response respuesta = target.path(RESOURCE_NAME_PRUEBA).path(idPrueba).path(RESOURCE_NAME_CLAVE)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(pruebaClave));
        idClave = respuesta.getLocation().toString().split("clave/")[1];

        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
        assertNotNull(respuesta.getLocation());

        Response respuesta2 = target.path(RESOURCE_NAME_PRUEBA).path(idPrueba).path(RESOURCE_NAME_CLAVE)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(pruebaClave2));
        idClave2 = respuesta2.getLocation().toString().split("clave/")[1];

        assertNotNull(respuesta2.getLocation());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta2.getStatus());

    }

    @Order(2)
    @Test
    public void testBuscarPorId(){
        Response respuesta = target.path(RESOURCE_NAME_PRUEBA).path(idPrueba).path(RESOURCE_NAME_CLAVE).path(idClave)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());

        pruebaClave.setIdPruebaClave(UUID.fromString(idClave));
        assertEquals(pruebaClave, respuesta.readEntity(PruebaClave.class));
    }

    @Order(3)
    @Test
    public void testListarClaves(){
        Response respuesta = target.path(RESOURCE_NAME_PRUEBA).path(idPrueba).path(RESOURCE_NAME_CLAVE)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        List<PruebaClave> encontrados = respuesta.readEntity(new GenericType<List<PruebaClave>>() {});
        assertNotNull(encontrados);
        assertFalse(encontrados.isEmpty());
    }


    @Order(4)
    @Test
    public void testEliminar(){
        Response respuestae = target.path(RESOURCE_NAME_PRUEBA).path(idPrueba).path(RESOURCE_NAME_CLAVE).path(idClave)
                .request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuestae.getStatus());
    }



}

