package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractorPK;

public class PreguntaDistractorResourceTest {

    private UriInfo mockUriInfo;
    private PreguntaDistractorDAO pDDAO;
    private DistractorDAO dDAO;
    private PreguntaDAO pDAO;
    private PreguntaDistractorResource cut;

    private UUID idPregunta;
    private UUID idDistractor;

    @BeforeEach
    public void setUp() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        pDDAO = Mockito.mock(PreguntaDistractorDAO.class);
        dDAO = Mockito.mock(DistractorDAO.class);
        pDAO = Mockito.mock(PreguntaDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder())
            .thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build())
                .thenReturn(URI.create("http://localhost:8080/v1/pregunta/1/distractor/1"));

        idPregunta = UUID.randomUUID();
        idDistractor = UUID.randomUUID();

        cut = new PreguntaDistractorResource();
        cut.pDDAO = pDDAO;
        cut.dDAO = dDAO;
        cut.pDAO = pDAO;
    }

    @Test 
    public void crear_PreguntaDistractor_Exitoso(){
    
        PreguntaDistractor pD = new PreguntaDistractor();
        Pregunta p = new Pregunta(idPregunta);
        Distractor d = new Distractor(idDistractor);
        pD.setIdPregunta(p);
        pD.setIdDistractor(d);
        pD.setCorrecto(true);

        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
        Mockito.when(dDAO.buscarPorId(idDistractor)).thenReturn(d);
        Mockito.doNothing().when(pDDAO).crear(pD);

        Response response = cut.crear(idPregunta, pD, mockUriInfo);
        Mockito.verify(pDDAO).crear(pD);
        Mockito.verify(mockUriInfo).getAbsolutePathBuilder();
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_PreguntaNoEncontrada(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Distractor d = new Distractor(idDistractor);
        pD.setIdDistractor(d);
        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(null);
        Mockito.when(dDAO.buscarPorId(idDistractor)).thenReturn(d);  
        Response response = cut.crear(idPregunta, pD, mockUriInfo);
        Mockito.verify(pDAO).buscarPorId(idPregunta);
        Mockito.verify(dDAO).buscarPorId(idDistractor);
        Mockito.verify(pDDAO, Mockito.never()).crear(Mockito.any(PreguntaDistractor.class));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_DistractorNoEncontrado(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Distractor d = new Distractor(idDistractor);
        pD.setIdDistractor(d);
        Pregunta p = new Pregunta(idPregunta);
        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);       
        Mockito.when(dDAO.buscarPorId(idDistractor)).thenReturn(null);
        Response response = cut.crear(idPregunta, pD, mockUriInfo);
        Mockito.verify(pDAO).buscarPorId(idPregunta);
        Mockito.verify(dDAO).buscarPorId(idDistractor);
        Mockito.verify(pDDAO, Mockito.never()).crear(Mockito.any(PreguntaDistractor.class));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_ErrorInterno(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Distractor d = new Distractor(idDistractor);
        pD.setIdDistractor(d);
        Pregunta p = new Pregunta(idPregunta);
        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
        Mockito.when(dDAO.buscarPorId(idDistractor)).thenReturn(d);
        Mockito.doThrow(new RuntimeException("Error de prueba")).when(pDDAO).crear(Mockito.any(PreguntaDistractor.class));
        Response response = cut.crear(idPregunta, pD, mockUriInfo);
        Mockito.verify(pDDAO).crear(Mockito.any(PreguntaDistractor.class));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

     @Test
     public void crear_PreguntaDistractor_BadRequestIdPregunta(){
         PreguntaDistractor pD = new PreguntaDistractor();
         pD.setIdDistractor(new Distractor(idDistractor));
         Response response = cut.crear(null, pD, mockUriInfo);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }
    @Test
    public void crear_PreguntaDistractor_BadRequestIdDistractor(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Response response = cut.crear(idPregunta, pD, mockUriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    @Test
    public void crear_PreguntaDistractor_BadRequestBody(){
        Response response = cut.crear(idPregunta, null, mockUriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }



      @Test
      public void eliminar_PreguntaDistractor_ErrorInterno(){
          PreguntaDistractor pD = new PreguntaDistractor();
          PreguntaDistractorPK pk = new PreguntaDistractorPK(idPregunta, idDistractor);
          Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(pD);
          Mockito.doThrow(new RuntimeException("Error de prueba")).when(pDDAO).eliminar(pD);
          Response response = cut.eliminar(idPregunta, idDistractor);
          Mockito.verify(pDDAO).buscarPorId(pk);
          Mockito.verify(pDDAO).eliminar(pD);
          assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
      }

     @Test
     public void eliminar_PreguntaDistractor_BadRequestIdDistractor(){
         Response response = cut.eliminar(null, idDistractor);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

    @Test
    public void eliminar_PreguntaDistractor_BadRequestIdPregunta(){
        Response response = cut.eliminar(idPregunta, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

     @Test
     public void eliminar_PreguntaDistractor_Exitoso(){
         PreguntaDistractor pD = new PreguntaDistractor();
         PreguntaDistractorPK pk = new PreguntaDistractorPK(idPregunta, idDistractor);
         Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(pD);
         Mockito.doNothing().when(pDDAO).eliminar(pD);
         Response response = cut.eliminar(idPregunta, idDistractor);
         Mockito.verify(pDDAO).buscarPorId(pk);
         Mockito.verify(pDDAO).eliminar(pD);
         assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
     }

     @Test
     public void eliminar_PreguntaDistractor_NotFound(){
         PreguntaDistractorPK pk = new PreguntaDistractorPK(idPregunta, idDistractor);
         Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(null);
         Response response = cut.eliminar(idPregunta, idDistractor);
         Mockito.verify(pDDAO).buscarPorId(pk);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void listar_PreguntaDistractor_Exitoso(){
         PreguntaDistractor pD = new PreguntaDistractor();
         Pregunta p = new Pregunta(idPregunta);
         Distractor d = new Distractor(idDistractor);
         pD.setIdPregunta(p);
         pD.setIdDistractor(d);
         pD.setCorrecto(true);

         Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
         Mockito.when(pDDAO.buscarPorIdPregunta(idPregunta)).thenReturn(List.of(pD, pD));

         Response response = cut.listar(idPregunta);
         Mockito.verify(pDDAO).buscarPorIdPregunta(idPregunta);
         assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
         assertEquals(List.of(pD, pD), response.getEntity());
     }

     @Test
     public void listar_PreguntaDistractor_PreguntaNoEncontrada(){
         Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(null);

         Response response = cut.listar(idPregunta);
         Mockito.verify(pDAO).buscarPorId(idPregunta);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void listar_PreguntaDistractor_NoEncontrado(){
         Pregunta p = new Pregunta(idPregunta);
         Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
         Mockito.when(pDDAO.buscarPorIdPregunta(idPregunta)).thenReturn(List.of());

         Response response = cut.listar(idPregunta);
         Mockito.verify(pDDAO).buscarPorIdPregunta(idPregunta);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

    @Test
    public void listar_PreguntaDistractor_Null(){
        Pregunta p = new Pregunta(idPregunta);
        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
        Mockito.when(pDDAO.buscarPorIdPregunta(idPregunta)).thenReturn(null);

        Response response = cut.listar(idPregunta);
        Mockito.verify(pDDAO).buscarPorIdPregunta(idPregunta);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }


    @Test 
    public void listar_PreguntaDistractor_ErrorInterno(){
        Pregunta p = new Pregunta(idPregunta);
        Mockito.when(pDAO.buscarPorId(idPregunta)).thenReturn(p);
        Mockito.when(pDDAO.buscarPorIdPregunta(idPregunta)).thenThrow(new RuntimeException("Error de prueba"));

        Response response = cut.listar(idPregunta);
        Mockito.verify(pDDAO).buscarPorIdPregunta(idPregunta);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void listar_PreguntaDistractor_BadRequest(){
        Response response = cut.listar(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}