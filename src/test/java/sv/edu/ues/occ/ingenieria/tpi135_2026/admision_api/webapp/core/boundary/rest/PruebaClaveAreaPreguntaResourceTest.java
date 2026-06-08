package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;



import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveAreaPreguntaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaPK;

@ExtendWith(MockitoExtension.class)
class PruebaClaveAreaPreguntaResourceTest {

    @Mock
    private PreguntaDAO pDAO;

    @Mock
    private AreaDAO aDAO;

    @Mock
    private PruebaClaveAreaDAO pCADAO;

    @Mock
    private PruebaClaveAreaPreguntaDAO pCAPDAO;

    @InjectMocks
    private PruebaClaveAreaPreguntaResource resource;

    PruebaClave pruebaClave = new PruebaClave();
    private UUID idPruebaClave;
    Area area = new Area();
    private UUID idArea;
    Pregunta pregunta = new Pregunta();
    private UUID idPregunta;

    PruebaClaveArea pruebaClaveArea = new PruebaClaveArea();
    PruebaClaveAreaPK pkArea = new PruebaClaveAreaPK();

    PruebaClaveAreaPregunta pruebaClaveAreaPregunta = new PruebaClaveAreaPregunta();

    @BeforeEach
    void setUp() {
        idPruebaClave = UUID.randomUUID();
        idArea = UUID.randomUUID();
        idPregunta = UUID.randomUUID();

        pruebaClave.setIdPruebaClave(idPruebaClave);
        area.setIdArea(idArea);
        pregunta.setIdPregunta(idPregunta);

        pruebaClaveArea.setIdPruebaClave(pruebaClave);
        pruebaClaveArea.setIdArea(area);
        pkArea = new PruebaClaveAreaPK(idPruebaClave, idArea);

        pruebaClaveAreaPregunta.setIdArea(area);
        pruebaClaveAreaPregunta.setIdPregunta(pregunta);
        pruebaClaveAreaPregunta.setPorcentaje(new BigDecimal(1));
    }

    @Nested
    @DisplayName("Pruebas para el método crear")
    class CrearTests {

        @Test
        @DisplayName("Debe crear exitosamente y retornar 201")
        void crear_Exitoso() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(pruebaClaveAreaPregunta.getIdPregunta());
            when(pCAPDAO.validarPorcentajePrueba(pruebaClave, pruebaClaveAreaPregunta)).thenReturn(true);

            Response response = resource.crear(idPruebaClave, idArea, pruebaClaveAreaPregunta);

            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            verify(pCAPDAO).crear(any(PruebaClaveAreaPregunta.class));
        }

        @Test
        @DisplayName("Debe retornar 404 si pruebaClaveArea no existe")
        void crear_PruebaClaveAreaNoEncontrada() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(null);

