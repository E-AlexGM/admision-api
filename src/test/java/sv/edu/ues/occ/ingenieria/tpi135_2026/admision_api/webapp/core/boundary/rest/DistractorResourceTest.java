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
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;

public class DistractorResourceTest {

    private UriInfo mockUriInfo;
    private DistractorDAO mockDD;
    private DistractorResource cut;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockDD = Mockito.mock(DistractorDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/distractor/1"));

        cut = new DistractorResource();
        cut.distractorDAO = mockDD;
    }

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en DistractorResource");
        Distractor nuevo = new Distractor();
        Mockito.doNothing().when(mockDD).crear(nuevo);
        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        Mockito.verify(mockDD).crear(nuevo);
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en DistractorResource");
        Distractor nuevo = new Distractor();
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockDD).crear(nuevo);

        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDD).crear(nuevo);
    }

    @Test
    public void crearNullTest() {
        System.out.println("Ejecutando test: crearNullTest en DistractorResource");
        Response resultado = cut.crear(null, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void crearConIdTest() {
        System.out.println("Ejecutando test: crearConIdTest en DistractorResource");
        Distractor nuevo = new Distractor(UUID.randomUUID());
        Response resultado = cut.crear(nuevo, mockUriInfo);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en DistractorResource");
        UUID id = UUID.randomUUID();
        Distractor distractorExistente = new Distractor(id);

        Mockito.when(mockDD.buscarPorId(id)).thenReturn(distractorExistente);
        Mockito.doNothing().when(mockDD).eliminar(distractorExistente);

        Response resultado = cut.eliminar(id);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(id);
        Mockito.verify(mockDD).eliminar(distractorExistente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en DistractorResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockDD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.eliminar(id);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(id);
        Mockito.verify(mockDD, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en DistractorResource");
        UUID id = UUID.randomUUID();
        Distractor distractorExistente = new Distractor(id);
        Mockito.when(mockDD.buscarPorId(id)).thenReturn(distractorExistente);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockDD).eliminar(distractorExistente);
        Response resultado = cut.eliminar(id);
        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(id);
        Mockito.verify(mockDD).eliminar(distractorExistente);
    }

    @Test
    public void eliminarIdNullTest() {
        System.out.println("Ejecutando test: eliminarIdNullTest en DistractorResource");
        Response resultado = cut.eliminar(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en DistractorResource");
        Mockito.when(mockDD.buscarPorRango(0, 50)).thenReturn(List.of(new Distractor(UUID.randomUUID()), new Distractor(UUID.randomUUID())));
        Mockito.when(mockDD.contar()).thenReturn(2L);
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockDD).buscarPorRango(0, 50);
        Mockito.verify(mockDD).contar();
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en DistractorResource");
        Response resultado = cut.buscarPorRango(-1, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void buscarPorRangoMaxCeroTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxCeroTest en DistractorResource");
        Response resultado = cut.buscarPorRango(0, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void buscarPorRangoMaxMayorACincuentaTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorACincuentaTest en DistractorResource");
        Response resultado = cut.buscarPorRango(0, 51);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void buscarPorRangoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en DistractorResource");
        Mockito.when(mockDD.buscarPorRango(0, 50)).thenThrow(new RuntimeException("Error en base de datos"));
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorRango(0, 50);
    }

    @Test
    public void buscarPorIdExitosoTest() {
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en DistractorResource");
        Distractor encontrado = new Distractor(UUID.randomUUID());
        Mockito.when(mockDD.buscarPorId(Mockito.any())).thenReturn(encontrado);
        Response resultado = cut.buscarPorId(UUID.randomUUID());
        assertNotNull(resultado.getEntity());
        assertEquals(200, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(Mockito.any());
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en DistractorResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockDD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.buscarPorId(id);
        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en DistractorResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockDD.buscarPorId(id)).thenThrow(new RuntimeException("Error en base de datos"));
        Response resultado = cut.buscarPorId(id);
        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdNullTest() {
        System.out.println("Ejecutando test: buscarPorIdNullTest en DistractorResource");
        Response resultado = cut.buscarPorId(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void actualizarExitosoTest() {
        System.out.println("Ejecutando test: actualizarExitosoTest en DistractorResource");
        Distractor distractor = new Distractor(UUID.randomUUID());
        Mockito.when(mockDD.buscarPorId(distractor.getIdDistractor())).thenReturn(distractor);
        Mockito.when(mockDD.actualizar(distractor)).thenReturn(distractor);

        Response resultado = cut.actualizar(distractor.getIdDistractor(), distractor);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockDD).buscarPorId(distractor.getIdDistractor());
        Mockito.verify(mockDD).actualizar(distractor);
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en DistractorResource");
        Distractor distractor = new Distractor(UUID.randomUUID());
        Mockito.when(mockDD.buscarPorId(distractor.getIdDistractor())).thenReturn(null);

        Response resultado = cut.actualizar(distractor.getIdDistractor(), distractor);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(distractor.getIdDistractor());
        Mockito.verify(mockDD, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarConExcepcionTest() {
        System.out.println("Ejecutando test: actualizarConExcepcionTest en DistractorResource");
        Distractor distractor = new Distractor(UUID.randomUUID());
        Mockito.when(mockDD.buscarPorId(distractor.getIdDistractor())).thenReturn(distractor);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockDD).actualizar(distractor);

        Response resultado = cut.actualizar(distractor.getIdDistractor(), distractor);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDD).buscarPorId(distractor.getIdDistractor());
        Mockito.verify(mockDD).actualizar(distractor);
    }

    @Test
    public void actualizarNullTest() {
        System.out.println("Ejecutando test: actualizarNullTest en DistractorResource");
        Response resultado = cut.actualizar(null, null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }

    @Test
    public void actualizarSinIdTest() {
        System.out.println("Ejecutando test: actualizarSinIdTest en DistractorResource");
        Distractor distractor = new Distractor();
        Response resultado = cut.actualizar(distractor.getIdDistractor(), distractor);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockDD);
    }
}
