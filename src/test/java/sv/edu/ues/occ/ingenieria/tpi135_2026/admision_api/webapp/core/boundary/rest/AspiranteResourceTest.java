package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;

public class AspiranteResourceTest {

    private UriInfo mockUriInfo;
    private AspiranteDAO mockAD;
    private AspiranteResource cut;


    @BeforeEach
    public void setup(){
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockAD = Mockito.mock(AspiranteDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/aspirante/1"));

        cut = new AspiranteResource();
        cut.aspiranteDAO = mockAD;



    }

    @Test
    public void crearExitosoTest(){
        System.out.println("Ejecutando test: crearExitosoTest en AspiranteResource");
        Aspirante nuevo = new Aspirante();
        Mockito.doNothing().when(mockAD).crear(nuevo);
        Response resultado = cut.crear(nuevo, mockUriInfo);

        assertEquals(201, resultado.getStatus());
        assertEquals(1, Integer.parseInt(resultado.getHeaderString("Location").split("aspirante/")[1]));

        Mockito.verify(mockAD).crear(nuevo);
    }

    @Test
    public void crearConExcepcionTest() {
        System.out.println("Ejecutando test: crearConExcepcionTest en AspiranteResource");
        Aspirante nuevo = new Aspirante();

        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockAD).crear(nuevo);

        assertThrows(RuntimeException.class, () -> cut.crear(nuevo, mockUriInfo));
        Mockito.verify(mockAD).crear(nuevo);
    }

    @Test
    public void crearNullTest() {
        System.out.println("Ejecutando test: crearNullTest en AspiranteResource");
        Response resultado = cut.crear(null, mockUriInfo);

        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void crearConIdTest(){
        System.out.println("Ejecutando test: crearConIdTest en AspiranteResource");
        Aspirante nuevo = new Aspirante(UUID.randomUUID());
        Response resultado = cut.crear(nuevo, mockUriInfo);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en AspiranteResource");
        UUID id = UUID.randomUUID();
        Aspirante aspiranteExistente = new Aspirante(id);

        Mockito.when(mockAD.buscarPorId(id)).thenReturn(aspiranteExistente);
        Mockito.doNothing().when(mockAD).eliminar(aspiranteExistente);

        Response resultado = cut.eliminar(id);

        assertEquals(204, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD).eliminar(aspiranteExistente);
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en AspiranteResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockAD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.eliminar(id);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD, Mockito.never()).eliminar(Mockito.any());
    }

