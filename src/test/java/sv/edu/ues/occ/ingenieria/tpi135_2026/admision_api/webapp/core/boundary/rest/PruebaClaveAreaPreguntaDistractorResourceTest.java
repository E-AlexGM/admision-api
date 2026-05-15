package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractorPK;

class PruebaClaveAreaPreguntaDistractorResourceTest {

    private PruebaClaveAreaPreguntaDistractorResource resource;

    private PruebaClaveAreaPreguntaDistractorDAO mockDAO;
    private PruebaClaveAreaPreguntaDAO mockPadreDAO;
    private DistractorDAO mockDistractorDAO;

    private UUID idPruebaClave;
    private UUID idArea;
    private UUID idPregunta;
    private UUID idDistractor;

    @BeforeEach
    void setUp() {
        resource = new PruebaClaveAreaPreguntaDistractorResource();

        mockDAO = mock(PruebaClaveAreaPreguntaDistractorDAO.class);
        mockPadreDAO = mock(PruebaClaveAreaPreguntaDAO.class);
        mockDistractorDAO = mock(DistractorDAO.class);

        resource.pruebaClaveAreaPreguntaDistractorDAO = mockDAO;
        resource.pruebaClaveAreaPreguntaDAO = mockPadreDAO;
        resource.distractorDAO = mockDistractorDAO;

        idPruebaClave = UUID.randomUUID();
        idArea = UUID.randomUUID();
        idPregunta = UUID.randomUUID();
        idDistractor = UUID.randomUUID();
    }

    // =========================
    //  CREAR
    // =========================

