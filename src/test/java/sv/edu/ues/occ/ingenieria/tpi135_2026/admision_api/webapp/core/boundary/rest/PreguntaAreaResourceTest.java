package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaAreaPK;

public class PreguntaAreaResourceTest {

    private UriInfo mockUriInfo;
    private PreguntaAreaDAO mockPAA;
    private PreguntaDAO mockPD;
    private AreaDAO mockAreaDAO;
    private PreguntaAreaResource cut;

    private UUID idPregunta;
    private UUID idArea;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockPAA = Mockito.mock(PreguntaAreaDAO.class);
        mockPD = Mockito.mock(PreguntaDAO.class);
        mockAreaDAO = Mockito.mock(AreaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/pregunta/1/area/1"));

        idPregunta = UUID.randomUUID();
        idArea = UUID.randomUUID();

        cut = new PreguntaAreaResource();
        cut.preguntaAreaDAO = mockPAA;
        cut.preguntaDAO = mockPD;
        cut.areaDAO = mockAreaDAO;
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en PreguntaAreaResource");
        Mockito.when(mockPD.buscarPorId(idPregunta)).thenReturn(new Pregunta(idPregunta));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        PreguntaArea nueva = new PreguntaArea();
        nueva.setIdArea(new Area(idArea));

        Response resultado = cut.crear(idPregunta, nueva, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockUriInfo).getAbsolutePathBuilder();
        assertNotNull(resultado.getLocation());
        Mockito.verify(mockPD).buscarPorId(idPregunta);
        Mockito.verify(mockAreaDAO).buscarPorId(idArea);
        Mockito.verify(mockPAA).crear(Mockito.any(PreguntaArea.class));
    }

