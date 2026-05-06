package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.DistractorArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.DistractorAreaPK;

public class DistractorAreaResourceTest {

    private UriInfo mockUriInfo;
    private DistractorAreaDAO mockDAA;
    private DistractorDAO mockDD;
    private AreaDAO mockAreaDAO;
    private DistractorAreaResource cut;

    private UUID idDistractor;
    private UUID idArea;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        mockDAA = Mockito.mock(DistractorAreaDAO.class);
        mockDD = Mockito.mock(DistractorDAO.class);
        mockAreaDAO = Mockito.mock(AreaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePath())
                .thenReturn(URI.create("http://localhost:8080/v1/distractor/1/area/1"));

        idDistractor = UUID.randomUUID();
        idArea = UUID.randomUUID();

        cut = new DistractorAreaResource();
        cut.distractorAreaDAO = mockDAA;
        cut.distractorDAO = mockDD;
        cut.areaDAO = mockAreaDAO;
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en DistractorAreaResource");
        Mockito.when(mockDD.buscarPorId(idDistractor)).thenReturn(new Distractor(idDistractor));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));

        Response resultado = cut.crear(idDistractor, idArea, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(idDistractor);
        Mockito.verify(mockAreaDAO).buscarPorId(idArea);
        Mockito.verify(mockDAA).crear(Mockito.any(DistractorArea.class));
    }

    @Test
    public void crearConIdAreaNuloTest() {
        System.out.println("Ejecutando test: crearConIdAreaNuloTest en DistractorAreaResource");

        Response resultado = cut.crear(idDistractor, null, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verify(mockDAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    public void crearDistractorNoEncontradoTest() {
        System.out.println("Ejecutando test: crearDistractorNoEncontradoTest en DistractorAreaResource");
        Mockito.when(mockDD.buscarPorId(idDistractor)).thenReturn(null);

        Response resultado = cut.crear(idDistractor, idArea, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(idDistractor);
        Mockito.verify(mockDAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Valida 404 cuando el distractor existe pero el area referenciada no existe.
    public void crearAreaNoEncontradaTest() {
        System.out.println("Ejecutando test: crearAreaNoEncontradaTest en DistractorAreaResource");
        Mockito.when(mockDD.buscarPorId(idDistractor)).thenReturn(new Distractor(idDistractor));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(null);

        Response resultado = cut.crear(idDistractor, idArea, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Cubre el branch de exito validando que se use la URI absoluta con el path completo.
    public void crearUsaUriAbsolutaConPathCompletoTest() {
        System.out.println("Ejecutando test: crearUsaUriAbsolutaConPathCompletoTest en DistractorAreaResource");
        Mockito.when(mockDD.buscarPorId(idDistractor)).thenReturn(new Distractor(idDistractor));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));

        Response resultado = cut.crear(idDistractor, idArea, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockUriInfo).getAbsolutePath();
        Mockito.verify(mockDAA).crear(Mockito.any(DistractorArea.class));
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en DistractorAreaResource");
        Mockito.when(mockDD.buscarPorId(idDistractor)).thenReturn(new Distractor(idDistractor));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        Mockito.doThrow(new RuntimeException("Error en base de datos")).when(mockDAA).crear(Mockito.any(DistractorArea.class));

        Response resultado = cut.crear(idDistractor, idArea, mockUriInfo);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAA).crear(Mockito.any(DistractorArea.class));
    }

    @Test
    public void crearBadRequestTest() {
        System.out.println("Ejecutando test: crearBadRequestTest en DistractorAreaResource");
        assertEquals(400, cut.crear(idDistractor, null, mockUriInfo).getStatus());
        assertEquals(400, cut.crear(null, idArea, mockUriInfo).getStatus());
        Mockito.verifyNoInteractions(mockDAA);
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en DistractorAreaResource");
        List<DistractorArea> registros = List.of(
                new DistractorArea(new Distractor(idDistractor), new Area(idArea)));
        Mockito.when(mockDAA.buscarPorDistractorRango(idDistractor, 0, 50)).thenReturn(registros);
        Mockito.when(mockDAA.contarPorDistractor(idDistractor)).thenReturn(1L);

        Response resultado = cut.buscarPorRango(idDistractor, 0, 50);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        assertEquals("1", resultado.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
        Mockito.verify(mockDAA).buscarPorDistractorRango(idDistractor, 0, 50);
        Mockito.verify(mockDAA).contarPorDistractor(idDistractor);
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en DistractorAreaResource");
        Mockito.when(mockDAA.buscarPorDistractorRango(idDistractor, 0, 50))
                .thenThrow(new RuntimeException("Error en base de datos"));

        Response resultado = cut.buscarPorRango(idDistractor, 0, 50);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAA).buscarPorDistractorRango(idDistractor, 0, 50);
    }

    @Test
    public void buscarPorRangoBadRequestTest() {
        System.out.println("Ejecutando test: buscarPorRangoBadRequestTest en DistractorAreaResource");
        assertEquals(400, cut.buscarPorRango(idDistractor, -1, 1).getStatus());
        assertEquals(400, cut.buscarPorRango(idDistractor, 0, 0).getStatus());
        assertEquals(400, cut.buscarPorRango(idDistractor, 0, 51).getStatus());
        assertEquals(400, cut.buscarPorRango(null, 0, 50).getStatus());
        Mockito.verifyNoInteractions(mockDAA);
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en DistractorAreaResource");
        DistractorArea encontrado = new DistractorArea(new Distractor(idDistractor), new Area(idArea));
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea))).thenReturn(encontrado);

        Response resultado = cut.buscarPorId(idDistractor, idArea);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockDAA).buscarPorId(new DistractorAreaPK(idDistractor, idArea));
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en DistractorAreaResource");
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea))).thenReturn(null);

        Response resultado = cut.buscarPorId(idDistractor, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAA).buscarPorId(new DistractorAreaPK(idDistractor, idArea));
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en DistractorAreaResource");
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        Response resultado = cut.buscarPorId(idDistractor, idArea);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAA).buscarPorId(new DistractorAreaPK(idDistractor, idArea));
    }

    @Test
    public void buscarPorIdBadRequestTest() {
        System.out.println("Ejecutando test: buscarPorIdBadRequestTest en DistractorAreaResource");
        assertEquals(400, cut.buscarPorId(idDistractor, null).getStatus());
        assertEquals(400, cut.buscarPorId(null, idArea).getStatus());
        Mockito.verifyNoInteractions(mockDAA);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en DistractorAreaResource");
        DistractorArea existente = new DistractorArea(new Distractor(idDistractor), new Area(idArea));
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea))).thenReturn(existente);
        Mockito.doNothing().when(mockDAA).eliminar(existente);

        Response resultado = cut.eliminar(idDistractor, idArea);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockDAA).eliminar(existente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en DistractorAreaResource");
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea))).thenReturn(null);

        Response resultado = cut.eliminar(idDistractor, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAA, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en DistractorAreaResource");
        DistractorArea existente = new DistractorArea(new Distractor(idDistractor), new Area(idArea));
        Mockito.when(mockDAA.buscarPorId(new DistractorAreaPK(idDistractor, idArea))).thenReturn(existente);
        Mockito.doThrow(new RuntimeException("Error en base de datos")).when(mockDAA).eliminar(existente);

        Response resultado = cut.eliminar(idDistractor, idArea);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAA).eliminar(existente);
    }

    @Test
    public void eliminarBadRequestTest() {
        System.out.println("Ejecutando test: eliminarBadRequestTest en DistractorAreaResource");
        assertEquals(400, cut.eliminar(idDistractor, null).getStatus());
        assertEquals(400, cut.eliminar(null, idArea).getStatus());
        Mockito.verifyNoInteractions(mockDAA);
    }
}
