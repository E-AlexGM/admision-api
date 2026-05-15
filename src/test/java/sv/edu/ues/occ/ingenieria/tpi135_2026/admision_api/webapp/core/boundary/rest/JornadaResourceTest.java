package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;

public class JornadaResourceTest {

    private UriInfo mockUriInfo;
    private JornadaDAO mockJD;
    private JornadaResource cut;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockJD = Mockito.mock(JornadaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/jornada/1"));

        cut = new JornadaResource();
        cut.jornadaDAO = mockJD;
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en JornadaResource");
        Jornada nuevo = new Jornada();
        nuevo.setNombre("Matutina");
        nuevo.setFechaInicio(OffsetDateTime.now());
        nuevo.setFechaFin(OffsetDateTime.now().plusDays(1));

        Mockito.doNothing().when(mockJD).crear(nuevo);
        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockJD).crear(nuevo);
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en JornadaResource");
        Jornada nuevo = new Jornada();
        nuevo.setNombre("Matutina");
        nuevo.setFechaInicio(OffsetDateTime.now());
        nuevo.setFechaFin(OffsetDateTime.now().plusDays(1));

        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockJD).crear(nuevo);

        assertThrows(RuntimeException.class, () -> cut.crear(nuevo, mockUriInfo));
        Mockito.verify(mockJD).crear(nuevo);
    }

    @Test
    public void crearNullTest() {
        System.out.println("Ejecutando test: crearNullTest en JornadaResource");
        Response resultado = cut.crear(null, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void crearConIdTest() {
        System.out.println("Ejecutando test: crearConIdTest en JornadaResource");
        Jornada nuevo = new Jornada(UUID.randomUUID());
        Response resultado = cut.crear(nuevo, mockUriInfo);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en JornadaResource");
        UUID id = UUID.randomUUID();
        Jornada existente = new Jornada(id);

        Mockito.when(mockJD.buscarPorId(id)).thenReturn(existente);
        Mockito.doNothing().when(mockJD).eliminar(existente);

        Response resultado = cut.eliminar(id);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockJD).buscarPorId(id);
        Mockito.verify(mockJD).eliminar(existente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en JornadaResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockJD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.eliminar(id);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockJD).buscarPorId(id);
        Mockito.verify(mockJD, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en JornadaResource");
        UUID id = UUID.randomUUID();
        Jornada existente = new Jornada(id);
        Mockito.when(mockJD.buscarPorId(id)).thenReturn(existente);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockJD).eliminar(existente);
        assertThrows(RuntimeException.class, () -> cut.eliminar(id));
        Mockito.verify(mockJD).buscarPorId(id);
        Mockito.verify(mockJD).eliminar(existente);
    }

    @Test
    public void eliminarIdNullTest() {
        System.out.println("Ejecutando test: eliminarIdNullTest en JornadaResource");
        Response resultado = cut.eliminar(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en JornadaResource");
        Mockito.when(mockJD.buscarPorRango(0, 50)).thenReturn(List.of(new Jornada(UUID.randomUUID()), new Jornada(UUID.randomUUID())));
        Mockito.when(mockJD.contar()).thenReturn(2L);
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockJD).buscarPorRango(0, 50);
        Mockito.verify(mockJD).contar();
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en JornadaResource");
        Response resultado = cut.buscarPorRango(-1, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void buscarPorRangoMaxCeroTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxCeroTest en JornadaResource");
        Response resultado = cut.buscarPorRango(0, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void buscarPorRangoMaxMayorACincuentaTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorACincuentaTest en JornadaResource");
        Response resultado = cut.buscarPorRango(0, 51);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en JornadaResource");
        Mockito.when(mockJD.buscarPorRango(0, 50)).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(0, 50));
        Mockito.verify(mockJD).buscarPorRango(0, 50);
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en JornadaResource");
        Jornada encontrado = new Jornada(UUID.randomUUID());
        Mockito.when(mockJD.buscarPorId(Mockito.any())).thenReturn(encontrado);
        Response resultado = cut.buscarPorId(UUID.randomUUID());
        assertNotNull(resultado.getEntity());
        assertEquals(200, resultado.getStatus());
        Mockito.verify(mockJD).buscarPorId(Mockito.any());
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en JornadaResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockJD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.buscarPorId(id);
        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockJD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en JornadaResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockJD.buscarPorId(id)).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(id));
        Mockito.verify(mockJD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdNullTest() {
        System.out.println("Ejecutando test: buscarPorIdNullTest en JornadaResource");
        Response resultado = cut.buscarPorId(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void actualizarExitosoTest() {
        System.out.println("Ejecutando test: actualizarExitosoTest en JornadaResource");
        Jornada jornada = new Jornada(UUID.randomUUID());
        Mockito.when(mockJD.buscarPorId(jornada.getIdJornada())).thenReturn(jornada);
        Mockito.when(mockJD.actualizar(jornada)).thenReturn(jornada);

        Response resultado = cut.actualizar(jornada.getIdJornada(), jornada);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockJD).buscarPorId(jornada.getIdJornada());
        Mockito.verify(mockJD).actualizar(jornada);
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en JornadaResource");
        Jornada jornada = new Jornada(UUID.randomUUID());
        Mockito.when(mockJD.buscarPorId(jornada.getIdJornada())).thenReturn(null);

        Response resultado = cut.actualizar(jornada.getIdJornada(), jornada);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockJD).buscarPorId(jornada.getIdJornada());
        Mockito.verify(mockJD, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarConExcepcionTest() {
        System.out.println("Ejecutando test: actualizarConExcepcionTest en JornadaResource");
        Jornada jornada = new Jornada(UUID.randomUUID());
        Mockito.when(mockJD.buscarPorId(jornada.getIdJornada())).thenReturn(jornada);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockJD).actualizar(jornada);

        assertThrows(RuntimeException.class, () -> cut.actualizar(jornada.getIdJornada(), jornada));
        Mockito.verify(mockJD).buscarPorId(jornada.getIdJornada());
        Mockito.verify(mockJD).actualizar(jornada);
    }

    @Test
    public void actualizarNullTest() {
        System.out.println("Ejecutando test: actualizarNullTest en JornadaResource");
        Response resultado = cut.actualizar(null, null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }

    @Test
    public void actualizarSinIdTest() {
        System.out.println("Ejecutando test: actualizarSinIdTest en JornadaResource");
        Jornada jornada = new Jornada();
        Response resultado = cut.actualizar(jornada.getIdJornada(), jornada);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockJD);
    }
}
