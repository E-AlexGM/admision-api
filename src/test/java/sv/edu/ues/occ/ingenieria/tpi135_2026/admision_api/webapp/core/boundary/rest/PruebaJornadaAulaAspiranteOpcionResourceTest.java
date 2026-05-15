package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.net.URI;
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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.AspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.JornadaAulaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaPK;

public class PruebaJornadaAulaAspiranteOpcionResourceTest {

    private UriInfo mockUriInfo;
    private PruebaJornadaAulaAspiranteOpcionDAO mockDAO;
    private PruebaJornadaDAO mockPJDAO;
    private JornadaAulaDAO mockJADAO;
    private AspiranteOpcionDAO mockAODAO;
    private PruebaJornadaAulaAspiranteOpcionResource cut;

    private UUID idPrueba;
    private UUID idJornada;
    private String idAula;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockDAO = Mockito.mock(PruebaJornadaAulaAspiranteOpcionDAO.class);
        mockPJDAO = Mockito.mock(PruebaJornadaDAO.class);
        mockJADAO = Mockito.mock(JornadaAulaDAO.class);
        mockAODAO = Mockito.mock(AspiranteOpcionDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build()).thenReturn(URI.create("http://localhost/1"));

        idPrueba = UUID.randomUUID();
        idJornada = UUID.randomUUID();
        idAula = "A-01";

