package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;

public class PruebaClaveResourceTest {

    PruebaClaveResource cut;
    PruebaDAO pDAO;
    PruebaClaveDAO pCDAO;

    private UriInfo mockUriInfo;
    private AreaDAO mockAD;

    @BeforeEach
    public void setUp() {

        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockAD = Mockito.mock(AreaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString()))
                .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/area/1"));

        cut = new PruebaClaveResource();
        pDAO = Mockito.mock(PruebaDAO.class);
        pCDAO = Mockito.mock(PruebaClaveDAO.class);
        cut.pDAO = pDAO;
        cut.pCDAO = pCDAO;
    }

    @Test
    public void test_PruebaClave_asignarClave() {
        Prueba p = new Prueba(UUID.randomUUID());
        PruebaClave pC = new PruebaClave();
        pC.setIdPrueba(null);

        Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
        Mockito.doNothing().when(pCDAO).crear(Mockito.any(PruebaClave.class));
        Response respuesta = cut.asignarClave(p.getIdPrueba(), pC, mockUriInfo);
        assertEquals(respuesta.getStatus(), Response.Status.CREATED.getStatusCode());
    }

     
    @Test
    public void test_PruebaClave_asignarClave_pruebaNoEncontrada() {
        PruebaClave pC = new PruebaClave();
        pC.setIdPrueba(null);
        Mockito.when(pDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(null);
        Response respuesta = cut.asignarClave(UUID.randomUUID(), pC, mockUriInfo);
        assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

      @Test
      public void test_PruebaClave_asignarClave_badRequest() {
          Response respuesta = cut.asignarClave(null, null, mockUriInfo);
          assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_asignarClave_idPruebaNullBadRequest() {
         PruebaClave pC = new PruebaClave();
         Response respuesta = cut.asignarClave(null, pC, mockUriInfo);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_asignarClave_pCNullBadRequest() {
         Response respuesta = cut.asignarClave(UUID.randomUUID(), null, mockUriInfo);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_asignarClave_internalServerError() {
         Prueba p = new Prueba(UUID.randomUUID());
         PruebaClave pC = new PruebaClave();
         pC.setIdPrueba(null);

         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.doThrow(new RuntimeException("Error al crear la clave")).when(pCDAO).crear(Mockito.any(PruebaClave.class));
         assertThrows(RuntimeException.class, () -> cut.asignarClave(p.getIdPrueba(), pC, mockUriInfo));
    }

    @Test 
    public void test_PruebaClave_listarClaves() {
        Prueba p = new Prueba(UUID.randomUUID());
        PruebaClave pC = new PruebaClave();
        pC.setIdPrueba(p);
        Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
        Mockito.when(pCDAO.listarClavesPorPrueba(Mockito.any(UUID.class))).thenReturn(List.of(pC));
        Response respuesta = cut.listarClaves(p.getIdPrueba());
        assertEquals(respuesta.getStatus(), Response.Status.OK.getStatusCode());
    }

     @Test
     public void test_PruebaClave_listarClaves_NoEncontrado() {
         Prueba p = new Prueba(UUID.randomUUID());
         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.when(pCDAO.listarClavesPorPrueba(Mockito.any(UUID.class))).thenReturn(null);
         Response respuesta = cut.listarClaves(p.getIdPrueba());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_listarClaves_ListaVacia() {
         Prueba p = new Prueba(UUID.randomUUID());
         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.when(pCDAO.listarClavesPorPrueba(Mockito.any(UUID.class))).thenReturn(List.of());
         Response respuesta = cut.listarClaves(p.getIdPrueba());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_listarClaves_PruebaNoEncontrada() {
         Mockito.when(pDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(null);
         Response respuesta = cut.listarClaves(UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_listarClaves_BadRequest() {
         Response respuesta = cut.listarClaves(null);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

     @Test
     public void test_PruebaClave_listarClaves_InternalServerError() {
         Prueba p = new Prueba(UUID.randomUUID());
         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.doThrow(new RuntimeException("Error al listar las claves")).when(pCDAO).listarClavesPorPrueba(Mockito.any(UUID.class));
         assertThrows(RuntimeException.class, () -> cut.listarClaves(p.getIdPrueba()));
    }

    @Test
    public void test_PruebaClave_eliminarClave() {
            Prueba p = new Prueba(UUID.randomUUID());
            PruebaClave pC = new PruebaClave();
            pC.setIdPrueba(p);
            Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
            Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(pC);
            Mockito.doNothing().when(pCDAO).eliminar(Mockito.any(PruebaClave.class));
            Response respuesta = cut.eliminarClave(p.getIdPrueba(), UUID.randomUUID());
            assertEquals(respuesta.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
    }

     @Test
     public void test_PruebaClave_eliminarClave_ClaveNoEncontrada() {
         Prueba p = new Prueba(UUID.randomUUID());
         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(null);
         Response respuesta = cut.eliminarClave(p.getIdPrueba(), UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_eliminarClave_IdsPruebaNoConcuerdan() {
         Prueba p = new Prueba(UUID.randomUUID());
         Prueba p2 = new Prueba(UUID.randomUUID());
         PruebaClave pC = new PruebaClave();
         pC.setIdPrueba(p2);
         Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
         Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(pC);
         Response respuesta = cut.eliminarClave(p.getIdPrueba(), UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_eliminarClave_IdPruebaNull() {
         Response respuesta = cut.eliminarClave(null, UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_eliminarClave_IdClaveNull() {
         Response respuesta = cut.eliminarClave(UUID.randomUUID(), null);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

    @Test
    public void test_PruebaClave_eliminarClave_BadRequest() {
        Response respuesta = cut.eliminarClave(null, null);
        assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test 
    public void test_PruebaClave_eliminarClave_InternalServerError() {
        Prueba p = new Prueba(UUID.randomUUID());
        PruebaClave pC = new PruebaClave();
        pC.setIdPrueba(p);
        Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
        Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(pC);
        Mockito.doThrow(new RuntimeException("Error al eliminar la clave")).when(pCDAO).eliminar(Mockito.any(PruebaClave.class));
        assertThrows(RuntimeException.class, () -> cut.eliminarClave(p.getIdPrueba(), UUID.randomUUID()));
    }

    @Test 
    public void test_PruebaClave_eliminarClave_PruebaNoEncontrada() {
        Mockito.when(pDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(null);
        Response respuesta = cut.eliminarClave(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

     @Test
     public void test_PruebaClave_buscarPorId(){

         PruebaClave pC = new PruebaClave(UUID.randomUUID());
         Mockito.when(pCDAO.buscarPorId(pC.getIdPruebaClave())).thenReturn(pC);
         Response respuesta = cut.buscarPorId(UUID.randomUUID(), pC.getIdPruebaClave());
         assertEquals(respuesta.getStatus(), Response.Status.OK.getStatusCode());

     }

     @Test
     public void test_PruebaClave_buscarPorId_NoEncontrada(){
         Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenReturn(null);
         Response respuesta = cut.buscarPorId(UUID.randomUUID(), UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
     }

     @Test
     public void test_PruebaClave_buscarPorId_BadRequest_IdPruebaNull(){
         Response respuesta = cut.buscarPorId(null, UUID.randomUUID());
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_buscarPorId_BadRequest_IdClaveNull(){
         Response respuesta = cut.buscarPorId(UUID.randomUUID(), null);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_buscarPorId_BadRequest_AmbosNull(){
         Response respuesta = cut.buscarPorId(null, null);
         assertEquals(respuesta.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
     }

     @Test
     public void test_PruebaClave_buscarPorId_ErrorInterno(){
         Mockito.when(pCDAO.buscarPorId(Mockito.any(UUID.class))).thenThrow(new RuntimeException("Error al buscar la clave"));
         assertThrows(RuntimeException.class, () -> cut.buscarPorId(UUID.randomUUID(), UUID.randomUUID()));
     }

 }
