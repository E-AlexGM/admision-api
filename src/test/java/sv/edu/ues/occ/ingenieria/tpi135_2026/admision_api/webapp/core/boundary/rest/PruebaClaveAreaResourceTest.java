package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPK;

public class PruebaClaveAreaResourceTest {

    private UriInfo mockUriInfo;
    private PruebaClaveAreaDAO mockDAO;
    private PruebaClaveDAO mockPCD;
    private AreaDAO mockAreaDAO;
    private PruebaClaveAreaResource cut;

    private UUID idPruebaClave;
    private UUID idArea;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockDAO = Mockito.mock(PruebaClaveAreaDAO.class);
        mockPCD = Mockito.mock(PruebaClaveDAO.class);
        mockAreaDAO = Mockito.mock(AreaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/prueba_clave/1/area/1"));

        idPruebaClave = UUID.randomUUID();
        idArea = UUID.randomUUID();

        cut = new PruebaClaveAreaResource();
        cut.pruebaClaveAreaDAO = mockDAO;
        cut.pruebaClaveDAO = mockPCD;
        cut.areaDAO = mockAreaDAO;
    }

    private PruebaClaveArea nuevaConArea(UUID idArea) {
        PruebaClaveArea nueva = new PruebaClaveArea();
        nueva.setIdArea(new Area(idArea));
        return nueva;
    }

    // --- crear ---