            Response response = resource.crear(idPruebaClave, idArea, pruebaClaveAreaPregunta);

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any());
        }
        @Test
        @DisplayName("Debe retornar 404 si la pregunta no existe")
        void crear_PreguntaNoEncontrada() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            pruebaClaveArea.setIdPruebaClave(pruebaClave);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(null);
            Response response = resource.crear(idPruebaClave, idArea, pruebaClaveAreaPregunta);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any());
            }

        @Test
        @DisplayName("Debe retornar 400 si idPruebaClave es nulo")
        void crear_IdPruebaClaveNulo() {
            Response response = resource.crear(null, idArea, pruebaClaveAreaPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idArea es nulo")
        void crear_IdAreaNulo() {
            Response response = resource.crear(idPruebaClave, null, pruebaClaveAreaPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si pruebaClaveAreaPregunta es nulo")
        void crear_PruebaClaveAreaPreguntaNulo() {
            Response response = resource.crear(idPruebaClave, idArea, null);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 409 CONFLICT si se excede el porcentaje máximo")
        void crear_PorcentajeExcedido() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(pruebaClaveAreaPregunta.getIdPregunta());
            when(pCAPDAO.validarPorcentajePrueba(pruebaClave, pruebaClaveAreaPregunta)).thenReturn(false);

            Response response = resource.crear(idPruebaClave, idArea, pruebaClaveAreaPregunta);

            assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any(PruebaClaveAreaPregunta.class));
        }

        @Test
        @DisplayName("Debe retornar 500 si ocurre una excepción")
        void crear_Excepcion() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenThrow(new RuntimeException("Error de BD"));
            assertThrows(RuntimeException.class, () -> resource.crear(idPruebaClave, idArea, pruebaClaveAreaPregunta));
        }
    }



    @Nested
    @DisplayName("Pruebas para el método eliminar")
    class EliminarTests {

        @Test
        @DisplayName("Debe eliminar exitosamente y retornar 204")
        void eliminar_Exitoso() {
            PruebaClaveAreaPregunta pcap = new PruebaClaveAreaPregunta();
            when(pCAPDAO.buscarPorId(any(PruebaClaveAreaPreguntaPK.class))).thenReturn(pcap);

            Response response = resource.eliminar(idPruebaClave, idArea, idPregunta);

            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(pCAPDAO).eliminar(pcap);
        }

        @Test
        @DisplayName("Debe retornar 404 si no encuentra el registro a eliminar")
        void eliminar_NoEncontrado() {
            when(pCAPDAO.buscarPorId(any(PruebaClaveAreaPreguntaPK.class))).thenReturn(null);

            Response response = resource.eliminar(idPruebaClave, idArea, idPregunta);

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).eliminar(any());
        }

        @Test
        @DisplayName("Debe retornar 400 si idPruebaClave es nulo")
        void eliminar_IdPruebaClaveNulo() {
            Response response = resource.eliminar(null, idArea, idPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idArea es nulo")
        void eliminar_IdAreaNulo() {
            Response response = resource.eliminar(idPruebaClave, null, idPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idPregunta es nulo")
        void eliminar_IdPreguntaNulo() {
            Response response = resource.eliminar(idPruebaClave, idArea, null);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 500 si ocurre una excepción")
        void eliminar_Excepcion() {
            when(pCAPDAO.buscarPorId(any(PruebaClaveAreaPreguntaPK.class)))
                    .thenThrow(new RuntimeException("Error al eliminar"));
            assertThrows(RuntimeException.class, () -> resource.eliminar(idPruebaClave, idArea, idPregunta));
        }
    }

    @Nested
    @DisplayName("Pruebas para el método listar")
    class ListarTests {

        @Test
        @DisplayName("Debe retornar la lista y 200")
        void listar_Exitoso() {
            List<PruebaClaveAreaPregunta> lista = Arrays.asList(new PruebaClaveAreaPregunta());
            when(pCAPDAO.buscarPorClaveYArea(idPruebaClave, idArea)).thenReturn(lista);

            Response response = resource.listar(idPruebaClave, idArea);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(lista, response.getEntity());
        }

        @Test
        @DisplayName("Debe retornar 200 si la lista es nula")
        void listar_Nulo() {
            when(pCAPDAO.buscarPorClaveYArea(idPruebaClave, idArea)).thenReturn(null);

            Response response = resource.listar(idPruebaClave, idArea);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 200 si la lista está vacía")
        void listar_ListaVacia() {
            when(pCAPDAO.buscarPorClaveYArea(idPruebaClave, idArea)).thenReturn(List.of());

            Response response = resource.listar(idPruebaClave, idArea);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idPruebaClave es nulo")
        void listar_IdPruebaClaveNulo() {
            Response response = resource.listar(null, idArea);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idArea es nulo")
        void listar_IdAreaNulo() {
            Response response = resource.listar(idPruebaClave, null);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 500 si ocurre una excepción")
        void listar_Excepcion() {
            when(pCAPDAO.buscarPorClaveYArea(idPruebaClave, idArea))
                    .thenThrow(new RuntimeException("Error de consulta"));
            assertThrows(RuntimeException.class, () -> resource.listar(idPruebaClave, idArea));
        }
    }

    @Test
    @DisplayName("Debe retornar el registro y 200")
    void buscarPorId_Exitoso() {
        PruebaClaveAreaPreguntaPK pk = new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta);
        when(pCAPDAO.buscarPorId(pk)).thenReturn(pruebaClaveAreaPregunta);

        Response response = resource.buscarPorId(idPruebaClave, idArea, idPregunta);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(pruebaClaveAreaPregunta, response.getEntity());
    }

    @Test
    @DisplayName("Debe retornar 404 si no se encuentra el registro")
    void buscarPorId_NoEncontrado() {
        PruebaClaveAreaPreguntaPK pk = new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta);
        when(pCAPDAO.buscarPorId(pk)).thenReturn(null);

        Response response = resource.buscarPorId(idPruebaClave, idArea, idPregunta);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Debe retornar 400 si idPruebaClave es nulo")
    void buscarPorId_IdPruebaClaveNulo() {
        Response response = resource.buscarPorId(null, idArea, idPregunta);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Debe retornar 400 si idArea es nulo")
    void buscarPorId_IdAreaNulo() {
        Response response = resource.buscarPorId(idPruebaClave, null, idPregunta);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Debe retornar 400 si idPregunta es nulo")
    void buscarPorId_IdPreguntaNulo() {
        Response response = resource.buscarPorId(idPruebaClave, idArea, null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @DisplayName("Debe retornar 500 si ocurre una excepción")
    void buscarPorId_Excepcion() {
        PruebaClaveAreaPreguntaPK pk = new PruebaClaveAreaPreguntaPK(idPruebaClave, idArea, idPregunta);
        when(pCAPDAO.buscarPorId(pk)).thenThrow(new RuntimeException("Error de consulta"));
        assertThrows(RuntimeException.class, () -> resource.buscarPorId(idPruebaClave, idArea, idPregunta));
    }

    @Nested
    @DisplayName("Pruebas para el método actuaizar")
    class ActualizarTests {

        @Test
        @DisplayName("Debe crear exitosamente y retornar 201")
        void actualizarrExitoso() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(pruebaClaveAreaPregunta.getIdPregunta());
            when(pCAPDAO.validarPorcentajePrueba(pruebaClave, pruebaClaveAreaPregunta)).thenReturn(true);

            Response response = resource.actualizar(idPruebaClave, idArea, pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta(),pruebaClaveAreaPregunta);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            verify(pCAPDAO).actualizar(any(PruebaClaveAreaPregunta.class));
        }

        @Test
        @DisplayName("Debe retornar 404 si pruebaClaveArea no existe")
        void actualizarPruebaClaveAreaNoEncontrada() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(null);

            Response response = resource.actualizar(idPruebaClave, idArea, pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta(),pruebaClaveAreaPregunta);

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any());
        }
        @Test
        @DisplayName("Debe retornar 404 si la pregunta no existe")
        void actualizarPreguntaNoEncontrada() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            pruebaClaveArea.setIdPruebaClave(pruebaClave);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(null);
            Response response = resource.actualizar(idPruebaClave, idArea, pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta(),pruebaClaveAreaPregunta);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any());
        }

        @Test
        @DisplayName("Debe retornar 400 si idPruebaClave es nulo")
        void actualizarIdPruebaClaveNulo() {
            Response response = resource.actualizar(null, idArea, pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta(),pruebaClaveAreaPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si idArea es nulo")
        void actualizarIdAreaNulo() {
            Response response = resource.actualizar(idPruebaClave, null,pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta() ,pruebaClaveAreaPregunta);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 400 si pruebaClaveAreaPregunta es nulo")
        void actualizarPruebaClaveAreaPreguntaNulo() {
            Response response = resource.actualizar(idPruebaClave, idArea, idPregunta,null);

            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar 409 CONFLICT si se excede el porcentaje máximo")
        void actualizarPorcentajeExcedido() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenReturn(pruebaClaveArea);
            when(pDAO.buscarPorId(pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta())).thenReturn(pruebaClaveAreaPregunta.getIdPregunta());
            when(pCAPDAO.validarPorcentajePrueba(pruebaClave, pruebaClaveAreaPregunta)).thenReturn(false);

            Response response = resource.actualizar(idPruebaClave, idArea, pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta(),pruebaClaveAreaPregunta);

            assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
            verify(pCAPDAO, never()).crear(any(PruebaClaveAreaPregunta.class));
        }

        @Test
        @DisplayName("Debe retornar 500 si ocurre una excepción")
        void actualizarExcepcion() {
            PruebaClaveAreaPK pk = new PruebaClaveAreaPK(idPruebaClave, idArea);
            when(pCADAO.buscarPorId(pk)).thenThrow(new RuntimeException("Error de BD"));
            assertThrows(RuntimeException.class, () -> resource.actualizar(idPruebaClave, idArea,pruebaClaveAreaPregunta.getIdPregunta().getIdPregunta() ,pruebaClaveAreaPregunta));
        }
    }



}