    @Test
    void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockPadreDAO.buscarPorId(any())).thenReturn(new PruebaClaveAreaPregunta(new PruebaClave(idPruebaClave), new Area(idArea), new Pregunta(idPregunta)));
        when(mockDistractorDAO.buscarPorId(any())).thenReturn(new Distractor(idDistractor));

        UriInfo uriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://localhost/test");
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));

        Response response = resource.crear(idPruebaClave, idArea, idPregunta, body, uriInfo);

        assertEquals(201, response.getStatus());
        verify(mockDAO).crear(any(PruebaClaveAreaPreguntaDistractor.class));
    }

    @Test
    void crearDatosInvalidosTest() {
        System.out.println("Ejecutando test: crearDatosInvalidosTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.crear(null, null, null, null, null);
        assertEquals(400, response.getStatus());
    }

    @Test
    void crearConIdAreaNullTest() {
        System.out.println("Ejecutando test: crearConIdAreaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));
        Response response = resource.crear(idPruebaClave, null, idPregunta, body, mock(UriInfo.class));
        assertEquals(400, response.getStatus());
    }

    @Test
    void crearConIdPreguntaNullTest() {
        System.out.println("Ejecutando test: crearConIdPreguntaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));
        Response response = resource.crear(idPruebaClave, idArea, null, body, mock(UriInfo.class));
        assertEquals(400, response.getStatus());
    }

    @Test
    void crearConIdDistractorNullTest() {
        System.out.println("Ejecutando test: crearConIdDistractorNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.crear(idPruebaClave, idArea, idPregunta, null, mock(UriInfo.class));
        assertEquals(400, response.getStatus());
    }

    @Test
    void crearPadreNoExisteTest() {
        System.out.println("Ejecutando test: crearPadreNoExisteTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockPadreDAO.buscarPorId(any())).thenReturn(null);

        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));
        Response response = resource.crear(idPruebaClave, idArea, idPregunta, body, mock(UriInfo.class));

        assertEquals(404, response.getStatus());
    }

    @Test
    void crearDistractorNoExisteTest() {
        System.out.println("Ejecutando test: crearDistractorNoExisteTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockPadreDAO.buscarPorId(any())).thenReturn(new PruebaClaveAreaPregunta());
        when(mockDistractorDAO.buscarPorId(any())).thenReturn(null);

        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));
        Response response = resource.crear(idPruebaClave, idArea, idPregunta, body, mock(UriInfo.class));

        assertEquals(404, response.getStatus());
    }

    @Test
    void crearExceptionTest() {
        System.out.println("Ejecutando test: crearExceptionTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockPadreDAO.buscarPorId(any())).thenThrow(new RuntimeException());

        PruebaClaveAreaPreguntaDistractor body = new PruebaClaveAreaPreguntaDistractor();
        body.setIdDistractor(new Distractor(idDistractor));
        assertThrows(RuntimeException.class, () -> resource.crear(idPruebaClave, idArea, idPregunta, body, mock(UriInfo.class)));
    }

    // =========================
    //  BUSCAR POR RANGO
    // =========================

    @Test
    void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockDAO.buscarPorPadreRango(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(new PruebaClaveAreaPreguntaDistractor()));

        when(mockDAO.contarPorPadre(any(), any(), any())).thenReturn(1L);

        Response response = resource.buscarPorRango(idPruebaClave, idArea, idPregunta, 0, 10);

        assertEquals(200, response.getStatus());
        assertEquals("1", response.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
    }

    @Test
    void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(null, null, null, -1, 100);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoConIdAreaNullTest() {
        System.out.println("Ejecutando test: buscarPorRangoConIdAreaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(idPruebaClave, null, idPregunta, 0, 10);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoConIdPreguntaNullTest() {
        System.out.println("Ejecutando test: buscarPorRangoConIdPreguntaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(idPruebaClave, idArea, null, 0, 10);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoConFirstNegativoTest() {
        System.out.println("Ejecutando test: buscarPorRangoConFirstNegativoTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(idPruebaClave, idArea, idPregunta, -1, 10);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoMaxCeroTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxCeroTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(idPruebaClave, idArea, idPregunta, 0, 0);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoMaxMayorACincuentaTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorACincuentaTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorRango(idPruebaClave, idArea, idPregunta, 0, 51);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorRangoExceptionTest() {
        System.out.println("Ejecutando test: buscarPorRangoExceptionTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockDAO.buscarPorPadreRango(any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> resource.buscarPorRango(idPruebaClave, idArea, idPregunta, 0, 10));
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    @Test
    void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en PruebaClaveAreaPreguntaDistractorResource");
        PruebaClaveAreaPreguntaDistractorPK pk = new PruebaClaveAreaPreguntaDistractorPK(idPruebaClave, idArea, idPregunta, idDistractor);
        when(mockDAO.buscarPorId(pk)).thenReturn(new PruebaClaveAreaPreguntaDistractor());

        Response response = resource.buscarPorId(idPruebaClave, idArea, idPregunta, idDistractor);

        assertEquals(200, response.getStatus());
        verify(mockDAO).buscarPorId(pk);
    }

    @Test
    void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en PruebaClaveAreaPreguntaDistractorResource");
        PruebaClaveAreaPreguntaDistractorPK pk = new PruebaClaveAreaPreguntaDistractorPK(idPruebaClave, idArea, idPregunta, idDistractor);
        when(mockDAO.buscarPorId(pk)).thenReturn(null);

        Response response = resource.buscarPorId(idPruebaClave, idArea, idPregunta, idDistractor);

        assertEquals(404, response.getStatus());
        verify(mockDAO).buscarPorId(pk);
    }

    @Test
    void buscarPorIdParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorIdParametrosInvalidosTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorId(null, null, null, null);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorIdConIdAreaNullTest() {
        System.out.println("Ejecutando test: buscarPorIdConIdAreaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorId(idPruebaClave, null, idPregunta, idDistractor);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorIdConIdPreguntaNullTest() {
        System.out.println("Ejecutando test: buscarPorIdConIdPreguntaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorId(idPruebaClave, idArea, null, idDistractor);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorIdConIdDistractorNullTest() {
        System.out.println("Ejecutando test: buscarPorIdConIdDistractorNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.buscarPorId(idPruebaClave, idArea, idPregunta, null);
        assertEquals(400, response.getStatus());
    }

    @Test
    void buscarPorIdExceptionTest() {
        System.out.println("Ejecutando test: buscarPorIdExceptionTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockDAO.buscarPorId(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> resource.buscarPorId(idPruebaClave, idArea, idPregunta, idDistractor));
    }

    // =========================
    // ELIMINAR
    // =========================

    @Test
    void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en PruebaClaveAreaPreguntaDistractorResource");
        PruebaClaveAreaPreguntaDistractor existente = new PruebaClaveAreaPreguntaDistractor();

        when(mockDAO.buscarPorId(any())).thenReturn(existente);

        Response response = resource.eliminar(idPruebaClave, idArea, idPregunta, idDistractor);

        assertEquals(204, response.getStatus());
        verify(mockDAO).eliminar(existente);
    }

    @Test
    void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockDAO.buscarPorId(any())).thenReturn(null);

        Response response = resource.eliminar(idPruebaClave, idArea, idPregunta, idDistractor);

        assertEquals(404, response.getStatus());
    }

    @Test
    void eliminarParametrosInvalidosTest() {
        System.out.println("Ejecutando test: eliminarParametrosInvalidosTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.eliminar(null, null, null, null);
        assertEquals(400, response.getStatus());
    }

    @Test
    void eliminarConIdAreaNullTest() {
        System.out.println("Ejecutando test: eliminarConIdAreaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.eliminar(idPruebaClave, null, idPregunta, idDistractor);
        assertEquals(400, response.getStatus());
    }

    @Test
    void eliminarConIdPreguntaNullTest() {
        System.out.println("Ejecutando test: eliminarConIdPreguntaNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.eliminar(idPruebaClave, idArea, null, idDistractor);
        assertEquals(400, response.getStatus());
    }

    @Test
    void eliminarConIdDistractorNullTest() {
        System.out.println("Ejecutando test: eliminarConIdDistractorNullTest en PruebaClaveAreaPreguntaDistractorResource");
        Response response = resource.eliminar(idPruebaClave, idArea, idPregunta, null);
        assertEquals(400, response.getStatus());
    }

    @Test
    void eliminarExceptionTest() {
        System.out.println("Ejecutando test: eliminarExceptionTest en PruebaClaveAreaPreguntaDistractorResource");
        when(mockDAO.buscarPorId(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> resource.eliminar(idPruebaClave, idArea, idPregunta, idDistractor));
    }
}