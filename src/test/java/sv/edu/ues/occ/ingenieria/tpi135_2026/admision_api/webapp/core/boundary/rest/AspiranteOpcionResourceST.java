package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AspiranteOpcionResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_ASPIRANTE = "aspirante";
    private final String RESOURCE_NAME_OPCION = "opcion";

    Aspirante aspirante = new Aspirante();
    String idAspirante;

    AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();
    String idAspiranteOpcion;

    AspiranteOpcion aspiranteOpcion2 = new AspiranteOpcion();
    String idAspiranteOpcion2;

    @Override
    public String getResourceName() {
        return RESOURCE_NAME_ASPIRANTE;
    }

    @BeforeEach
    public void setUp(){
        aspirante.setFechaNacimiento(LocalDate.of(2000, 9, 1));
        aspirante.setCorreo("aspirante@email.com");
        aspirante.setFechaCreacion(OffsetDateTime.now());
        aspirante.setNombres("JOSE");
        aspirante.setApellidos("CHAVEZ");
        aspirante.setDocumentoIdentidad("1111111-1");

        aspiranteOpcion.setIdOpcion("INGENIERIA_SISTEMAS_INFORMATICOS");
        aspiranteOpcion.setFechaCreacion(OffsetDateTime.now());
        aspiranteOpcion.setPrioridad(1);

        aspiranteOpcion2.setIdOpcion("INGENIERIA_CIVIL");
        aspiranteOpcion2.setFechaCreacion(OffsetDateTime.now());
        aspiranteOpcion2.setPrioridad(2);
    }

    public void crearContexto(){
        Response respuesta = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(aspirante));
        idAspirante = respuesta.getLocation().toString().split(RESOURCE_NAME_ASPIRANTE + "/")[1];
    }

    @Order(1)
    @Test
    public void testCrear(){
        crearContexto();
        Response respuesta = target.path(idAspirante).path(RESOURCE_NAME_OPCION)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(aspiranteOpcion));
        Response respuesta2 = target.path(idAspirante).path(RESOURCE_NAME_OPCION)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(aspiranteOpcion2));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta2.getStatus());

        idAspiranteOpcion = respuesta.getLocation().toString().split(RESOURCE_NAME_OPCION + "/")[1];
        idAspiranteOpcion2 = respuesta2.getLocation().toString().split(RESOURCE_NAME_OPCION + "/")[1];
    }

    @Order(2)
    @Test
    public void testBuscarPorId(){
        Response respuesta = target.path(idAspirante).path(RESOURCE_NAME_OPCION + "/").path(idAspiranteOpcion).request().get();
        AspiranteOpcion encontrado = respuesta.readEntity(AspiranteOpcion.class);
        aspiranteOpcion.setIdAspiranteOpcion(UUID.fromString(idAspiranteOpcion));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(encontrado, aspiranteOpcion);
    }

    @Order(3)
    @Test
    public void testBuscarPorRango(){
        Response respuesta = target.path(idAspirante).path(RESOURCE_NAME_OPCION + "/").request().get();
        List<AspiranteOpcion> encontrados = respuesta.readEntity(new GenericType<List<AspiranteOpcion>>() {});
        assertTrue(encontrados.contains(aspiranteOpcion));
        aspiranteOpcion2.setIdAspiranteOpcion(UUID.fromString(idAspiranteOpcion2));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertTrue(encontrados.contains(aspiranteOpcion2));
    }

    @Order(4)
    @Test
    public void testActualizar(){
        aspiranteOpcion2.setIdOpcion("INGENIERIA_QUIMICA");
        Response respuesta = target.path(idAspirante).path(RESOURCE_NAME_OPCION).path(idAspiranteOpcion2)
                .request().put(Entity.json(aspiranteOpcion2));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void testEliminar(){
        Response respuesta = target.path(idAspirante).path(RESOURCE_NAME_OPCION).path(idAspiranteOpcion2)
                .request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }
}
