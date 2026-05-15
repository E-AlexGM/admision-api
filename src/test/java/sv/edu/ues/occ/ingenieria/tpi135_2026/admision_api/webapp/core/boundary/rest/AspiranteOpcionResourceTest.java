package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;

public class AspiranteOpcionResourceTest {

    private UriInfo mockUriInfo;
    private AspiranteOpcionDAO mockAOD;
    private AspiranteDAO mockAD;
    private AspiranteOpcionResource cut;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockAOD = Mockito.mock(AspiranteOpcionDAO.class);
        mockAD = Mockito.mock(AspiranteDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/aspirante/1/opcion/1"));

        cut = new AspiranteOpcionResource();
        cut.aspiranteOpcionDAO = mockAOD;
        cut.aspiranteDAO = mockAD;
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();
        Aspirante aspirante = new Aspirante(idAspirante);

        Mockito.when(mockAD.buscarPorId(idAspirante)).thenReturn(aspirante);
        
        Response resultado = cut.crear(idAspirante, aspiranteOpcion, mockUriInfo);
        assertEquals(201, resultado.getStatus());
        assertNotNull(aspiranteOpcion.getIdAspiranteOpcion());
        assertNotNull(aspiranteOpcion.getFechaCreacion());
        Mockito.verify(mockAOD).crear(aspiranteOpcion);
    }