    @Test
    public void crearExitosoSinIdAreaTest() {
        System.out.println("Ejecutando test: crearExitosoSinIdAreaTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = new PruebaClaveArea();

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        assertNull(nueva.getIdArea());
        Mockito.verifyNoInteractions(mockPCD);
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    // Verifica 400 cuando el body trae Area, pero con id interno nulo.
    public void crearConAreaSinIdInternoTest() {
        System.out.println("Ejecutando test: crearConAreaSinIdInternoTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = new PruebaClaveArea();
        nueva.setIdArea(new Area());

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockPCD);
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void crearPruebaClaveNoEncontradaTest() {
        System.out.println("Ejecutando test: crearPruebaClaveNoEncontradaTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenReturn(null);

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockPCD).buscarPorId(idPruebaClave);
        Mockito.verify(mockDAO, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Valida 404 cuando la prueba clave existe pero el area asociada no existe.
    public void crearAreaNoEncontradaTest() {
        System.out.println("Ejecutando test: crearAreaNoEncontradaTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenReturn(new PruebaClave(idPruebaClave));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(null);

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Cubre el flujo de creacion cuando el area recuperada no trae id interno.
    public void crearConAreaPersistidaSinIdTest() {
        System.out.println("Ejecutando test: crearConAreaPersistidaSinIdTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenReturn(new PruebaClave(idPruebaClave));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area());

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockDAO).crear(nueva);
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en PruebaClaveAreaResource");
        PruebaClave pruebaClave = new PruebaClave(idPruebaClave);
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenReturn(pruebaClave);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.doThrow(new IllegalStateException("Error en base de datos")).when(mockDAO).crear(nueva);

        assertThrows(IllegalStateException.class, () -> cut.crear(idPruebaClave, nueva, mockUriInfo));
        Mockito.verify(mockPCD).buscarPorId(idPruebaClave);
        Mockito.verify(mockDAO).crear(nueva);
    }

    @Test
    public void crearNullTest() {
        System.out.println("Ejecutando test: crearNullTest en PruebaClaveAreaResource");
        Response resultado = cut.crear(idPruebaClave, null, mockUriInfo);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
        Mockito.verifyNoInteractions(mockPCD);
    }

    @Test
    public void crearConIdTest() {
        System.out.println("Ejecutando test: crearConIdTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenReturn(new PruebaClave(idPruebaClave));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));

        Response resultado = cut.crear(idPruebaClave, nueva, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockPCD).buscarPorId(idPruebaClave);
        Mockito.verify(mockDAO).crear(nueva);
    }

    @Test
    public void crearIdPruebaClaveNullTest() {
        System.out.println("Ejecutando test: crearIdPruebaClaveNullTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = new PruebaClaveArea();
        Response resultado = cut.crear(null, nueva, mockUriInfo);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
        Mockito.verifyNoInteractions(mockPCD);
    }

    @Test
    public void crearConIllegalArgumentExceptionTest() {
        System.out.println("Ejecutando test: crearConIllegalArgumentExceptionTest en PruebaClaveAreaResource");
        PruebaClaveArea nueva = nuevaConArea(idArea);
        Mockito.when(mockPCD.buscarPorId(idPruebaClave)).thenThrow(new IllegalArgumentException("Argumento inválido"));

        assertThrows(IllegalArgumentException.class, () -> cut.crear(idPruebaClave, nueva, mockUriInfo));
        Mockito.verify(mockPCD).buscarPorId(idPruebaClave);
    }

    // --- buscarPorRango ---

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en PruebaClaveAreaResource");
        List<PruebaClaveArea> registros = List.of(nuevaConArea(idArea));
        Mockito.when(mockDAO.buscarPorPruebaClaveRango(idPruebaClave, 0, 50)).thenReturn(registros);
        Mockito.when(mockDAO.contarPorPruebaClave(idPruebaClave)).thenReturn(1L);

        Response resultado = cut.buscarPorRango(idPruebaClave, 0, 50);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        assertEquals("1", resultado.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
        Mockito.verify(mockDAO).buscarPorPruebaClaveRango(idPruebaClave, 0, 50);
        Mockito.verify(mockDAO).contarPorPruebaClave(idPruebaClave);
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorRango(idPruebaClave, -1, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void buscarPorRangoMaxCeroTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxCeroTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorRango(idPruebaClave, 0, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void buscarPorRangoMaxMayorACincuentaTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorACincuentaTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorRango(idPruebaClave, 0, 51);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void buscarPorRangoIdPruebaClaveNullTest() {
        System.out.println("Ejecutando test: buscarPorRangoIdPruebaClaveNullTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorRango(null, 0, 50);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en PruebaClaveAreaResource");
        Mockito.when(mockDAO.buscarPorPruebaClaveRango(idPruebaClave, 0, 50))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(idPruebaClave, 0, 50));
        Mockito.verify(mockDAO).buscarPorPruebaClaveRango(idPruebaClave, 0, 50);
    }

    // --- buscarPorId ---

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en PruebaClaveAreaResource");
        PruebaClaveArea encontrado = nuevaConArea(idArea);
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(encontrado);

        Response resultado = cut.buscarPorId(idPruebaClave, idArea);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en PruebaClaveAreaResource");
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(null);

        Response resultado = cut.buscarPorId(idPruebaClave, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en PruebaClaveAreaResource");
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idPruebaClave, idArea));
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
    }

    @Test
    public void buscarPorIdNullTest() {
        System.out.println("Ejecutando test: buscarPorIdNullTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorId(idPruebaClave, null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void buscarPorIdIdPruebaClaveNullTest() {
        System.out.println("Ejecutando test: buscarPorIdIdPruebaClaveNullTest en PruebaClaveAreaResource");
        Response resultado = cut.buscarPorId(null, UUID.randomUUID());
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    // --- actualizar ---

    @Test
    public void actualizarExitosoTest() {
        System.out.println("Ejecutando test: actualizarExitosoTest en PruebaClaveAreaResource");
        UUID otroIdPrueba = UUID.randomUUID();
        PruebaClaveArea pruebaClaveArea = new PruebaClaveArea(new PruebaClave(otroIdPrueba), new Area(idArea));
        PruebaClaveArea existente = new PruebaClaveArea(new PruebaClave(idPruebaClave), new Area(idArea));
        existente.setIdPruebaClave(new PruebaClave(idPruebaClave));
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.when(mockDAO.actualizar(pruebaClaveArea)).thenReturn(pruebaClaveArea);

        Response resultado = cut.actualizar(idPruebaClave, idArea, pruebaClaveArea);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        assertNotNull(pruebaClaveArea.getIdArea());
        assertEquals(idArea, pruebaClaveArea.getIdArea().getIdArea());
        assertEquals(idPruebaClave, pruebaClaveArea.getIdPruebaClave().getIdPruebaClave());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO).actualizar(pruebaClaveArea);
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en PruebaClaveAreaResource");
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(null);

        Response resultado = cut.actualizar(idPruebaClave, idArea, nuevaConArea(idArea));

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    // Verifica 400 si el idArea del body no coincide con el id_area del path.
    public void actualizarIdAreaBodyDistintoTest() {
        System.out.println("Ejecutando test: actualizarIdAreaBodyDistintoTest en PruebaClaveAreaResource");
        UUID idAreaBody = UUID.randomUUID();
        PruebaClaveArea body = nuevaConArea(idAreaBody);

        Response resultado = cut.actualizar(idPruebaClave, idArea, body);

        assertEquals(400, resultado.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    // Verifica 404 en actualizar cuando la relacion existe pero el area del body no existe.
    public void actualizarAreaNoEncontradaTest() {
        System.out.println("Ejecutando test: actualizarAreaNoEncontradaTest en PruebaClaveAreaResource");
        PruebaClaveArea body = nuevaConArea(idArea);
        PruebaClaveArea existente = nuevaConArea(idArea);
        existente.setIdPruebaClave(new PruebaClave(idPruebaClave));

        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(null);

        Response resultado = cut.actualizar(idPruebaClave, idArea, body);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarConExcepcionTest() {
        System.out.println("Ejecutando test: actualizarConExcepcionTest en PruebaClaveAreaResource");
        PruebaClaveArea pruebaClaveArea = nuevaConArea(idArea);
        PruebaClaveArea existente = nuevaConArea(idArea);
        existente.setIdPruebaClave(new PruebaClave(idPruebaClave));
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.doThrow(new IllegalStateException("Error en base de datos")).when(mockDAO).actualizar(pruebaClaveArea);

        assertThrows(IllegalStateException.class, () -> cut.actualizar(idPruebaClave, idArea, pruebaClaveArea));
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO).actualizar(pruebaClaveArea);
    }

    @Test
    public void actualizarNullTest() {
        System.out.println("Ejecutando test: actualizarNullTest en PruebaClaveAreaResource");
        Response resultado = cut.actualizar(idPruebaClave, UUID.randomUUID(), null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void actualizarSinIdEnBodyTest() {
        System.out.println("Ejecutando test: actualizarSinIdEnBodyTest en PruebaClaveAreaResource");
        Response resultado = cut.actualizar(idPruebaClave, null, new PruebaClaveArea());
        assertEquals(400, resultado.getStatus());
        assertEquals("El recurso no puede ser nulo y idPruebaClave no puede ser nulo",
                resultado.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    // Verifica 400 cuando en actualizar se envia body sin id de area.
    public void actualizarBodySinIdAreaTest() {
        System.out.println("Ejecutando test: actualizarBodySinIdAreaTest en PruebaClaveAreaResource");
        Response resultado = cut.actualizar(idPruebaClave, idArea, new PruebaClaveArea());
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void actualizarBodyConAreaSinIdInternoTest() {
        // Verifica 400 cuando el body trae Area, pero con id interno nulo.
        System.out.println("Ejecutando test: actualizarBodyConAreaSinIdInternoTest en PruebaClaveAreaResource");
        PruebaClaveArea body = new PruebaClaveArea();
        body.setIdArea(new Area());

        Response resultado = cut.actualizar(idPruebaClave, idArea, body);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void actualizarIdPruebaClaveNullTest() {
        System.out.println("Ejecutando test: actualizarIdPruebaClaveNullTest en PruebaClaveAreaResource");
        Response resultado = cut.actualizar(null, UUID.randomUUID(), nuevaConArea(UUID.randomUUID()));
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void actualizarConIllegalArgumentExceptionTest() {
        System.out.println("Ejecutando test: actualizarConIllegalArgumentExceptionTest en PruebaClaveAreaResource");
        PruebaClaveArea pruebaClaveArea = nuevaConArea(idArea);
        PruebaClaveArea existente = nuevaConArea(idArea);
        existente.setIdPruebaClave(new PruebaClave(idPruebaClave));
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.doThrow(new IllegalArgumentException("Error de validación")).when(mockDAO).actualizar(pruebaClaveArea);

        assertThrows(IllegalArgumentException.class, () -> cut.actualizar(idPruebaClave, idArea, pruebaClaveArea));
        Mockito.verify(mockDAO).actualizar(pruebaClaveArea);
    }

    // --- eliminar ---

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en PruebaClaveAreaResource");
        PruebaClaveArea existente = nuevaConArea(idArea);
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.doNothing().when(mockDAO).eliminar(existente);

        Response resultado = cut.eliminar(idPruebaClave, idArea);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO).eliminar(existente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en PruebaClaveAreaResource");
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(null);

        Response resultado = cut.eliminar(idPruebaClave, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en PruebaClaveAreaResource");
        PruebaClaveArea existente = nuevaConArea(idArea);
        Mockito.when(mockDAO.buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea))).thenReturn(existente);
        Mockito.doThrow(new RuntimeException("Error en base de datos")).when(mockDAO).eliminar(existente);

        assertThrows(RuntimeException.class, () -> cut.eliminar(idPruebaClave, idArea));
        Mockito.verify(mockDAO).buscarPorId(new PruebaClaveAreaPK(idPruebaClave, idArea));
        Mockito.verify(mockDAO).eliminar(existente);
    }

    @Test
    public void eliminarIdNullTest() {
        System.out.println("Ejecutando test: eliminarIdNullTest en PruebaClaveAreaResource");
        Response resultado = cut.eliminar(idPruebaClave, null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void eliminarIdPruebaClaveNullTest() {
        System.out.println("Ejecutando test: eliminarIdPruebaClaveNullTest en PruebaClaveAreaResource");
        Response resultado = cut.eliminar(null, UUID.randomUUID());
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDAO);
    }
}
