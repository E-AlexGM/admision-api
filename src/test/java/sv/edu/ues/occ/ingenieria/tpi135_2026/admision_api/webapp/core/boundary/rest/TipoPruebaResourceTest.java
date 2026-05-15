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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

public class TipoPruebaResourceTest {

    TipoPruebaDAO mockTpDAO;
    TipoPruebaResource cut; // Class Under Test
    UriInfo mockUriInfo;
    UriBuilder mockUriBuilder;


    @BeforeEach
    public void setUp() {
        mockTpDAO = Mockito.mock(TipoPruebaDAO.class);
        cut = new TipoPruebaResource();
        cut.tpDAO = mockTpDAO; // Inyectar el mock en la clase bajo prueba
        mockUriInfo = Mockito.mock(UriInfo.class);
        mockUriBuilder = Mockito.mock(UriBuilder.class);
          
    }
    
    @Test 
    public void crear_TipoPrueba_Exitoso(){
        System.out.println("crear_TipoPrueba_Exitoso");
        // Configurar el mock para simular la creación exitosa
        TipoPrueba tp = new TipoPrueba();

        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.doNothing().when(mockTpDAO).crear(tp);


        Mockito.when(mockUriBuilder.build()).thenReturn(URI.create("http://localhost:8080/v1/tipo_prueba/"));
        // Llamar al método crear
        Response response = cut.crear(tp, mockUriInfo);

        // Verificar que se haya llamado al método crear del DAO
        Mockito.verify(mockTpDAO).crear(tp);

        // Verificar que la respuesta sea CREATED
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());
    }

    @Test
    public void crear_TipoPrueba_ErrorInterno(){
        System.out.println("crear_TipoPrueba_ErrorInterno");
        TipoPrueba tp = new TipoPrueba();
        Mockito.doThrow(new RuntimeException("Error interno")).when(mockTpDAO).crear(tp);
        assertThrows(RuntimeException.class, () -> cut.crear(tp, mockUriInfo));
    }

    @Test 
    public void crear_TipoPrueba_BadRequest(){
        System.out.println("crear_TipoPrueba_BadRequest");
        // Llamar al método crear con un TipoPrueba nulo
        Response response = cut.crear(null, mockUriInfo);

        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }   

    @Test 
    public void actualizar_TipoPrueba_Exitoso(){
        System.out.println("actualizar_TipoPrueba_Exitoso");
        // Configurar el mock para simular la existencia del TipoPrueba
        UUID idTipoPrueba = UUID.randomUUID();
        TipoPrueba tpExistente = new TipoPrueba();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenReturn(tpExistente);

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método actualizar
        TipoPrueba tpActualizado = new TipoPrueba();
        Response response = cut.actualizar(tpActualizado, idTipoPrueba);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockTpDAO).buscarPorId(idTipoPrueba);
        Mockito.verify(mockTpDAO).actualizar(tpActualizado);

        // Verificar que la respuesta sea OK
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(tpActualizado, response.getEntity());
    }

    @Test 
    public void actualizar_TipoPrueba_NoEncontrado(){
        System.out.println("actualizar_TipoPrueba_NoEncontrado");
        // Configurar el mock para simular la no existencia del TipoPrueba
        UUID idTipoPrueba = UUID.randomUUID();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenReturn(null);

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método actualizar
        TipoPrueba tpActualizado = new TipoPrueba();
        Response response = cut.actualizar(tpActualizado, idTipoPrueba);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockTpDAO).buscarPorId(idTipoPrueba);

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void actualizar_TipoPrueba_ErrorInterno(){
        System.out.println("actualizar_TipoPrueba_ErrorInterno");
        UUID idTipoPrueba = UUID.randomUUID();
        TipoPrueba tpActualizado = new TipoPrueba();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.actualizar(tpActualizado, idTipoPrueba));
    }

    @Test 
    public void actualizar_TipoPrueba_BadRequest_IdTipoPrueba(){
        System.out.println("actualizar_TipoPrueba_BadRequest");
        UUID idTipoPrueba = UUID.randomUUID();
        Response response = cut.actualizar(new TipoPrueba(), null);
        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void actualizar_TipoPrueba_BadRequest_Tp(){
        System.out.println("actualizar_TipoPrueba_BadRequest");
        // Llamar al método actualizar con un TipoPrueba nulo
        UUID idTipoPrueba = UUID.randomUUID();
        Response response = cut.actualizar(null, idTipoPrueba);
        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test 
    public void buscarPorRango_TipoPrueba_Exitoso(){
        System.out.println("buscarPorRango_TipoPrueba_Exitoso");
        // Configurar el mock para simular la búsqueda exitosa
        int first = 0;
        int max = 10;
        Mockito.when(mockTpDAO.buscarPorRango(first, max)).thenReturn(List.of(new TipoPrueba(), new TipoPrueba()));

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método buscarPorRango
        Response response = cut.buscarPorRango(first, max);

        // Verificar que se haya llamado al método buscarPorRango del DAO
        Mockito.verify(mockTpDAO).buscarPorRango(first, max);

        // Verificar que la respuesta sea OK
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test 
    public void buscarPorRango_TipoPrueba_ParametrosInvalidos_Min(){
        System.out.println("buscarPorRango_TipoPrueba_ParametrosInvalidos");
        // Llamar al método buscarPorRango con parámetros inválidos
        int first = -1;
        int max = 10;
        Response response = cut.buscarPorRango(first, max);

        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }


    @Test
    public void buscarPorRango_TipoPrueba_ParametrosInvalidos_Max(){
        System.out.println("buscarPorRango_TipoPrueba_ParametrosInvalidos");
        // Llamar al método buscarPorRango con parámetros inválidos
        int first = 1;
        int max = -6;
        Response response = cut.buscarPorRango(first, max);

        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test 
    public void buscarPorRango_TipoPrueba_NoEncontrado(){
        System.out.println("buscarPorRango_TipoPrueba_NoEncontrado");
        // Configurar el mock para simular la búsqueda sin resultados
        int first = 0;
        int max = 10;
        Mockito.when(mockTpDAO.buscarPorRango(first, max)).thenReturn(null);

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método buscarPorRango
        Response response = cut.buscarPorRango(first, max);

        // Verificar que se haya llamado al método buscarPorRango del DAO
        Mockito.verify(mockTpDAO).buscarPorRango(first, max);

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_TipoPrueba_Vacio(){
        System.out.println("buscarPorRango_TipoPrueba_Vacio");
        // Configurar el mock para simular la búsqueda vacía
        int first = 0;
        int max = 10;
        Mockito.when(mockTpDAO.buscarPorRango(first, max)).thenReturn(List.of());

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método buscarPorRango
        Response response = cut.buscarPorRango(first, max);

        // Verificar que se haya llamado al método buscarPorRango del DAO
        Mockito.verify(mockTpDAO).buscarPorRango(first, max);

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void buscarPorRango_TipoPrueba_ErrorInterno(){
        System.out.println("buscarPorRango_TipoPrueba_ErrorInterno");
        int first = 0;
        int max = 10;
        Mockito.when(mockTpDAO.buscarPorRango(first, max)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(first, max));
    }

    @Test
    public void eliminar_TipoPrueba_Exitoso(){
        System.out.println("eliminar_TipoPrueba_Exitoso");
        // Configurar el mock para simular la existencia del TipoPrueba
        UUID idTipoPrueba = UUID.randomUUID();

        TipoPrueba tpExistente = new TipoPrueba();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenReturn(tpExistente);

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método eliminar
        Response response = cut.eliminar(idTipoPrueba);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockTpDAO).buscarPorId(idTipoPrueba);
        Mockito.verify(mockTpDAO).eliminar(tpExistente);

        // Verificar que la respuesta sea NO_CONTENT
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_TipoPrueba_NoEncontrado(){
        System.out.println("eliminar_TipoPrueba_NoEncontrado");
        // Configurar el mock para simular la no existencia del TipoPrueba
        UUID idTipoPrueba = UUID.randomUUID();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenReturn(null);

        // Crear una instancia de TipoPruebaResource y asignar el mock
        cut.tpDAO = mockTpDAO;

        // Llamar al método eliminar
        Response response = cut.eliminar(idTipoPrueba);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockTpDAO).buscarPorId(idTipoPrueba);
        

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_TipoPrueba_ErrorInterno(){
        System.out.println("eliminar_TipoPrueba_ErrorInterno");
        UUID idTipoPrueba = UUID.randomUUID();
        TipoPrueba tpExistente = new TipoPrueba();
        Mockito.when(mockTpDAO.buscarPorId(idTipoPrueba)).thenReturn(tpExistente);
        Mockito.doThrow(new RuntimeException("Error interno")).when(mockTpDAO).eliminar(tpExistente);
        assertThrows(RuntimeException.class, () -> cut.eliminar(idTipoPrueba));
    }

    @Test
    public void eliminar_TipoPrueba_BadRequest(){
        System.out.println("eliminar_TipoPrueba_BadRequest");
        // Llamar al método eliminar con un ID nulo
        Response response = cut.eliminar(null);
        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }


}