    @Test
    public void crearAspiranteNoEncontradoTest() {
        System.out.println("Ejecutando test: crearAspiranteNoEncontradoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();

        Mockito.when(mockAD.buscarPorId(idAspirante)).thenReturn(null);
        
        Response resultado = cut.crear(idAspirante, aspiranteOpcion, mockUriInfo);
        assertEquals(404, resultado.getStatus());
    }

    @Test
    public void crearConFechaCreacionTest() {
        System.out.println("Ejecutando test: crearConFechaCreacionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();
        OffsetDateTime fechaEsperada = OffsetDateTime.now().minusDays(1);
        aspiranteOpcion.setFechaCreacion(fechaEsperada);
        Aspirante aspirante = new Aspirante(idAspirante);

        Mockito.when(mockAD.buscarPorId(idAspirante)).thenReturn(aspirante);
        
        Response resultado = cut.crear(idAspirante, aspiranteOpcion, mockUriInfo);
        assertEquals(201, resultado.getStatus());
        assertEquals(fechaEsperada, aspiranteOpcion.getFechaCreacion());
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();
        Mockito.when(mockAD.buscarPorId(idAspirante)).thenThrow(new IllegalArgumentException("Error"));
        assertThrows(IllegalArgumentException.class, () -> cut.crear(idAspirante, aspiranteOpcion, mockUriInfo));
    }

    @Test
    public void crearNullTest() {
        System.out.println("Ejecutando test: crearNullTest en AspiranteOpcionResource");
        Response resultado = cut.crear(null, new AspiranteOpcion(), mockUriInfo);
        assertEquals(400, resultado.getStatus());
        
        resultado = cut.crear(UUID.randomUUID(), null, mockUriInfo);
        assertEquals(400, resultado.getStatus());
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        List<AspiranteOpcion> registros = Collections.singletonList(new AspiranteOpcion());
        Mockito.when(mockAOD.buscarPorAspiranteRango(idAspirante, 0, 10)).thenReturn(registros);
        Mockito.when(mockAOD.contarPorAspirante(idAspirante)).thenReturn(1L);

        Response resultado = cut.buscarPorRango(idAspirante, 0, 10);
        assertEquals(200, resultado.getStatus());
        assertEquals(registros, resultado.getEntity());
        assertEquals("1", resultado.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en AspiranteOpcionResource");
        Response resultado = cut.buscarPorRango(null, 0, 10);
        assertEquals(400, resultado.getStatus());

        resultado = cut.buscarPorRango(UUID.randomUUID(), -1, 10);
        assertEquals(400, resultado.getStatus());

        resultado = cut.buscarPorRango(UUID.randomUUID(), 0, 0); // max <= 0
        assertEquals(400, resultado.getStatus());

        resultado = cut.buscarPorRango(UUID.randomUUID(), 0, 51); // max > 50
        assertEquals(400, resultado.getStatus());
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        Mockito.when(mockAOD.contarPorAspirante(idAspirante)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(idAspirante, 0, 10));
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcion opcion = new AspiranteOpcion();

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(opcion);

        Response resultado = cut.buscarPorId(idAspirante, idOpcion);
        assertEquals(200, resultado.getStatus());
        assertEquals(opcion, resultado.getEntity());
    }

    @Test
    public void buscarPorIdNullTest() {
        System.out.println("Ejecutando test: buscarPorIdNullTest en AspiranteOpcionResource");
        Response resultado = cut.buscarPorId(null, UUID.randomUUID());
        assertEquals(400, resultado.getStatus());

        resultado = cut.buscarPorId(UUID.randomUUID(), null);
        assertEquals(400, resultado.getStatus());
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(null);

        Response resultado = cut.buscarPorId(idAspirante, idOpcion);
        assertEquals(404, resultado.getStatus());
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idAspirante, idOpcion));
    }

    @Test
    public void actualizarExitosoTest() {
        System.out.println("Ejecutando test: actualizarExitosoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcion nuevaOpcion = new AspiranteOpcion(idOpcion);
        AspiranteOpcion opcionExistente = new AspiranteOpcion(idOpcion);
        opcionExistente.setFechaCreacion(OffsetDateTime.now());
        opcionExistente.setIdAspirante(new Aspirante(idAspirante));

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(opcionExistente);

        Response resultado = cut.actualizar(idAspirante, idOpcion, nuevaOpcion);
        assertEquals(200, resultado.getStatus());
        assertEquals(opcionExistente.getFechaCreacion(), nuevaOpcion.getFechaCreacion());
        Mockito.verify(mockAOD).actualizar(nuevaOpcion);
        Mockito.clearInvocations(mockAOD);

        AspiranteOpcion nuevaOpcionConFecha = new AspiranteOpcion(idOpcion);
        OffsetDateTime fechaPersonalizada = OffsetDateTime.now().minusDays(2);
        nuevaOpcionConFecha.setFechaCreacion(fechaPersonalizada);
        Response resultadoConFecha = cut.actualizar(idAspirante, idOpcion, nuevaOpcionConFecha);
        assertEquals(200, resultadoConFecha.getStatus());
        assertEquals(fechaPersonalizada, nuevaOpcionConFecha.getFechaCreacion());
        Mockito.verify(mockAOD).actualizar(nuevaOpcionConFecha);
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcion nuevaOpcion = new AspiranteOpcion(idOpcion);

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(null);

        Response resultado = cut.actualizar(idAspirante, idOpcion, nuevaOpcion);
        assertEquals(404, resultado.getStatus());
    }

    @Test
    public void actualizarParametrosInvalidosTest() {
        System.out.println("Ejecutando test: actualizarParametrosInvalidosTest en AspiranteOpcionResource");
        Response resultado = cut.actualizar(null, null, new AspiranteOpcion(UUID.randomUUID()));
        assertEquals(400, resultado.getStatus());

        resultado = cut.actualizar(UUID.randomUUID(), UUID.randomUUID(), null);
        assertEquals(400, resultado.getStatus());

        resultado = cut.actualizar(UUID.randomUUID(), null, new AspiranteOpcion()); 
        assertEquals(400, resultado.getStatus());
    }

    @Test
    public void actualizarConExcepcionTest() {
        System.out.println("Ejecutando test: actualizarConExcepcionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcion nuevaOpcion = new AspiranteOpcion(idOpcion);
        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenThrow(new IllegalArgumentException("Error"));
        assertThrows(IllegalArgumentException.class, () -> cut.actualizar(idAspirante, idOpcion, nuevaOpcion));
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcion opcionExistente = new AspiranteOpcion(idOpcion);

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(opcionExistente);

        Response resultado = cut.eliminar(idAspirante, idOpcion);
        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockAOD).eliminar(opcionExistente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();

        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenReturn(null);

        Response resultado = cut.eliminar(idAspirante, idOpcion);
        assertEquals(404, resultado.getStatus());
    }

    @Test
    public void eliminarParametrosInvalidosTest() {
        System.out.println("Ejecutando test: eliminarParametrosInvalidosTest en AspiranteOpcionResource");
        Response resultado = cut.eliminar(null, UUID.randomUUID());
        assertEquals(400, resultado.getStatus());

        resultado = cut.eliminar(UUID.randomUUID(), null);
        assertEquals(400, resultado.getStatus());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en AspiranteOpcionResource");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        Mockito.when(mockAOD.buscarPorIdYAspirante(idOpcion, idAspirante)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> cut.eliminar(idAspirante, idOpcion));
    }
}
