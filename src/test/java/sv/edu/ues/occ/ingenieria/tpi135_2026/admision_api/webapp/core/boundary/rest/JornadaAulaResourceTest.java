package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaAulaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;

public class JornadaAulaResourceTest {

    JornadaDAO jDAO;
    JornadaAulaDAO jADAO;
    JornadaAulaResource cut;
    UUID idAula;
    Jornada j;
    UUID idJornada;
    jakarta.ws.rs.core.UriInfo uriInfo;
    jakarta.ws.rs.core.UriBuilder uriBuilder;

    @BeforeEach
    public void setUp() {
        jDAO = Mockito.mock(JornadaDAO.class);
        jADAO = Mockito.mock(JornadaAulaDAO.class);
        cut = new JornadaAulaResource();
        cut.jDAO = jDAO;
        cut.jADAO = jADAO;
        idAula = UUID.randomUUID();
        idJornada = UUID.randomUUID();
        j = new Jornada(idJornada);
        uriInfo = Mockito.mock(jakarta.ws.rs.core.UriInfo.class);
        uriBuilder = Mockito.mock(jakarta.ws.rs.core.UriBuilder.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
    }

    @Test
    public void crear_JornadaAula_Exitoso() {
        System.out.println("Ejecutando test: crear_JornadaAula_Exitoso");
        JornadaAula request = new JornadaAula(UUID.randomUUID());
        request.setIdAula(idAula.toString());

        Mockito.when(jDAO.buscarPorId(j.getIdJornada())).thenReturn(j); 
        Mockito.doNothing().when(jADAO).crear(Mockito.any());
        Response respuesta = cut.crear(idJornada, request, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crear_JornadaAula_JornadaNoEncontrada() {
        System.out.println("Ejecutando test: crear_JornadaAula_JornadaNoEncontrada");
        JornadaAula request = new JornadaAula(UUID.randomUUID());
        request.setIdAula(idAula.toString());

        Mockito.when(jDAO.buscarPorId(j.getIdJornada())).thenReturn(null);
        Response respuesta = cut.crear(idJornada, request, uriInfo);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crear_JornadaAula_ErrorInterno() {
        System.out.println("Ejecutando test: crear_JornadaAula_ErrorInterno");
        JornadaAula request = new JornadaAula(UUID.randomUUID());
        request.setIdAula(idAula.toString());

        Mockito.when(jDAO.buscarPorId(j.getIdJornada())).thenReturn(j);
        Mockito.doThrow(new RuntimeException("Error interno")).when(jADAO).crear(Mockito.any());
        Response respuesta = cut.crear(idJornada, request, uriInfo);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void crear_JornadaAula_BadRequest_IdJornada() {
        System.out.println("Ejecutando test: crear_JornadaAula_BadRequest");
        JornadaAula request = new JornadaAula(UUID.randomUUID());
        request.setIdAula(UUID.randomUUID().toString());
        Response respuesta = cut.crear(null, request, uriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }


    @Test
    public void crear_JornadaAula_BadRequest_IdAula() {
        System.out.println("Ejecutando test: crear_JornadaAula_BadRequest");
        Response respuesta = cut.crear(UUID.randomUUID(), null, uriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }


    @Test
    public void eliminar_JornadaAula_BadRequest_IdAula() {
        System.out.println("Ejecutando test: eliminar_JornadaAula_BadRequest");
        Response respuesta = cut.eliminar(UUID.randomUUID(), null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }
    @Test
    public void eliminar_JornadaAula_BadRequest_IdJornada() {
        System.out.println("Ejecutando test: eliminar_JornadaAula_BadRequest");
        Response respuesta = cut.eliminar(null, UUID.randomUUID().toString());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }


    @Test
    public void eliminar_JornadaAula_JornadaNoEncontrada() {
        System.out.println("Ejecutando test: eliminar_JornadaAula_JornadaNoEncontrada");
        cut.eliminar(idJornada, idAula.toString());

        Mockito.when(jADAO.buscarPorJornadaYAula(idJornada, idAula.toString())).thenReturn(null);
        Response respuesta = cut.eliminar(idJornada, idAula.toString());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }

    @Test 
    public void eliminar_JornadaAula_ErrorInterno() {
        System.out.println("Ejecutando test: eliminar_JornadaAula_ErrorInterno");
        cut.eliminar(idJornada, idAula.toString());

        Mockito.doThrow(new RuntimeException("Error interno")).when(jADAO).eliminar(Mockito.any());
        Mockito.when(jADAO.buscarPorJornadaYAula(idJornada, idAula.toString())).thenReturn(new JornadaAula(UUID.randomUUID()));
        Response respuesta = cut.eliminar(idJornada, idAula.toString());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void eliminar_JornadaAula_Exitoso() {
        System.out.println("Ejecutando test: eliminar_JornadaAula_Exitoso");
        cut.eliminar(idJornada, idAula.toString());

        Mockito.doNothing().when(jADAO).eliminar(Mockito.any());
        Mockito.when(jADAO.buscarPorJornadaYAula(idJornada, idAula.toString())).thenReturn(new JornadaAula(UUID.randomUUID()));
        Response respuesta = cut.eliminar(idJornada, idAula.toString());
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void listar_JornadaAula_BadRequest() {
        System.out.println("Ejecutando test: listar_JornadaAula_BadRequest");
        Response respuesta = cut.listarAulaJornadas(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void listar_JornadaAula_Exitoso() {
        System.out.println("Ejecutando test: listar_JornadaAula_Exitoso");
        cut.listarAulaJornadas(idJornada);

        Mockito.when(jADAO.listarPorJornada(idJornada)).thenReturn(List.of(new JornadaAula(UUID.randomUUID())));
        Mockito.verify(jADAO).listarPorJornada(idJornada);
        Response respuesta = cut.listarAulaJornadas(idJornada);
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }

    @Test 
    public void listar_JornadaAula_NoEncontrado_EmptyList() {
        System.out.println("Ejecutando test: listar_JornadaAula_NoEncontrado");
        cut.listarAulaJornadas(idJornada);

        Mockito.when(jADAO.listarPorJornada(idJornada)).thenReturn(List.of());
        Mockito.verify(jADAO).listarPorJornada(idJornada);
        Response respuesta = cut.listarAulaJornadas(idJornada);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }

    @Test
    public void listar_JornadaAula_NoEncontrado_ListNull() {
        System.out.println("Ejecutando test: listar_JornadaAula_NoEncontrado");
        cut.listarAulaJornadas(idJornada);

        Mockito.when(jADAO.listarPorJornada(idJornada)).thenReturn(null);
        Mockito.verify(jADAO).listarPorJornada(idJornada);
        Response respuesta = cut.listarAulaJornadas(idJornada);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), respuesta.getStatus());
    }

     @Test 
     public void listar_JornadaAula_ErrorInterno() {
         System.out.println("Ejecutando test: listar_JornadaAula_ErrorInterno");
         cut.listarAulaJornadas(idJornada);

         Mockito.doThrow(new RuntimeException("Error interno")).when(jADAO).listarPorJornada(idJornada);
         Response respuesta = cut.listarAulaJornadas(idJornada);
         assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), respuesta.getStatus());
     }
}