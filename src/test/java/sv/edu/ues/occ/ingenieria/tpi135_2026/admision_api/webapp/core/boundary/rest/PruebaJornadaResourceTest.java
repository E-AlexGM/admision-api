package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaPK;

public class PruebaJornadaResourceTest {

    PruebaJornadaResource cut;
    Prueba p;
    Jornada j;
    PruebaDAO pDAO;
    JornadaDAO jDAO;
    PruebaJornadaDAO pJDAO;
    UriInfo uriInfo;
    UriBuilder uriBuilder;

    @BeforeEach
    public void setUp() {
        // Configurar el entorno dme prueba, como inicializar objetos necesarios
         // Clase bajo prueba

        cut = new PruebaJornadaResource();      

        pDAO = Mockito.mock(PruebaDAO.class);
        jDAO = Mockito.mock(JornadaDAO.class);
        pJDAO = Mockito.mock(PruebaJornadaDAO.class);

        cut.pDAO = pDAO;
        cut.jDAO = jDAO;
        cut.pJDAO = pJDAO;

        uriInfo = Mockito.mock(UriInfo.class);
        uriBuilder = Mockito.mock(UriBuilder.class);
         Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
         Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
    }

    @Test
    public void crearExitoso() {
        System.out.println("EjecutandoTest: crear_PruebaJornada_Exitoso");
        UUID idPrueba = UUID.randomUUID();
        UUID idJornada = UUID.randomUUID();
        p = new Prueba(idPrueba);
        j = new Jornada(idJornada);

        Mockito.when(pDAO.buscarPorId(p.getIdPrueba())).thenReturn(p);
        Mockito.when(jDAO.buscarPorId(j.getIdJornada())).thenReturn(j);
        PruebaJornada pj = new PruebaJornada(p, j);
        Mockito.doNothing().when(pJDAO).crear(Mockito.any(PruebaJornada.class)); // Simula la creación exitosa

        PruebaJornada request = new PruebaJornada();
        request.setIdJornada(new Jornada(idJornada));
        Response respuesta = cut.crear(idPrueba, request, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crearNotFoundJornada() {
        System.out.println("EjecutandoTest: crearNotFound");
        UUID idPrueba = UUID.randomUUID();
        UUID idJornada = UUID.randomUUID();

        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(new Prueba(UUID.randomUUID())); // Simula que no se encuentra la prueba
        Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(null); // Simula que no se encuentra la jornada

        PruebaJornada request = new PruebaJornada();
        request.setIdJornada(new Jornada(idJornada));
        Response respuesta = cut.crear(idPrueba, request, uriInfo);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crearNotFoundPrueba() {
        System.out.println("EjecutandoTest: crearNotFound");
        UUID idPrueba = UUID.randomUUID();
        UUID idJornada = UUID.randomUUID();

        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null); // Simula que no se encuentra la prueba
        Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(new Jornada(UUID.randomUUID())); // Simula que no se encuentra la jornada

        PruebaJornada request = new PruebaJornada();
        request.setIdJornada(new Jornada(idJornada));
        Response respuesta = cut.crear(idPrueba, request, uriInfo);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }


    @Test
    public void crearBadRequestIdPrueba() {
        System.out.println("EjecutandoTest: crearBadRequest");
        PruebaJornada request = new PruebaJornada();
        request.setIdJornada(new Jornada(UUID.randomUUID()));
        Response respuesta = cut.crear(null, request, uriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crearBadRequestIdJornada() {
        System.out.println("EjecutandoTest: crearBadRequest");
        Response respuesta = cut.crear(UUID.randomUUID(), null, uriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }

     @Test
     public void crearPruebaNull() {
         System.out.println("EjecutandoTest: crearPruebaNull");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         j = new Jornada(idJornada);

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null); // Prueba no encontrada
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(j);

            PruebaJornada request = new PruebaJornada();
            request.setIdJornada(new Jornada(idJornada));
            Response respuesta = cut.crear(idPrueba, request, uriInfo);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void crearJornadaNull() {
         System.out.println("EjecutandoTest: crearJornadaNull");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         p = new Prueba(idPrueba);

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(p);
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(null); // Jornada no encontrada

            PruebaJornada request = new PruebaJornada();
            request.setIdJornada(new Jornada(idJornada));
            Response respuesta = cut.crear(idPrueba, request, uriInfo);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void crearErrorInterno() {
         System.out.println("EjecutandoTest: crearErrorInterno");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         p = new Prueba(idPrueba);
         j = new Jornada(idJornada);

        cut.jDAO = null;
        PruebaJornada request = new PruebaJornada();
        request.setIdJornada(new Jornada(idJornada));
        Response respuesta = cut.crear(idPrueba, request, uriInfo);
         assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
     }

    @Test
    public void eliminarExitoso() {
        System.out.println("EjecutandoTest: eliminarExitoso");
        UUID idPrueba = UUID.randomUUID();
        UUID idJornada = UUID.randomUUID();
        p = new Prueba(idPrueba);
        j = new Jornada(idJornada);
        PruebaJornada pj = new PruebaJornada(p, j);

        Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(p);
        Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(j);
        Mockito.when(pJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(pj);
        Mockito.doNothing().when(pJDAO).eliminar(pj);

        Response respuesta = cut.eliminar(idPrueba, idJornada);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }

     @Test
     public void eliminarNotFound() {
         System.out.println("EjecutandoTest: eliminarNotFound");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null);
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(null);

         Response respuesta = cut.eliminar(idPrueba, idJornada);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void eliminarPruebaNull() {
         System.out.println("EjecutandoTest: eliminarPruebaNull");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         j = new Jornada(idJornada);

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(null); // Prueba no encontrada
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(j);

         Response respuesta = cut.eliminar(idPrueba, idJornada);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void eliminarJornadaNull() {
         System.out.println("EjecutandoTest: eliminarJornadaNull");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         p = new Prueba(idPrueba);

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(p);
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(null); // Jornada no encontrada

         Response respuesta = cut.eliminar(idPrueba, idJornada);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void eliminarPruebaJornadaExistenteNull() {
         System.out.println("EjecutandoTest: eliminarPruebaJornadaExistenteNull");
         UUID idPrueba = UUID.randomUUID();
         UUID idJornada = UUID.randomUUID();
         p = new Prueba(idPrueba);
         j = new Jornada(idJornada);

         Mockito.when(pDAO.buscarPorId(idPrueba)).thenReturn(p);
         Mockito.when(jDAO.buscarPorId(idJornada)).thenReturn(j);
         Mockito.when(pJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(null); // No existe la relación

         Response respuesta = cut.eliminar(idPrueba, idJornada);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void eliminarBadRequestIdPrueba() {
        System.out.println("EjecutandoTest: eliminarBadRequest");
        Response respuesta = cut.eliminar(UUID.randomUUID(), null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void eliminarBadRequestIdJornada() {
        System.out.println("EjecutandoTest: eliminarBadRequest");
        Response respuesta = cut.eliminar(null, UUID.randomUUID());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }

     @Test
    public void eliminarErrorInterno() {
        System.out.println("EjecutandoTest: eliminarErrorInterno");
        UUID idPrueba = UUID.randomUUID();
        UUID idJornada = UUID.randomUUID();
        p = new Prueba(idPrueba);
        j = new Jornada(idJornada);
        cut.jDAO = null;
        Response respuesta = cut.eliminar(idPrueba, idJornada);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
    }

     @Test
     public void listarJornadasExitoso(){
         System.out.println("EjecutandoTest: listarJornadasExitoso");
         UUID idPrueba = UUID.randomUUID();
         Mockito.when(jDAO.listarPorIdPrueba(idPrueba, 0, 10)).thenReturn(List.of(new Jornada(UUID.randomUUID()), new Jornada(UUID.randomUUID())));
         Response respuesta = cut.listarJornadas(idPrueba, 0, 10);
         assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void listarJornadasNotFound(){
         System.out.println("EjecutandoTest: listarJornadasNotFound");
         UUID idPrueba = UUID.randomUUID();
         Mockito.when(jDAO.listarPorIdPrueba(idPrueba, 0, 10)).thenReturn(null);
         Response respuesta = cut.listarJornadas(idPrueba, 0, 10);
         assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void listarJornadasBadRequestNullId(){
         System.out.println("EjecutandoTest: listarJornadasBadRequestNullId");
         Response respuesta = cut.listarJornadas(null, 0, 10);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
     }


     @Test
     public void listarJornadasBadRequestFirstNegativo(){
         System.out.println("EjecutandoTest: listarJornadasBadRequestFirstNegativo");
         UUID idPrueba = UUID.randomUUID();
         Response respuesta = cut.listarJornadas(idPrueba, -1, 10);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void listarJornadasBadRequestMaxInvalido(){
         System.out.println("EjecutandoTest: listarJornadasBadRequestMaxInvalido");
         UUID idPrueba = UUID.randomUUID();
         Response respuesta = cut.listarJornadas(idPrueba, 0, 0);
         assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
     }

     @Test
     public void listarJornadasErrorInterno(){
         System.out.println("EjecutandoTest: listarJornadasErrorInterno");
         UUID idPrueba = UUID.randomUUID();
         cut.jDAO = null;
         Response respuesta = cut.listarJornadas(idPrueba, 0, 10);
         assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
     }

 }
