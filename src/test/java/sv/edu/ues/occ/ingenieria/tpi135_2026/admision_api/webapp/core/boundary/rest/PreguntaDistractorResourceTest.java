package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.DistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDistractorDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractorPK;

public class PreguntaDistractorResourceTest {

    PreguntaDistractorResource cut;
    PreguntaDAO pDAO;
    DistractorDAO dDAO;
    PreguntaDistractorDAO pDDAO;
    Pregunta p;
    Distractor d;

    @BeforeEach
    public void setUp(){
        pDAO = Mockito.mock(PreguntaDAO.class);
        dDAO = Mockito.mock(DistractorDAO.class);
        pDDAO = Mockito.mock(PreguntaDistractorDAO.class);
        cut = new PreguntaDistractorResource();
        cut.dDAO = dDAO;
        cut.pDAO = pDAO;
        cut.pDDAO = pDDAO;
            p = new Pregunta(UUID.randomUUID());
            d = new Distractor(UUID.randomUUID());
    }

    @Test 
    public void crear_PreguntaDistractor_Exitoso(){
    
        PreguntaDistractor pD = new PreguntaDistractor();
        pD.setIdPregunta(p);
        pD.setIdDistractor(d);
        pD.setCorrecto(true);

        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
        Mockito.when(dDAO.buscarPorId(d.getIdDistractor())).thenReturn(d);
        Mockito.doNothing().when(pDDAO).crear(pD);

        Response response = cut.crear(p.getIdPregunta(), d.getIdDistractor(), pD);
        Mockito.verify(pDDAO).crear(pD);
        Assertions.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_PreguntaNoEncontrada(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(null);
        Mockito.when(dDAO.buscarPorId(d.getIdDistractor())).thenReturn(d);  
        Response response = cut.crear(p.getIdPregunta(), d.getIdDistractor(), pD);
        Mockito.verify(pDAO).buscarPorId(p.getIdPregunta());
        Mockito.verify(dDAO).buscarPorId(d.getIdDistractor());
        Mockito.verify(pDDAO, Mockito.never()).crear(Mockito.any(PreguntaDistractor.class));
        Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_DistractorNoEncontrado(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);       
        Mockito.when(dDAO.buscarPorId(d.getIdDistractor())).thenReturn(null);
        Response response = cut.crear(p.getIdPregunta(), d.getIdDistractor(), pD);
        Mockito.verify(pDAO).buscarPorId(p.getIdPregunta());
        Mockito.verify(dDAO).buscarPorId(d.getIdDistractor());
        Mockito.verify(pDDAO, Mockito.never()).crear(Mockito.any(PreguntaDistractor.class));
        Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test 
    public void crear_PreguntaDistractor_ErrorInterno(){
        PreguntaDistractor pD = new PreguntaDistractor();
        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
        Mockito.when(dDAO.buscarPorId(d.getIdDistractor())).thenReturn(d);
        Mockito.doThrow(new RuntimeException("Error de prueba")).when(pDDAO).crear(Mockito.any(PreguntaDistractor.class));
        Response response = cut.crear(p.getIdPregunta(), d.getIdDistractor(), pD);
        Mockito.verify(pDDAO).crear(Mockito.any(PreguntaDistractor.class));
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

     @Test
     public void crear_PreguntaDistractor_BadRequestIdPregunta(){
         Response response = cut.crear(null, UUID.randomUUID(), new PreguntaDistractor());
         Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }
    @Test
    public void crear_PreguntaDistractor_BadRequestIdDistractor(){
        Response response = cut.crear(UUID.randomUUID(), null, new PreguntaDistractor());
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    @Test
    public void crear_PreguntaDistractor_BadRequestBody(){
        Response response = cut.crear(UUID.randomUUID(), UUID.randomUUID(), null);
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }



      @Test
      public void eliminar_PreguntaDistractor_ErrorInterno(){
          PreguntaDistractor pD = new PreguntaDistractor();
          PreguntaDistractorPK pk = new PreguntaDistractorPK(p.getIdPregunta(), d.getIdDistractor());
          Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(pD);
          Mockito.doThrow(new RuntimeException("Error de prueba")).when(pDDAO).eliminar(pD);
          Response response = cut.eliminar(p.getIdPregunta(), d.getIdDistractor());
          Mockito.verify(pDDAO).buscarPorId(pk);
          Mockito.verify(pDDAO).eliminar(pD);
          Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
      }

     @Test
     public void eliminar_PreguntaDistractor_BadRequestIdDistractor(){
         Response response = cut.eliminar(null, UUID.randomUUID());
         Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
     }

    @Test
    public void eliminar_PreguntaDistractor_BadRequestIdPregunta(){
        Response response = cut.eliminar(UUID.randomUUID(), null);
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

     @Test
     public void eliminar_PreguntaDistractor_Exitoso(){
         PreguntaDistractor pD = new PreguntaDistractor();
         PreguntaDistractorPK pk = new PreguntaDistractorPK(p.getIdPregunta(), d.getIdDistractor());
         Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(pD);
         Mockito.doNothing().when(pDDAO).eliminar(pD);
         Response response = cut.eliminar(p.getIdPregunta(), d.getIdDistractor());
         Mockito.verify(pDDAO).buscarPorId(pk);
         Mockito.verify(pDDAO).eliminar(pD);
         Assertions.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
     }

     @Test
     public void eliminar_PreguntaDistractor_NotFound(){
         PreguntaDistractorPK pk = new PreguntaDistractorPK(p.getIdPregunta(), d.getIdDistractor());
         Mockito.when(pDDAO.buscarPorId(pk)).thenReturn(null);
         Response response = cut.eliminar(p.getIdPregunta(), d.getIdDistractor());
         Mockito.verify(pDDAO).buscarPorId(pk);
         Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void listar_PreguntaDistractor_Exitoso(){
         PreguntaDistractor pD = new PreguntaDistractor();
         pD.setIdPregunta(p);
         pD.setIdDistractor(d);
         pD.setCorrecto(true);

         Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
         Mockito.when(pDDAO.buscarPorIdPregunta(p.getIdPregunta())).thenReturn(List.of(pD, pD));

         Response response = cut.listar(p.getIdPregunta());
         Mockito.verify(pDDAO).buscarPorIdPregunta(p.getIdPregunta());
         Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
         Assertions.assertEquals(List.of(pD, pD), response.getEntity());
     }

     @Test
     public void listar_PreguntaDistractor_PreguntaNoEncontrada(){
         Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(null);

         Response response = cut.listar(p.getIdPregunta());
         Mockito.verify(pDAO).buscarPorId(p.getIdPregunta());
         Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

     @Test
     public void listar_PreguntaDistractor_NoEncontrado(){
         Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
         Mockito.when(pDDAO.buscarPorIdPregunta(p.getIdPregunta())).thenReturn(List.of());

         Response response = cut.listar(p.getIdPregunta());
         Mockito.verify(pDDAO).buscarPorIdPregunta(p.getIdPregunta());
         Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
     }

    @Test
    public void listar_PreguntaDistractor_Null(){
        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
        Mockito.when(pDDAO.buscarPorIdPregunta(p.getIdPregunta())).thenReturn(null);

        Response response = cut.listar(p.getIdPregunta());
        Mockito.verify(pDDAO).buscarPorIdPregunta(p.getIdPregunta());
        Assertions.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }


    @Test 
    public void listar_PreguntaDistractor_ErrorInterno(){
        Mockito.when(pDAO.buscarPorId(p.getIdPregunta())).thenReturn(p);
        Mockito.when(pDDAO.buscarPorIdPregunta(p.getIdPregunta())).thenThrow(new RuntimeException("Error de prueba"));

        Response response = cut.listar(p.getIdPregunta());
        Mockito.verify(pDDAO).buscarPorIdPregunta(p.getIdPregunta());
        Assertions.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void listar_PreguntaDistractor_BadRequest(){
        Response response = cut.listar(null);
        Assertions.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}