        cut = new PruebaJornadaAulaAspiranteOpcionResource();
        cut.pruebaJornadaAulaAspiranteOpcionDAO = mockDAO;
        cut.pruebaJornadaDAO = mockPJDAO;
        cut.jornadaAulaDAO = mockJADAO;
        cut.aspiranteOpcionDAO = mockAODAO;
    }

    private PruebaJornadaAulaAspiranteOpcion nuevaEntidad(UUID idAspiranteOpcion) {
        return new PruebaJornadaAulaAspiranteOpcion(
                new Prueba(idPrueba),
                new Jornada(idJornada),
                idAula,
                new AspiranteOpcion(idAspiranteOpcion));
    }

    // ============================================================
    //  POST - crear
    // ============================================================

    @Test
    public void crearExitosoTest() {
        UUID idAO = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcion entity = new PruebaJornadaAulaAspiranteOpcion();
        entity.setIdAspiranteOpcion(new AspiranteOpcion(idAO));

        UUID idJornadaCompartida = UUID.randomUUID();
        PruebaJornada pj = new PruebaJornada(new Prueba(idPrueba), new Jornada(idJornadaCompartida));
        JornadaAula ja = new JornadaAula();
        ja.setIdAula(idAula);
        ja.setIdJornada(new Jornada(idJornadaCompartida));

        Mockito.when(mockPJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(pj);
        Mockito.when(mockJADAO.buscarPorJornadaYAula(idJornada, idAula)).thenReturn(ja);
        Mockito.when(mockAODAO.buscarPorId(idAO)).thenReturn(new AspiranteOpcion(idAO));

        Response res = cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo);
        assertEquals(201, res.getStatus());
        Mockito.verify(mockDAO).crear(entity);
    }

    @Test
    public void crearValidacionFallidaTest() {
        assertEquals(400, cut.crear(null, idJornada, idAula, new PruebaJornadaAulaAspiranteOpcion(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, null, idAula, new PruebaJornadaAulaAspiranteOpcion(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, idJornada, null, new PruebaJornadaAulaAspiranteOpcion(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, null, mockUriInfo).getStatus());

        // AspiranteOpcion nulo o con ID interno nulo
        PruebaJornadaAulaAspiranteOpcion entitySinAO = new PruebaJornadaAulaAspiranteOpcion();
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, entitySinAO, mockUriInfo).getStatus());

        PruebaJornadaAulaAspiranteOpcion entityConAOSinId = new PruebaJornadaAulaAspiranteOpcion();
        entityConAOSinId.setIdAspiranteOpcion((AspiranteOpcion) null);
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, entityConAOSinId, mockUriInfo).getStatus());
    }

    @Test
    public void crearJornadasNoCoincidenODesconocidasTest() {
        // Valida diferentes formas en que la jornada queda inconsistente al crear.
        PruebaJornadaAulaAspiranteOpcion entity = new PruebaJornadaAulaAspiranteOpcion();
        entity.setIdAspiranteOpcion(new AspiranteOpcion(UUID.randomUUID()));

        UUID idJornadaPrueba = UUID.randomUUID();
        PruebaJornada pj = new PruebaJornada(new Prueba(idPrueba), new Jornada(idJornadaPrueba));
        JornadaAula ja = new JornadaAula();
        ja.setIdAula(idAula);

        Mockito.when(mockPJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(pj);
        Mockito.when(mockJADAO.buscarPorJornadaYAula(idJornada, idAula)).thenReturn(ja);
        Mockito.when(mockAODAO.buscarPorId(Mockito.any())).thenReturn(new AspiranteOpcion());

        // Escenario 1: la jornada del aula no existe (null), por lo tanto no se puede validar pertenencia.
        Response res1 = cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo);
        assertEquals(400, res1.getStatus());

        // Escenario 2: ambas jornadas existen, pero no coinciden entre prueba y aula.
        ja.setIdJornada(new Jornada(UUID.randomUUID()));
        Response res2 = cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo);
        assertEquals(400, res2.getStatus());
        assertEquals("Conflicto de Jornada", res2.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
        Mockito.verify(mockDAO, Mockito.never()).crear(Mockito.any());

        // Escenario 3: se cambia la jornada de prueba y se mantiene el conflicto con la jornada del aula.
        pj.setIdJornada(new Jornada(UUID.randomUUID())); // pj ahora tiene, pero distinta a ja
        Response res3 = cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo);
        assertEquals(400, res3.getStatus());

        // Escenario 4: la prueba no tiene jornada asociada (rama faltante del OR de conflicto).
        pj.setIdJornada(null);
        ja.setIdJornada(new Jornada(UUID.randomUUID()));
        Response res4 = cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo);
        assertEquals(400, res4.getStatus());
        assertEquals("Conflicto de Jornada", res4.getHeaderString(ResponseHeaders.WRONG_PARAMETER.toString()));
    }

    @Test
    public void crearNoEncontradosTest() {
        PruebaJornadaAulaAspiranteOpcion entity = new PruebaJornadaAulaAspiranteOpcion();
        entity.setIdAspiranteOpcion(new AspiranteOpcion(UUID.randomUUID()));

        // 1. Falta prueba
        Mockito.when(mockPJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(null);
        assertEquals(404, cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo).getStatus());

        // 2. Falta aula (pero prueba sí está)
        Mockito.when(mockPJDAO.buscarPorId(Mockito.any(PruebaJornadaPK.class))).thenReturn(new PruebaJornada());
        Mockito.when(mockJADAO.buscarPorJornadaYAula(idJornada, idAula)).thenReturn(null);
        assertEquals(404, cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo).getStatus());

        // 3. Falta aspirante (pero prueba y aula sí están)
        Mockito.when(mockJADAO.buscarPorJornadaYAula(idJornada, idAula)).thenReturn(new JornadaAula());
        Mockito.when(mockAODAO.buscarPorId(Mockito.any())).thenReturn(null);
        assertEquals(404, cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo).getStatus());
    }

    @Test
    public void crearExcepcionTest() {
        PruebaJornadaAulaAspiranteOpcion entity = new PruebaJornadaAulaAspiranteOpcion();
        entity.setIdAspiranteOpcion(new AspiranteOpcion(UUID.randomUUID()));

        Mockito.when(mockPJDAO.buscarPorId(Mockito.any())).thenThrow(new RuntimeException("Error BD"));
        assertThrows(RuntimeException.class, () -> cut.crear(idPrueba, idJornada, idAula, entity, mockUriInfo));
    }

    // ============================================================
    //  GET - buscarPorRango
    // ============================================================

    @Test
    public void buscarPorRangoExitosoTest() {
        Mockito.when(mockDAO.buscarPorPruebaJornadaYJornadaAulaRango(idPrueba, idJornada, idAula, 0, 50)).thenReturn(List.of());
        Mockito.when(mockDAO.contarPorPruebaJornadaYJornadaAula(idPrueba, idJornada, idAula)).thenReturn(0L);
        assertEquals(200, cut.buscarPorRango(idPrueba, idJornada, idAula, 0, 50).getStatus());
    }

    @Test
    public void buscarPorRangoValidacionFallidaTest() {
        assertEquals(400, cut.buscarPorRango(null, idJornada, idAula, 0, 50).getStatus());
        assertEquals(400, cut.buscarPorRango(idPrueba, null, idAula, 0, 50).getStatus());
        assertEquals(400, cut.buscarPorRango(idPrueba, idJornada, null, 0, 50).getStatus());
        assertEquals(400, cut.buscarPorRango(idPrueba, idJornada, idAula, -1, 50).getStatus());
        assertEquals(400, cut.buscarPorRango(idPrueba, idJornada, idAula, 0, 0).getStatus());
        assertEquals(400, cut.buscarPorRango(idPrueba, idJornada, idAula, 0, 51).getStatus());
    }

    @Test
    public void buscarPorRangoExcepcionTest() {
        Mockito.when(mockDAO.buscarPorPruebaJornadaYJornadaAulaRango(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorRango(idPrueba, idJornada, idAula, 0, 50));
    }

    // ============================================================
    //  GET {id} - buscarPorId
    // ============================================================

    @Test
    public void buscarPorIdExitosoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id)))
            .thenReturn(nuevaEntidad(id));
        assertEquals(200, cut.buscarPorId(idPrueba, idJornada, idAula, id).getStatus());
    }

    @Test
    public void buscarPorIdValidacionFallidaTest() {
        assertEquals(400, cut.buscarPorId(null, idJornada, idAula, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.buscarPorId(idPrueba, null, idAula, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.buscarPorId(idPrueba, idJornada, null, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.buscarPorId(idPrueba, idJornada, idAula, null).getStatus());
    }

    @Test
    public void buscarPorIdNoEncontradoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id)))
                .thenReturn(null);
        Response response = cut.buscarPorId(idPrueba, idJornada, idAula, id);
        assertEquals(404, response.getStatus());
        assertEquals("Recurso no encontrado", response.getHeaderString(ResponseHeaders.NOT_FOUND.toString()));
    }

    @Test
    public void buscarPorIdExcepcionTest() {
        Mockito.when(mockDAO.buscarPorId(Mockito.any(PruebaJornadaAulaAspiranteOpcionPK.class)))
                .thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> cut.buscarPorId(idPrueba, idJornada, idAula, UUID.randomUUID()));
    }

    // ============================================================
    //  PUT - actualizar
    // ============================================================

    @Test
    public void actualizarExitosoTest() {
        // el id del path y del body son del mismo valor
        UUID id = UUID.randomUUID();
        UUID idAO = id;
        PruebaJornadaAulaAspiranteOpcion entity = nuevaEntidad(id);
        entity.setIdAspiranteOpcion(new AspiranteOpcion(idAO));

        PruebaJornadaAulaAspiranteOpcion existente = nuevaEntidad(id);
        existente.setIdPrueba(new Prueba(idPrueba));
        existente.setIdJornada(new Jornada(idJornada));
        existente.setIdAula(idAula);

        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(existente);
        Mockito.when(mockAODAO.buscarPorId(idAO)).thenReturn(new AspiranteOpcion(idAO));

        Response res = cut.actualizar(idPrueba, idJornada, idAula, id, entity);
        assertEquals(200, res.getStatus());
        Mockito.verify(mockDAO).actualizar(entity);
    }

    @Test
    public void actualizarPreservarAspiranteOpcionTest() {
        UUID id = UUID.randomUUID();
        UUID idAOExistente = UUID.randomUUID();

        // Escenario 1: El body trae AspiranteOpcion, pero su ID interno es nulo.
        PruebaJornadaAulaAspiranteOpcion entity1 = nuevaEntidad(id);
        entity1.setIdAspiranteOpcion(new AspiranteOpcion());

        // Escenario 2: El body viene con AspiranteOpcion totalmente en nulo.
        PruebaJornadaAulaAspiranteOpcion entity2 = nuevaEntidad(id);
        entity2.setIdAspiranteOpcion((AspiranteOpcion) null);

        PruebaJornadaAulaAspiranteOpcion existente = nuevaEntidad(id);
        existente.setIdAspiranteOpcion(new AspiranteOpcion(idAOExistente));
        existente.setIdPrueba(new Prueba(idPrueba));
        existente.setIdJornada(new Jornada(idJornada));
        existente.setIdAula(idAula);

        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(existente);

        // Prueba Escenario 1
        Response res1 = cut.actualizar(idPrueba, idJornada, idAula, id, entity1);
        assertEquals(400, res1.getStatus());

        // Prueba Escenario 2
        Response res2 = cut.actualizar(idPrueba, idJornada, idAula, id, entity2);
        assertEquals(400, res2.getStatus());
    }

    @Test
    public void actualizarValidacionFallidaTest() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        assertEquals(400, cut.actualizar(null, idJornada, idAula, id1, nuevaEntidad(id1)).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, null, idAula, id2, nuevaEntidad(id2)).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, null, id3, nuevaEntidad(id3)).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, idAula, UUID.randomUUID(), null).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, idAula, null, new PruebaJornadaAulaAspiranteOpcion()).getStatus());
    }

    @Test
    public void actualizarNoEncontradoTest() {
        UUID id = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcion entity = nuevaEntidad(id);
        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(null);

        assertEquals(404, cut.actualizar(idPrueba, idJornada, idAula, id, entity).getStatus());
    }

    @Test
    public void actualizarAspiranteOpcionNoEncontradoTest() {
        UUID id = UUID.randomUUID();
        UUID idAO = id;
        PruebaJornadaAulaAspiranteOpcion entity = nuevaEntidad(id);
        entity.setIdAspiranteOpcion(new AspiranteOpcion(idAO));

        PruebaJornadaAulaAspiranteOpcion existente = nuevaEntidad(id);

        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(existente);
        Mockito.when(mockAODAO.buscarPorId(idAO)).thenReturn(null); // Retorna nulo, detona el 404 interno

        assertEquals(404, cut.actualizar(idPrueba, idJornada, idAula, id, entity).getStatus());
    }

    @Test
    // Verifica 400 cuando el id del path y el idAspiranteOpcion del body no coinciden.
    public void actualizarIdAspiranteOpcionBodyDistintoTest() {
        UUID idPath = UUID.randomUUID();
        UUID idBody = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcion entity = nuevaEntidad(idBody);
        entity.setIdAspiranteOpcion(new AspiranteOpcion(idBody));

        Response response = cut.actualizar(idPrueba, idJornada, idAula, idPath, entity);

        assertEquals(400, response.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarExcepcionTest() {
        UUID id = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcion entity = nuevaEntidad(id);
        Mockito.when(mockDAO.buscarPorId(Mockito.any(PruebaJornadaAulaAspiranteOpcionPK.class)))
                .thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> cut.actualizar(idPrueba, idJornada, idAula, id, entity));
    }

    // ============================================================
    //  DELETE - eliminar
    // ============================================================

    @Test
    public void eliminarExitosoTest() {
        UUID id = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcion existente = nuevaEntidad(id);
        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(existente);

        assertEquals(204, cut.eliminar(idPrueba, idJornada, idAula, id).getStatus());
        Mockito.verify(mockDAO).eliminar(existente);
    }

    @Test
    public void eliminarValidacionFallidaTest() {
        assertEquals(400, cut.eliminar(null, idJornada, idAula, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, null, idAula, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, idJornada, null, UUID.randomUUID()).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, idJornada, idAula, null).getStatus());
    }

    @Test
    public void eliminarNoEncontradoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mockDAO.buscarPorId(new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, id))).thenReturn(null);
        assertEquals(404, cut.eliminar(idPrueba, idJornada, idAula, id).getStatus());
    }

    @Test
    public void eliminarExcepcionTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mockDAO.buscarPorId(Mockito.any(PruebaJornadaAulaAspiranteOpcionPK.class)))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> cut.eliminar(idPrueba, idJornada, idAula, id));
    }
}