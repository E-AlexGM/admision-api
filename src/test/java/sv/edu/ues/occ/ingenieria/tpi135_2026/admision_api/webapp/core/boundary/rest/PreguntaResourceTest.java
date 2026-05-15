package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;

public class PreguntaResourceTest {

    PreguntaResource cut; // Class Under Test
    PreguntaDAO mockPDAO;
    UriInfo mockUriInfo;
    UriBuilder mockUriBuilder;   

    @BeforeEach
    public void setUp() {
        cut = new PreguntaResource();
        mockPDAO = Mockito.mock(PreguntaDAO.class);
        cut.pDAO = mockPDAO; // Inyectar el mock en la clase bajo prueba
        mockUriInfo = Mockito.mock(UriInfo.class);
        mockUriBuilder = Mockito.mock(UriBuilder.class);
        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
    }

    @Test
    public void crear_Pregunta_Exitoso(){
        System.out.println("Ejecutando Test: crear_Pregunta_Exitoso");
        Pregunta nuevaPregunta = new Pregunta();
        Mockito.doNothing().when(mockPDAO).crear(nuevaPregunta);

        Response response = cut.crear(nuevaPregunta, mockUriInfo);

        Mockito.verify(mockPDAO).crear(nuevaPregunta);
        Mockito.verify(mockUriInfo).getAbsolutePathBuilder();
        Mockito.verify(mockUriBuilder).path(Mockito.anyString());
        Mockito.verify(mockUriBuilder).build();
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void crear_Pregunta_BadRequest(){
        System.out.println("Ejecutando Test: crear_Pregunta_BadRequest");   
        Response response = cut.crear(null, mockUriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void crear_Pregunta_ErrorInterno(){
        System.out.println("Ejecutando Test: crear_Pregunta_ErrorInterno");
        Pregunta nuevaPregunta = new Pregunta();
        Mockito.doThrow(new RuntimeException("Error interno")).when(mockPDAO).crear(nuevaPregunta);
        assertThrows(RuntimeException.class, () -> cut.crear(nuevaPregunta, mockUriInfo));
    }

    @Test 
    public void buscarPorId_Pregunta_Encontrado(){
        System.out.println("Ejecutando Test: buscarPorId_Pregunta_Encontrado");
        // Configurar el mock para simular la existencia de la Pregunta
        UUID idPregunta = UUID.randomUUID();
        Pregunta pExistente = new Pregunta();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(pExistente);

        // Llamar al método buscarPorId
        Response response = cut.buscarPorId(idPregunta);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);

        // Verificar que la respuesta sea OK y contenga la Pregunta
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(pExistente, response.getEntity());
    }

    @Test
    public void buscarPorId_Pregunta_NoEncontrado(){
        System.out.println("Ejecutando Test: buscarPorId_Pregunta_NoEncontrado");
        UUID idPregunta = UUID.randomUUID();
        // Configurar el mock para simular la no existencia de la Pregunta
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(null);

        // Llamar al método buscarPorId
        Response response = cut.buscarPorId(idPregunta);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorId_Pregunta_ErrorInterno(){
        System.out.println("Ejecutando Test: buscarPorId_Pregunta_ErrorInterno");
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idPregunta));
    }

    @Test
    public void buscarPorId_TipoPrueba_BadRequest(){
        System.out.println("Ejecutando Test: buscarPorId_TipoPrueba_BadRequest");
        // Llamar al método buscarPorId con un ID nulo
        Response response = cut.buscarPorId(null);

        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void actualizar_Pregunta_Exitoso(){
        System.out.println("Ejecutando Test: actualizar_Pregunta_Exitoso");
        // Configurar el mock para simular la existencia de la Pregunta
        UUID idPregunta = UUID.randomUUID();
        Pregunta pExistente = new Pregunta();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(pExistente);

        Response response = cut.actualizar(pExistente, idPregunta);
        // Verificar que se haya llamado al método buscarPorId del DAO  
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);
        Mockito.verify(mockPDAO).actualizar(pExistente);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test 
    public void actualizar_Pregunta_NoEncontrado(){
        System.out.println("Ejecutando Test: actualizar_Pregunta_NoEncontrado");
        UUID idPregunta = UUID.randomUUID();
        // Configurar el mock para simular la no existencia de la Pregunta
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(null);

        Response response = cut.actualizar(new Pregunta(), idPregunta);
        // Verificar que se haya llamado al método buscarPorId del DAO  
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void actualizar_Pregunta_ErrorInterno(){
        System.out.println("Ejecutando Test: actualizar_Pregunta_ErrorInterno");
        UUID idPregunta = UUID.randomUUID();
        Pregunta pExistente = new Pregunta();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.actualizar(pExistente, idPregunta));
    }

     @Test
     public void actualizar_TipoPrueba_BadRequest(){
         System.out.println("Ejecutando Test: actualizar_TipoPrueba_BadRequest");
         // Llamar al método actualizar con un ID nulo
         Response response = cut.actualizar(new Pregunta(), null);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void actualizar_Pregunta_BadRequest_PreguntaNull(){
         System.out.println("Ejecutando Test: actualizar_Pregunta_BadRequest_PreguntaNull");
         // Llamar al método actualizar con Pregunta nula
         Response response = cut.actualizar(null, UUID.randomUUID());
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void actualizar_Pregunta_BadRequest_AmbosNull(){
         System.out.println("Ejecutando Test: actualizar_Pregunta_BadRequest_AmbosNull");
         // Llamar al método actualizar con ambos parámetros nulos
         Response response = cut.actualizar(null, null);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

    @Test
    public void eliminar_Pregunta_Exitoso(){
        System.out.println("Ejecutando Test: eliminar_Pregunta_Exitoso");
        // Configurar el mock para simular la existencia de la Pregunta
        UUID idPregunta = UUID.randomUUID();

        Pregunta pExistente = new Pregunta();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(pExistente);

        // Llamar al método eliminar
        Response response = cut.eliminar(idPregunta);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);
        Mockito.verify(mockPDAO).eliminar(pExistente);

        // Verificar que la respuesta sea NO_CONTENT
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_Pregunta_NoEncontrado(){
        System.out.println("eliminar_Pregunta_NoEncontrado");
        UUID idPregunta = UUID.randomUUID();
        // Configurar el mock para simular la no existencia de la Pregunta
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenReturn(null);

        // Crear una instancia de PreguntaResource y asignar el mock
        cut.pDAO = mockPDAO;

        // Llamar al método eliminar
        Response response = cut.eliminar(idPregunta);

        // Verificar que se haya llamado al método buscarPorId del DAO
        Mockito.verify(mockPDAO).buscarPorId(idPregunta);

        // Verificar que la respuesta sea NOT_FOUND
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_Pregunta_ErrorInterno(){
        System.out.println("eliminar_Pregunta_ErrorInterno");
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.eliminar(idPregunta));
    }

    @Test
    public void eliminar_TipoPrueba_BadRequest(){
        System.out.println("eliminar_TipoPrueba_BadRequest");
        // Llamar al método eliminar con un ID nulo
        Response response = cut.eliminar(null);
        // Verificar que la respuesta sea BAD_REQUEST
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_Preguntas_Exitoso(){
        System.out.println("Ejecutando Test: buscarPorRango_Preguntas_Exitoso");
        // Configurar el mock para simular la existencia de Preguntas
        int first = 0;
        int size = 50;
        Mockito.when(mockPDAO.buscarPorRango(first, size)).thenReturn(List.of(new Pregunta(), new Pregunta()));
        Response response = cut.buscarPorRango(first, size);
        Mockito.verify(mockPDAO).buscarPorRango(first, size);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()); 
    }

     @Test
     public void buscarPorRango_Preguntas_NoEncontrado(){
         System.out.println("Ejecutando Test: buscarPorRango_Preguntas_NoEncontrADO");
         // Configurar el mock para simular la no existencia de Preguntas
         int first = 0;
         int size = 50;
         Mockito.when(mockPDAO.buscarPorRango(first, size)).thenReturn(List.of());
         Response response = cut.buscarPorRango(first, size);
         Mockito.verify(mockPDAO).buscarPorRango(first, size);
         assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Preguntas_Null(){
         System.out.println("Ejecutando Test: buscarPorRango_Preguntas_Null");
         // Configurar el mock para retornar null
         int first = 0;
         int size = 50;
         Mockito.when(mockPDAO.buscarPorRango(first, size)).thenReturn(null);
         Response response = cut.buscarPorRango(first, size);
         Mockito.verify(mockPDAO).buscarPorRango(first, size);
         assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_TipoPrueba_BadRequest(){
         System.out.println("Ejecutando Test: buscarPorRango_TipoPrueba_BadRequest");
         // Llamar al método buscarPorRango con parámetros inválidos
         Response response = cut.buscarPorRango(-1, -10);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_BadRequest_FirstNegativo(){
         System.out.println("Ejecutando Test: buscarPorRango_BadRequest_FirstNegativo");
         // Llamar al método buscarPorRango con first negativo
         Response response = cut.buscarPorRango(-1, 50);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_BadRequest_SizeCero(){
         System.out.println("Ejecutando Test: buscarPorRango_BadRequest_SizeCero");
         // Llamar al método buscarPorRango con size = 0
         Response response = cut.buscarPorRango(0, 0);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_BadRequest_SizeNegativo(){
         System.out.println("Ejecutando Test: buscarPorRango_BadRequest_SizeNegativo");
         // Llamar al método buscarPorRango con size negativo
         Response response = cut.buscarPorRango(0, -5);
         // Verificar que la respuesta sea BAD_REQUEST
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

     @Test
     public void buscarPorRango_Preguntas_ErrorInterno(){
         System.out.println("Ejecutando Test: buscarPorRango_Preguntas_ErrorInterno");
         int first = 0;
         int size = 50;
         Mockito.when(mockPDAO.buscarPorRango(first, size)).thenThrow(new RuntimeException("Error interno"));
         assertThrows(RuntimeException.class, () -> cut.buscarPorRango(first, size));
     }

}