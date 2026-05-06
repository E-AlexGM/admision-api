package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;

public class PruebaResourceTest {

    PruebaDAO pDAO;
    TipoPruebaDAO tpDAO;
    PruebaResource cut;
    UriInfo uriInfo;
    UriBuilder uriBuilder;

    @BeforeEach
    public void setUp() {
        // Configurar el entorno de prueba, como inicializar objetos necesarios
        pDAO = Mockito.mock(PruebaDAO.class);
        cut = new PruebaResource();
        cut.pDAO = pDAO; // Inyectar el mock en el recurso
        uriInfo = Mockito.mock(UriInfo.class);
        uriBuilder = Mockito.mock(UriBuilder.class);
       
    }

    @Test 
    public void crear_Prueba_Exitoso(){
        System.out.println("EjecutandoTest: crear_Prueba_Exitoso");
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.build()).thenReturn(java.net.URI.create("http://localhost/prueba/1"));
        Prueba p = new Prueba();
        Response response = cut.crear(p, uriInfo);
        Mockito.verify(pDAO).crear(p);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_Prueba_ErrorInterno(){
        System.out.println("EjecutandoTest: crear_Prueba_ErrorInterno");    
        cut.pDAO = null;
        Prueba p = new Prueba();
        Response response = cut.crear(p, uriInfo);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
    
    @Test 
    public void crear_Prueba_BadRequest(){
        System.out.println("EjecutandoTest: crear_Prueba_BadRequest");
        Response response = cut.crear(null, uriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_Prueba_Exitoso(){
        System.out.println("EjecutandoTest: eliminar_Prueba_Exitoso");
        UUID idPrueba = UUID.randomUUID();
        Prueba prueba = new Prueba();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(prueba);
        cut.eliminar(idPrueba);
        Mockito.verify(pDAO).eliminar(prueba);
        Response response = cut.eliminar(idPrueba);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test 
    public void eliminar_Prueba_NoEncontrada(){
        System.out.println("EjecutandoTest: eliminar_Prueba_NoEncontrada");
        UUID idPrueba = UUID.randomUUID();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null);
        Response response = cut.eliminar(idPrueba);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void eliminar_Prueba_ErrorInterno(){
        System.out.println("EjecutandoTest: eliminar_Prueba_ErrorInterno");
        UUID idPrueba = UUID.randomUUID();
        Prueba prueba = new Prueba();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(prueba);
        Mockito.doThrow(new RuntimeException()).when(pDAO).eliminar(prueba);
        Response response = cut.eliminar(idPrueba);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test 
    public void eliminar_Prueba_BadRequest(){
        System.out.println("EjecutandoTest: eliminar_Prueba_BadRequest");
        Response response = cut.eliminar(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test 
    public void actualizar_Prueba_Exitoso(){
        System.out.println("EjecutandoTest: actualizar_Prueba_Exitoso");
        UUID idPrueba = UUID.randomUUID();
        Prueba pruebaExistente = new Prueba();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(pruebaExistente);
        Prueba p = new Prueba();
        Response response = cut.actualizar(idPrueba, p);
        Mockito.verify(pDAO).actualizar(p);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test 
    public void actualizar_Prueba_NoEncontrada(){
        System.out.println("EjecutandoTest: actualizar_Prueba_NoEncontrada");
        UUID idPrueba = UUID.randomUUID();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null);
        Prueba p = new Prueba();
        Response response = cut.actualizar(idPrueba, p);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());  
    }

    @Test 
    public void actualizar_Prueba_ErrorInterno(){
        System.out.println("EjecutandoTest: actualizar_Prueba_ErrorInterno");
        UUID idPrueba = UUID.randomUUID();
        Prueba pruebaExistente = new Prueba();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(pruebaExistente);
        Prueba p = new Prueba();
        Mockito.doThrow(new RuntimeException()).when(pDAO).actualizar(p);
        Response response = cut.actualizar(idPrueba, p);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test 
    public void actualizar_Prueba_BadRequest(){
        System.out.println("EjecutandoTest: actualizar_Prueba_BadRequest");
        UUID idPrueba = UUID.randomUUID();
        Response response = cut.actualizar(idPrueba, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());    
    }

    @Test 
    public void buscarPorId_Prueba_Exitoso(){
        System.out.println("EjecutandoTest: buscarPorId_Prueba_Exitoso");
        UUID idPrueba = UUID.randomUUID();
        Prueba prueba = new Prueba();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(prueba);
        Response response = cut.buscarPorId(idPrueba);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(prueba, response.getEntity());
    }

    @Test 
    public void buscarPorId_Prueba_NoEncontrada(){
        System.out.println("EjecutandoTest: buscarPorId_Prueba_NoEncontrada");
        UUID idPrueba = UUID.randomUUID();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null);
        Response response = cut.buscarPorId(idPrueba);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void buscarPorId_Prueba_ErrorInterno(){
        System.out.println("EjecutandoTest: buscarPorId_Prueba_ErrorInterno");
        UUID idPrueba = UUID.randomUUID();
        Mockito.when(pDAO.buscarPorId(idPrueba)).thenThrow(new RuntimeException());
        Response response = cut.buscarPorId(idPrueba);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());  
    }

    @Test 
    public void buscarPorId_Prueba_BadRequest(){
        System.out.println("EjecutandoTest: buscarPorId_Prueba_BadRequest");
        Response response = cut.buscarPorId(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());        
    }

     @Test
     public void buscarPorRango_Prueba_Exitoso(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_Exitoso");
         int first = 0;
         int max = 10;
         List<Prueba> pruebas = List.of(new Prueba(), new Prueba());
         Mockito.when(pDAO.buscarPorRango(first, max)).thenReturn(pruebas);
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
         assertEquals(pruebas, response.getEntity());
     }

     @Test
     public void buscarPorRango_Prueba_NoEncontrada(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_NoEncontrada");
         int first = 0;
         int max = 10;
         List<Prueba> pruebas = List.of();
         Mockito.when(pDAO.buscarPorRango(first, max)).thenReturn(pruebas);
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Prueba_Null(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_Null");
         int first = 0;
         int max = 10;
         Mockito.when(pDAO.buscarPorRango(first, max)).thenReturn(null);
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Prueba_ErrorInterno(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_ErrorInterno");
         int first = 0;
         int max = 10;
         Mockito.when(pDAO.buscarPorRango(first, max)).thenThrow(new RuntimeException());
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Prueba_BadRequest_FirstNegativo(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_BadRequest_FirstNegativo");
         int first = -1;
         int max = 10;
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Prueba_BadRequest_MaxCero(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_BadRequest_MaxCero");
         int first = 0;
         int max = 0;
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Prueba_BadRequest_MaxNegativo(){
         System.out.println("EjecutandoTest: buscarPorRango_Prueba_BadRequest_MaxNegativo");
         int first = 0;
         int max = -5;
         Response response = cut.buscarPorRango(first, max);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

 }