    @Test
    public void eliminarConExcepcionTest() {
        System.out.println("Ejecutando test: eliminarConExcepcionTest en AspiranteResource");
        UUID id = UUID.randomUUID();
        Aspirante areaExistente = new Aspirante(id);
        Mockito.when(mockAD.buscarPorId(id)).thenReturn(areaExistente);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockAD).eliminar(areaExistente);
        assertThrows(RuntimeException.class, () -> cut.eliminar(id));
        Mockito.verify(mockAD).buscarPorId(id);
        Mockito.verify(mockAD).eliminar(areaExistente);
    }

    @Test
    public void eliminarIdNullTest(){
        System.out.println("Ejecutando test: eliminarIdNullTest en AspiranteResource");
        Response resultado = cut.eliminar(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorRangoExitosoTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorRango(0, 50)).thenReturn(List.of(new Aspirante(UUID.randomUUID()), new Aspirante(UUID.randomUUID())));
        Mockito.when(mockAD.contar()).thenReturn(2L);
        Response resultado = cut.buscarPorRango(0, 50);
        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorRango(0, 50);
        Mockito.verify(mockAD).contar();
    }

    @Test
    public void buscarPorRangoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: buscarPorRangoParametrosInvalidosTest en AspiranteResource");
        Response resultado = cut.buscarPorRango(-1, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoMaxCeroTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxCeroTest en AspiranteResource");
        Response resultado = cut.buscarPorRango(0, 0);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoMaxMayorACincuentaTest() {
        System.out.println("Ejecutando test: buscarPorRangoMaxMayorACincuentaTest en AspiranteResource");
        Response resultado = cut.buscarPorRango(0, 51);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorRangoConExcepcionTest(){
        System.out.println("Ejecutando test: buscarPorRangoConExcepcionTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorRango(0, 50)).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(0, 50));
        Mockito.verify(mockAD).buscarPorRango(0, 50);
    }

    @Test
    public void buscarPorApellidosExitosoTest() {
        System.out.println("Ejecutando test: buscarPorApellidosExitosoTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorApellidos("Lopez"))
                .thenReturn(List.of(new Aspirante(UUID.randomUUID()), new Aspirante(UUID.randomUUID())));

        Response resultado = cut.buscar(null, "Lopez");

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorApellidos("Lopez");
    }

    @Test
    public void buscarPorApellidosNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorApellidosNoEncontradoTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorApellidos("NoExiste")).thenReturn(Collections.emptyList());

        Response resultado = cut.buscar(null, "NoExiste");

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorApellidos("NoExiste");
    }

    @Test
    public void buscarPorApellidosResultadoNullTest() {
        System.out.println("Ejecutando test: buscarPorApellidosResultadoNullTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorApellidos("NoExisteNull")).thenReturn(null);

        Response resultado = cut.buscar(null, "NoExisteNull");

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorApellidos("NoExisteNull");
    }

    @Test
    public void buscarPorApellidosParametroInvalidoTest() {
        System.out.println("Ejecutando test: buscarPorApellidosParametroInvalidoTest en AspiranteResource");
        Response resultadoNull = cut.buscar(null, null);
        Response resultadoBlank = cut.buscar(null, "   ");

        assertEquals(400, resultadoNull.getStatus());
        assertEquals(400, resultadoBlank.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorApellidosConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorApellidosConExcepcionTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorApellidos("Lopez")).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscar(null, "Lopez"));
        Mockito.verify(mockAD).buscarPorApellidos("Lopez");
    }

    @Test
    public void buscarPorCorreoExitosoTest() {
        System.out.println("Ejecutando test: buscarPorCorreoExitosoTest en AspiranteResource");
        Aspirante encontrado = new Aspirante(UUID.randomUUID());
        encontrado.setCorreo("correo@test.com");
        Mockito.when(mockAD.buscarPorCorreo("correo@test.com")).thenReturn(encontrado);

        Response resultado = cut.buscar("correo@test.com", null);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorCorreo("correo@test.com");
        Mockito.verify(mockAD, Mockito.never()).buscarPorApellidos(Mockito.any());
    }

    @Test
    public void buscarPorCorreoNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorCorreoNoEncontradoTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorCorreo("noexiste@test.com")).thenReturn(null);

        Response resultado = cut.buscar("noexiste@test.com", null);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorCorreo("noexiste@test.com");
    }

    @Test
    public void buscarPorCorreoParametroInvalidoTest() {
        System.out.println("Ejecutando test: buscarPorCorreoParametroInvalidoTest en AspiranteResource");
        Response resultadoNull = cut.buscar(null, null);
        Response resultadoBlank = cut.buscar("   ", null);

        assertEquals(400, resultadoNull.getStatus());
        assertEquals(400, resultadoBlank.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void buscarPorCorreoConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorCorreoConExcepcionTest en AspiranteResource");
        Mockito.when(mockAD.buscarPorCorreo("correo@test.com")).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscar("correo@test.com", null));
        Mockito.verify(mockAD).buscarPorCorreo("correo@test.com");
    }

    @Test
    public void buscarPorIdExitosoTest(){
        System.out.println("Ejecutando test: buscarPorIdExitosoTest en AspiranteResource");
        Aspirante encontrado = new Aspirante(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(Mockito.any())).thenReturn(encontrado);
        Response resultado = cut.buscarPorId(UUID.randomUUID());
        assertNotNull(resultado.getEntity());
        assertEquals(200, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(Mockito.any());
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorIdNoEncontradoTest en AspiranteResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockAD.buscarPorId(id)).thenReturn(null);
        Response resultado = cut.buscarPorId(id);
        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdConExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorIdConExcepcionTest en AspiranteResource");
        UUID id = UUID.randomUUID();
        Mockito.when(mockAD.buscarPorId(id)).thenThrow(new RuntimeException("Error en base de datos"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(id));
        Mockito.verify(mockAD).buscarPorId(id);
    }

    @Test
    public void buscarPorIdNullTest() {
        System.out.println("Ejecutando test: buscarPorIdNullTest en AspiranteResource");
        Response resultado = cut.buscarPorId(null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void actualizarExitosoTest() {
        System.out.println("Ejecutando test: actualizarExitosoTest en AspiranteResource");
        Aspirante aspirante = new Aspirante(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(aspirante.getIdAspirante())).thenReturn(aspirante);
        Mockito.when(mockAD.actualizar(aspirante)).thenReturn(aspirante);

        Response resultado = cut.actualizar(aspirante.getIdAspirante(), aspirante);

        assertEquals(200, resultado.getStatus());
        assertNotNull(resultado.getEntity());
        Mockito.verify(mockAD).buscarPorId(aspirante.getIdAspirante());
        Mockito.verify(mockAD).actualizar(aspirante);
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en AspiranteResource");
        Aspirante aspirante = new Aspirante(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(aspirante.getIdAspirante())).thenReturn(null);

        Response resultado = cut.actualizar(aspirante.getIdAspirante(), aspirante);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockAD).buscarPorId(aspirante.getIdAspirante());
        Mockito.verify(mockAD, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarConExcepcionTest() {
        System.out.println("Ejecutando test: actualizarConExcepcionTest en AspiranteResource");
        Aspirante aspirante = new Aspirante(UUID.randomUUID());
        Mockito.when(mockAD.buscarPorId(aspirante.getIdAspirante())).thenReturn(aspirante);
        Mockito.doThrow(new RuntimeException("Error en base de datos"))
                .when(mockAD).actualizar(aspirante);

        assertThrows(RuntimeException.class, () -> cut.actualizar(aspirante.getIdAspirante(), aspirante));
        Mockito.verify(mockAD).buscarPorId(aspirante.getIdAspirante());
        Mockito.verify(mockAD).actualizar(aspirante);
    }

    @Test
    public void actualizarNullTest() {
        System.out.println("Ejecutando test: actualizarNullTest en AspiranteResource");
        Response resultado = cut.actualizar(null, null);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }

    @Test
    public void actualizarSinIdTest() {
        System.out.println("Ejecutando test: actualizarSinIdTest en AspiranteResource");
        Aspirante aspirante = new Aspirante();
        Response resultado = cut.actualizar(aspirante.getIdAspirante(), aspirante);
        assertEquals(400, resultado.getStatus());
        Mockito.verifyNoInteractions(mockAD);
    }
}
