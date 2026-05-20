package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;

public class PreguntaResourceTest {

    PreguntaResource cut;
    PreguntaDAO mockPreguntaDAO;
    UriInfo mockUriInfo;
    UriBuilder mockUriBuilder;

    @BeforeEach
    public void setUp() {
        cut = new PreguntaResource();
        mockPreguntaDAO = Mockito.mock(PreguntaDAO.class);
        cut.preguntaDAO = mockPreguntaDAO;
        mockUriInfo = Mockito.mock(UriInfo.class);
        mockUriBuilder = Mockito.mock(UriBuilder.class);
        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build()).thenReturn(URI.create("http://localhost/pregunta"));
    }

    @Test
    public void crear_Pregunta_Exitoso() {
        Pregunta nuevaPregunta = new Pregunta();

        Response response = cut.crear(nuevaPregunta, mockUriInfo);

        Mockito.verify(mockPreguntaDAO).crear(nuevaPregunta);
        Mockito.verify(mockUriInfo).getAbsolutePathBuilder();
        Mockito.verify(mockUriBuilder).path(Mockito.anyString());
        Mockito.verify(mockUriBuilder).build();
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue(response.getHeaders().containsKey("Location"));
    }

    @Test
    public void crear_Pregunta_BadRequest() {
        Response response = cut.crear(null, mockUriInfo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El recurso no puede ser nulo y no debe tener un ID asignado",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void crear_Pregunta_BadRequest_ConIdAsignado() {
        Pregunta nuevaPregunta = new Pregunta();
        nuevaPregunta.setIdPregunta(UUID.randomUUID());

        Response response = cut.crear(nuevaPregunta, mockUriInfo);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El recurso no puede ser nulo y no debe tener un ID asignado",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void crear_Pregunta_ErrorInterno() {
        Pregunta nuevaPregunta = new Pregunta();
        Mockito.doThrow(new RuntimeException("Error interno")).when(mockPreguntaDAO).crear(nuevaPregunta);
        assertThrows(RuntimeException.class, () -> cut.crear(nuevaPregunta, mockUriInfo));
    }

    @Test
    public void buscarPorId_Pregunta_Encontrado() {
        UUID idPregunta = UUID.randomUUID();
        Pregunta existente = new Pregunta();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(existente);

        Response response = cut.buscarPorId(idPregunta);

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(existente, response.getEntity());
    }

    @Test
    public void buscarPorId_Pregunta_NoEncontrado() {
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(null);

        Response response = cut.buscarPorId(idPregunta);

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Recurso no encontrado", response.getHeaderString(ResponseHeaders.NOT_FOUND.toString()));
    }

    @Test
    public void buscarPorId_Pregunta_ErrorInterno() {
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idPregunta));
    }

    @Test
    public void buscarPorId_BadRequest() {
        Response response = cut.buscarPorId(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("id: null", response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void actualizar_Pregunta_Exitoso() {
        UUID idPregunta = UUID.randomUUID();
        Pregunta existente = new Pregunta();
        Pregunta entrada = new Pregunta();

        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(existente);

        Response response = cut.actualizar(idPregunta, entrada);

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        Mockito.verify(mockPreguntaDAO).actualizar(entrada);
        assertEquals(idPregunta, entrada.getIdPregunta());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void actualizar_Pregunta_NoEncontrado() {
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(null);

        Response response = cut.actualizar(idPregunta, new Pregunta());

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Recurso no encontrado", response.getHeaderString(ResponseHeaders.NOT_FOUND.toString()));
    }

    @Test
    public void actualizar_Pregunta_ErrorInterno() {
        UUID idPregunta = UUID.randomUUID();
        Pregunta entrada = new Pregunta();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.actualizar(idPregunta, entrada));
    }

    @Test
    public void actualizar_BadRequest_IdNull() {
        Response response = cut.actualizar(null, new Pregunta());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El recurso no puede ser nulo y debe tener un ID asignado",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void actualizar_BadRequest_PreguntaNull() {
        Response response = cut.actualizar(UUID.randomUUID(), null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El recurso no puede ser nulo y debe tener un ID asignado",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void actualizar_BadRequest_AmbosNull() {
        Response response = cut.actualizar(null, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El recurso no puede ser nulo y debe tener un ID asignado",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void eliminar_Pregunta_Exitoso() {
        UUID idPregunta = UUID.randomUUID();
        Pregunta existente = new Pregunta();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(existente);

        Response response = cut.eliminar(idPregunta);

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        Mockito.verify(mockPreguntaDAO).eliminar(existente);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void eliminar_Pregunta_NoEncontrado() {
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenReturn(null);

        Response response = cut.eliminar(idPregunta);

        Mockito.verify(mockPreguntaDAO).buscarPorId(idPregunta);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Recurso no encontrado", response.getHeaderString(ResponseHeaders.NOT_FOUND.toString()));
    }

    @Test
    public void eliminar_Pregunta_ErrorInterno() {
        UUID idPregunta = UUID.randomUUID();
        Mockito.when(mockPreguntaDAO.buscarPorId(idPregunta)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.eliminar(idPregunta));
    }

    @Test
    public void eliminar_BadRequest() {
        Response response = cut.eliminar(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("El ID no puede ser nulo", response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void buscarPorRango_Preguntas_Exitoso() {
        int first = 0;
        int max = 50;
        Mockito.when(mockPreguntaDAO.buscarPorRango(first, max)).thenReturn(List.of(new Pregunta(), new Pregunta()));
        Mockito.when(mockPreguntaDAO.contar()).thenReturn(2L);

        Response response = cut.buscarPorRango(first, max);

        Mockito.verify(mockPreguntaDAO).buscarPorRango(first, max);
        Mockito.verify(mockPreguntaDAO).contar();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("2", response.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
    }

    @Test
    public void buscarPorRango_Preguntas_Vacio() {
        int first = 0;
        int max = 50;
        Mockito.when(mockPreguntaDAO.buscarPorRango(first, max)).thenReturn(List.of());
        Mockito.when(mockPreguntaDAO.contar()).thenReturn(0L);

        Response response = cut.buscarPorRango(first, max);

        Mockito.verify(mockPreguntaDAO).buscarPorRango(first, max);
        Mockito.verify(mockPreguntaDAO).contar();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("0", response.getHeaderString(ResponseHeaders.TOTAL_RECORDS.toString()));
    }

    @Test
    public void buscarPorRango_Preguntas_Null() {
        int first = 0;
        int max = 50;
        Mockito.when(mockPreguntaDAO.buscarPorRango(first, max)).thenReturn(null);
        Mockito.when(mockPreguntaDAO.contar()).thenReturn(0L);

        Response response = cut.buscarPorRango(first, max);

        Mockito.verify(mockPreguntaDAO).buscarPorRango(first, max);
        Mockito.verify(mockPreguntaDAO).contar();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_BadRequest_AmbosInvalidos() {
        Response response = cut.buscarPorRango(-1, -10);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void buscarPorRango_BadRequest_FirstNegativo() {
        Response response = cut.buscarPorRango(-1, 50);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_BadRequest_MaxCero() {
        Response response = cut.buscarPorRango(0, 0);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_BadRequest_MaxNegativo() {
        Response response = cut.buscarPorRango(0, -5);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void buscarPorRango_BadRequest_MaxMayor50() {
        Response response = cut.buscarPorRango(0, 51);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Los parámetros 'first' debe ser >= 0 y 'max' debe ser > 0 y <= 50",
                response.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void buscarPorRango_Preguntas_ErrorInterno() {
        int first = 0;
        int max = 50;
        Mockito.when(mockPreguntaDAO.buscarPorRango(first, max)).thenThrow(new RuntimeException("Error interno"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(first, max));
    }
}

