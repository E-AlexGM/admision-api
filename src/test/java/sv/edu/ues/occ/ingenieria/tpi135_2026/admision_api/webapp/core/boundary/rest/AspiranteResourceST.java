package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AspiranteResourceST extends AbstractIntegrationTest {

    private final String RESOURCE_NAME_ASPIRANTE = "aspirante";

    @Override
    public String getResourceName() {
        return "";
    }

    @Order(1)
    @Test
    public void buscarPorRangoTest() {
        System.out.println("buscarPorRango en AspiranteResource");

        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .queryParam("first", 0)
                .queryParam("max", 50)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey(ResponseHeaders.TOTAL_RECORDS.toString()));
        List<Aspirante> registros = respuesta.readEntity(new GenericType<List<Aspirante>>() {});
        Assertions.assertNotNull(registros);
    }

    @Order(2)
    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("buscarPorRangoParametrosInvalidos en AspiranteResource");

        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .queryParam("first", -1)
                .queryParam("max", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(400, respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void crearTest() {
        System.out.println("crear en AspiranteResource");

        Aspirante nuevo = new Aspirante();
        nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
        nuevo.setApellidos("APELLIDOS-ST");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
        nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
        nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
        .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        assertEquals(201, respuesta.getStatus());
        Assertions.assertTrue(respuesta.getHeaders().containsKey("Location"));
    }

    @Order(4)
    @Test
    public void crearConIdInvalidoTest() {
        System.out.println("crearConIdInvalido en AspiranteResource");

        Aspirante nuevo = new Aspirante();
        nuevo.setIdAspirante(UUID.randomUUID());
        nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
        nuevo.setApellidos("APELLIDOS-ST");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
        nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));

        assertEquals(400, respuesta.getStatus());
    }

    @Order(5)
    @Test
    public void buscarPorIdTest() {
        System.out.println("buscarPorId en AspiranteResource");

        Aspirante nuevo = new Aspirante();
        nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
        nuevo.setApellidos("APELLIDOS-ST");
        nuevo.setFechaNacimiento(LocalDate.of(2001, 2, 2));
        nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
        nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
        nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response crear = target.path(RESOURCE_NAME_ASPIRANTE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));
        assertEquals(201, crear.getStatus());

        String location = crear.getHeaderString("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));

        Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, buscar.getStatus());
        Aspirante encontrado = buscar.readEntity(Aspirante.class);
        Assertions.assertNotNull(encontrado);
        assertEquals(id, encontrado.getIdAspirante());
    }

    @Order(6)
    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("buscarPorIdNoEncontrado en AspiranteResource");

        Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(UUID.randomUUID().toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, buscar.getStatus());
    }

    @Order(7)
    @Test
    public void actualizarTest() {
        System.out.println("actualizar en AspiranteResource");

        Aspirante nuevo = new Aspirante();
        nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
        nuevo.setApellidos("APELLIDOS-ST");
        nuevo.setFechaNacimiento(LocalDate.of(2002, 3, 3));
        nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
        nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
        nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response crear = target.path(RESOURCE_NAME_ASPIRANTE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));
        assertEquals(201, crear.getStatus());

        String location = crear.getHeaderString("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));

        Aspirante actualizar = new Aspirante();
        actualizar.setNombres("NOMBRES-ACTUALIZADO");
        actualizar.setApellidos("APELLIDOS-ACTUALIZADO");
        actualizar.setFechaNacimiento(LocalDate.of(2002, 3, 3));
        actualizar.setDocumentoIdentidad("DOC-ACT-ST");
        actualizar.setCorreo("actualizado@test.com");
        actualizar.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actualizar));

        assertEquals(200, respuesta.getStatus());
    }

    @Order(8)
    @Test
    public void eliminarTest() {
        System.out.println("eliminar en AspiranteResource");

        Aspirante nuevo = new Aspirante();
        nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
        nuevo.setApellidos("APELLIDOS-ST");
        nuevo.setFechaNacimiento(LocalDate.of(2003, 4, 4));
        nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
        nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
        nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

        Response crear = target.path(RESOURCE_NAME_ASPIRANTE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(nuevo));
        assertEquals(201, crear.getStatus());

        String location = crear.getHeaderString("Location");
        UUID id = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));

        Response eliminar = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(204, eliminar.getStatus());

        Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, buscar.getStatus());
    }

        @Order(9)
        @Test
        public void buscarPorApellidosTest() {
                System.out.println("buscarPorApellidos en AspiranteResource");

                String apellidos = "APELLIDOS-ST-BUSCAR-" + UUID.randomUUID().toString().substring(0, 8);
                Aspirante nuevo = new Aspirante();
                nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
                nuevo.setApellidos(apellidos);
                nuevo.setFechaNacimiento(LocalDate.of(2004, 5, 5));
                nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
                nuevo.setCorreo("aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com");
                nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

                Response crear = target.path(RESOURCE_NAME_ASPIRANTE)
                        .request(MediaType.APPLICATION_JSON)
                                .post(Entity.json(nuevo));
                assertEquals(201, crear.getStatus());

                Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                        .path("buscar")
                                .queryParam("apellidos", apellidos)
                                .request(MediaType.APPLICATION_JSON)
                                .get();

                assertEquals(200, buscar.getStatus());
                List<Aspirante> encontrados = buscar.readEntity(new GenericType<List<Aspirante>>() {});
                Assertions.assertNotNull(encontrados);
                Assertions.assertFalse(encontrados.isEmpty());
                Assertions.assertTrue(encontrados.stream().anyMatch(a -> apellidos.equals(a.getApellidos())));
        }

        @Order(10)
        @Test
        public void buscarPorApellidosNoEncontradoTest() {
                System.out.println("buscarPorApellidosNoEncontrado en AspiranteResource");

                String apellidosInexistentes = "APELLIDOS-ST-NO-ENCONTRADO-" + UUID.randomUUID().toString().substring(0, 8);

                Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                        .path("buscar")
                        .queryParam("apellidos", apellidosInexistentes)
                        .request(MediaType.APPLICATION_JSON)
                        .get();

                assertEquals(404, buscar.getStatus());
        }

       @Order(11)
        @Test
        public void buscarPorCorreoTest() {
                System.out.println("buscarPorCorreo en AspiranteResource");

                String correo = "aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
                Aspirante nuevo = new Aspirante();
                nuevo.setNombres("NOMBRES-ST-" + UUID.randomUUID());
                nuevo.setApellidos("APELLIDOS-ST-CORREO");
                nuevo.setFechaNacimiento(LocalDate.of(2005, 6, 6));
                nuevo.setDocumentoIdentidad("DOC-ST-" + UUID.randomUUID().toString().substring(0, 8));
                nuevo.setCorreo(correo);
                nuevo.setFechaCreacion(OffsetDateTime.now().withNano(0));

                Response crear = target.path(RESOURCE_NAME_ASPIRANTE)
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(nuevo));
                assertEquals(201, crear.getStatus());

                Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                        .path("buscar")
                        .queryParam("correo", correo)
                        .request(MediaType.APPLICATION_JSON)
                        .get();

                assertEquals(200, buscar.getStatus());
                Aspirante encontrado = buscar.readEntity(Aspirante.class);
                Assertions.assertNotNull(encontrado);
                Assertions.assertEquals(correo, encontrado.getCorreo());
                Assertions.assertEquals(nuevo.getApellidos(), encontrado.getApellidos());
        }

        @Order(12)
        @Test
        public void buscarPorCorreoNoEncontradoTest() {
                System.out.println("buscarPorCorreoNoEncontrado en AspiranteResource");

                String correoInexistente = "aspirante" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";

                Response buscar = target.path(RESOURCE_NAME_ASPIRANTE)
                        .path("buscar")
                        .queryParam("correo", correoInexistente)
                        .request(MediaType.APPLICATION_JSON)
                        .get();

                assertEquals(404, buscar.getStatus());
        }


}