    @Test
    public void crearConIdAreaNuloTest() {
        System.out.println("Ejecutando test: crearConIdAreaNuloTest en PreguntaAreaResource");
        PreguntaArea nueva = new PreguntaArea();

        Response resultado = cut.crear(idPregunta, nueva, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verify(mockPAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    public void crearPreguntaNoEncontradaTest() {
        System.out.println("Ejecutando test: crearPreguntaNoEncontradaTest en PreguntaAreaResource");
        Mockito.when(mockPD.buscarPorId(idPregunta)).thenReturn(null);
        PreguntaArea nueva = new PreguntaArea();
        nueva.setIdArea(new Area(idArea));

        Response resultado = cut.crear(idPregunta, nueva, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockPD).buscarPorId(idPregunta);
        Mockito.verify(mockPAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Valida 404 cuando la pregunta existe pero el area referenciada en body no existe.
    public void crearAreaNoEncontradaTest() {
        System.out.println("Ejecutando test: crearAreaNoEncontradaTest en PreguntaAreaResource");
        Mockito.when(mockPD.buscarPorId(idPregunta)).thenReturn(new Pregunta(idPregunta));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(null);
        PreguntaArea nueva = new PreguntaArea();
        nueva.setIdArea(new Area(idArea));

        Response resultado = cut.crear(idPregunta, nueva, mockUriInfo);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockPAA, Mockito.never()).crear(Mockito.any());
    }

    @Test
    // Cubre el branch de exito validando que se use la URI absoluta con el path completo.
    public void crearUsaUriAbsolutaConPathCompletoTest() {
        System.out.println("Ejecutando test: crearUsaUriAbsolutaConPathCompletoTest en PreguntaAreaResource");
        Mockito.when(mockPD.buscarPorId(idPregunta)).thenReturn(new Pregunta(idPregunta));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        PreguntaArea nueva = new PreguntaArea();
        nueva.setIdArea(new Area(idArea));

        Response resultado = cut.crear(idPregunta, nueva, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockUriInfo).getAbsolutePathBuilder();
        Mockito.verify(mockPAA).crear(Mockito.any(PreguntaArea.class));
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en PreguntaAreaResource");
        Mockito.when(mockPD.buscarPorId(idPregunta)).thenReturn(new Pregunta(idPregunta));
        Mockito.when(mockAreaDAO.buscarPorId(idArea)).thenReturn(new Area(idArea));
        PreguntaArea nueva = new PreguntaArea();
        nueva.setIdArea(new Area(idArea));
        Mockito.doThrow(new RuntimeException("Error en base de datos")).when(mockPAA).crear(Mockito.any(PreguntaArea.class));

        assertThrows(RuntimeException.class, () -> cut.crear(idPregunta, nueva, mockUriInfo));
        Mockito.verify(mockPAA).crear(Mockito.any(PreguntaArea.class));
    }

    @Test
    public void crearBadRequestTest() {
        System.out.println("Ejecutando test: crearBadRequestTest en PreguntaAreaResource");
        PreguntaArea nueva = new PreguntaArea();
        assertEquals(400, cut.crear(idPregunta, nueva, mockUriInfo).getStatus());
        assertEquals(400, cut.crear(null, nueva, mockUriInfo).getStatus());
        Mockito.verifyNoInteractions(mockPAA);
        Mockito.verifyNoInteractions(mockPD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en PreguntaAreaResource");
        List<PreguntaArea> registros = List.of(
                new PreguntaArea(new Pregunta(idPregunta), new Area(idArea)));
        Mockito.when(mockPAA.buscarPorPreguntaRango(idPregunta, 0, 50)).thenReturn(registros);
        Mockito.when(mockPAA.contarPorPregunta(idPregunta)).thenReturn(1L);

        Response resultado = cut.buscarPorRango(idPregunta, 0, 50);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        assertEquals("1", resultado.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
        Mockito.verify(mockPAA).buscarPorPreguntaRango(idPregunta, 0, 50);
        Mockito.verify(mockPAA).contarPorPregunta(idPregunta);
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en PreguntaAreaResource");
        Mockito.when(mockPAA.buscarPorPreguntaRango(idPregunta, 0, 50))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(idPregunta, 0, 50));
        Mockito.verify(mockPAA).buscarPorPreguntaRango(idPregunta, 0, 50);
    }

    @Test
    public void buscarPorRangoBadRequestTest() {
        System.out.println("Ejecutando test: buscarPorRangoBadRequestTest en PreguntaAreaResource");
        assertEquals(400, cut.buscarPorRango(idPregunta, -1, 1).getStatus());
        assertEquals(400, cut.buscarPorRango(idPregunta, 0, 0).getStatus());
        assertEquals(400, cut.buscarPorRango(idPregunta, 0, 51).getStatus());
        assertEquals(400, cut.buscarPorRango(null, 0, 50).getStatus());
        Mockito.verifyNoInteractions(mockPAA);
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en PreguntaAreaResource");
        PreguntaArea encontrado = new PreguntaArea(new Pregunta(idPregunta), new Area(idArea));
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea))).thenReturn(encontrado);

        Response resultado = cut.buscarPorId(idPregunta, idArea);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockPAA).buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en PreguntaAreaResource");
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea))).thenReturn(null);

        Response resultado = cut.buscarPorId(idPregunta, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockPAA).buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en PreguntaAreaResource");
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idPregunta, idArea));
        Mockito.verify(mockPAA).buscarPorId(new PreguntaAreaPK(idPregunta, idArea));
    }

    @Test
    public void buscarPorIdBadRequestTest() {
        System.out.println("Ejecutando test: buscarPorIdBadRequestTest en PreguntaAreaResource");
        assertEquals(400, cut.buscarPorId(idPregunta, null).getStatus());
        assertEquals(400, cut.buscarPorId(null, idArea).getStatus());
        Mockito.verifyNoInteractions(mockPAA);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en PreguntaAreaResource");
        PreguntaArea existente = new PreguntaArea(new Pregunta(idPregunta), new Area(idArea));
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea))).thenReturn(existente);
        Mockito.doNothing().when(mockPAA).eliminar(existente);

        Response resultado = cut.eliminar(idPregunta, idArea);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockPAA).eliminar(existente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en PreguntaAreaResource");
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea))).thenReturn(null);

        Response resultado = cut.eliminar(idPregunta, idArea);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockPAA, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en PreguntaAreaResource");
        PreguntaArea existente = new PreguntaArea(new Pregunta(idPregunta), new Area(idArea));
        Mockito.when(mockPAA.buscarPorId(new PreguntaAreaPK(idPregunta, idArea))).thenReturn(existente);
        Mockito.doThrow(new RuntimeException("Error en base de datos")).when(mockPAA).eliminar(existente);

        assertThrows(RuntimeException.class, () -> cut.eliminar(idPregunta, idArea));
        Mockito.verify(mockPAA).eliminar(existente);
    }

    @Test
    public void eliminarBadRequestTest() {
        System.out.println("Ejecutando test: eliminarBadRequestTest en PreguntaAreaResource");
        assertEquals(400, cut.eliminar(idPregunta, null).getStatus());
        assertEquals(400, cut.eliminar(null, idArea).getStatus());
        Mockito.verifyNoInteractions(mockPAA);
    }
}
