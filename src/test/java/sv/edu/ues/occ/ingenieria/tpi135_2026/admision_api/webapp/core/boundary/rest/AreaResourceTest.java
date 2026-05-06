package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;

public class AreaResourceTest {

    private UriInfo mockUriInfo;
    private AreaDAO mockAD;
    private AreaResource cut; // Class Under Test

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockAD = Mockito.mock(AreaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/area/1"));

        cut = new AreaResource();
        cut.areaDAO = mockAD; // Inyección manual del mock
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest");
        Area nuevo = new Area();

        Mockito.doNothing().when(mockAD).crear(nuevo);

        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertAll(
                () -> assertEquals(201, resultado.getStatus())
        );

        Mockito.verify(mockAD).crear(nuevo);
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest");
        Area nuevo = new Area();

        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockAD).crear(nuevo);

        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockAD).crear(nuevo);
    }

    @Test
    public void crearAreaNullTest() {
        System.out.println("Ejecutando test: crearAreaNullTest");
        Response resultado = cut.crear(null, mockUriInfo);

        assertEquals(422, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void crearAreaConIdTest() {
        System.out.println("Ejecutando test: crearAreaConIdTest");
        Area areaConId = new Area();
        areaConId.setIdArea(UUID.randomUUID());

        Response resultado = cut.crear(areaConId, mockUriInfo);

        assertEquals(422, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest");
        UUID id = UUID.randomUUID();
        Area areaExistente = new Area(id);

        Mockito.when(mockAD.buscarPorId(id)).thenReturn(areaExistente);
        Mockito.doNothing().when(mockAD).eliminar(areaExistente);

        Response resultado = cut.eliminar(id);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD).eliminar(areaExistente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest");
        UUID id = UUID.randomUUID();
        Mockito.when(mockAD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.eliminar(id);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest");
        UUID id = UUID.randomUUID();
        Area areaExistente = new Area(id);
        Mockito.when(mockAD.buscarPorId(id)).thenReturn(areaExistente);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockAD).eliminar(areaExistente);
        Response resultado = cut.eliminar(id);
        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD).eliminar(areaExistente);
    }

    @Test
    public void eliminarIdNullTest(){
        System.out.println("Ejecutando test: eliminarIdNullTest");
        Response resultado = cut.eliminar(null);
        assertEquals(422, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest");
        Mockito.when(mockAD.buscarPorRango(0, 50)).thenReturn(List.of(new Area(UUID.randomUUID()), new Area(UUID.randomUUID())));
        Mockito.when(mockAD.contar()).thenReturn(2L);
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorRango(0, 50);
        Mockito.verify(mockAD).contar();
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest");
        Response resultado = cut.buscarPorRango(-1, 0);
        assertEquals(422, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoMaxInvalidoTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorQue50Test y buscarPorRangoMaxCeroTest");
        Response resultadoMaxMayor = cut.buscarPorRango(0, 51);
        assertEquals(422, resultadoMaxMayor.getStatus());

        Response resultadoMaxCero = cut.buscarPorRango(0, 0);
        assertEquals(422, resultadoMaxCero.getStatus());

        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoConExcepcionTest(){
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest");
        Mockito.when(mockAD.buscarPorRango(0, 50)).thenThrow(new RuntimeException("Error en base de datos"));
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorRango(0, 50);
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest");
        UUID id = UUID.randomUUID();
        Area area = new Area(id);
        area.setNombre("AREA-UNIT");

        Mockito.when(mockAD.buscarPorId(id)).thenReturn(area);

        Response resultado = cut.buscarPorId(id);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest");
        UUID id = UUID.randomUUID();

        Mockito.when(mockAD.buscarPorId(id)).thenReturn(null);

        Response resultado = cut.buscarPorId(id);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest");
        UUID id = UUID.randomUUID();

        Mockito.when(mockAD.buscarPorId(id)).thenThrow(new RuntimeException("Error en base de datos"));

        Response resultado = cut.buscarPorId(id);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdNuloTest() {
        System.out.println("Ejecutando test: buscarPorIdNuloTest");

        Response resultado = cut.buscarPorId(null);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void actualizar(){
        Area area = new Area(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(area.getIdArea())).thenReturn(area);
        Mockito.when(mockAD.actualizar(area)).thenReturn(area);
        Response respuesta = cut.actualizar(area.getIdArea(), area);
        assertEquals(200, respuesta.getStatus());

    }


    @Test
    public void actualizarBadRequestIdArea(){
        Area area = new Area(UUID.randomUUID());
        Response respuesta = cut.actualizar(null, area);
        assertEquals(400, respuesta.getStatus());

    }

    @Test
    public void actualizarBadRequestArea(){
        Area area = new Area(UUID.randomUUID());
        Response respuesta = cut.actualizar(area.getIdArea(), null);
        assertEquals(400, respuesta.getStatus());

    }

    @Test
    public void actualizarErrorInterno(){
        Area area = new Area(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(area.getIdArea())).thenThrow(new RuntimeException("Error en base de datos"));;
        Response respuesta = cut.actualizar(area.getIdArea(), area);
        assertEquals(500, respuesta.getStatus());
    }


    @Test
    public void actualizarNoEncontrado(){
        Area area = new Area(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(area.getIdArea())).thenReturn(null);
        Mockito.when(mockAD.actualizar(area)).thenReturn(null);
        Response respuesta = cut.actualizar(area.getIdArea(), area);
        assertEquals(404, respuesta.getStatus());

    }

}