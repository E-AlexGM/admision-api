package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaClaveDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionExamenDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamenPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionPK;

public class PruebaJornadaAulaAspiranteOpcionExamenResourceTest {

    private UriInfo mockUriInfo;
    private PruebaJornadaAulaAspiranteOpcionExamenDAO mockDAO;
    private PruebaJornadaAulaAspiranteOpcionDAO mockPruebaOpcionDAO;
    private PruebaClaveDAO mockPruebaClaveDAO;
    private PruebaJornadaAulaAspiranteOpcionExamenResource cut;
    private UUID idPrueba;
    private UUID idJornada;
    private String idAula;
    private UUID idAspiranteOpcion;

    @BeforeEach
    public void setup() {
        mockUriInfo = Mockito.mock(UriInfo.class);
        UriBuilder mockUriBuilder = Mockito.mock(UriBuilder.class);
        mockDAO = Mockito.mock(PruebaJornadaAulaAspiranteOpcionExamenDAO.class);
        mockPruebaOpcionDAO = Mockito.mock(PruebaJornadaAulaAspiranteOpcionDAO.class);
        mockPruebaClaveDAO = Mockito.mock(PruebaClaveDAO.class);

        Mockito.when(mockUriInfo.getAbsolutePathBuilder()).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.path(Mockito.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.build()).thenReturn(URI.create("http://localhost/1"));

        idPrueba = UUID.randomUUID();
        idJornada = UUID.randomUUID();
        idAula = "A-01";
        idAspiranteOpcion = UUID.randomUUID();

        cut = new PruebaJornadaAulaAspiranteOpcionExamenResource();
        cut.pruebaJornadaAulaAspiranteOpcionExamenDAO = mockDAO;
        cut.pruebaJornadaAulaAspiranteOpcionDAO = mockPruebaOpcionDAO;
        cut.pruebaClaveDAO = mockPruebaClaveDAO;
    }

    private PruebaJornadaAulaAspiranteOpcionExamen nuevaEntidadValida() {
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("9.5"));
        entity.setIdPruebaClave(new PruebaClave(UUID.randomUUID()));
        return entity;
    }

    private PruebaJornadaAulaAspiranteOpcion padreValido() {
        return new PruebaJornadaAulaAspiranteOpcion(
                new Prueba(idPrueba),
                new Jornada(idJornada),
                idAula,
                new AspiranteOpcion(idAspiranteOpcion));
    }

    private PruebaJornadaAulaAspiranteOpcionExamen examenValido() {
        return new PruebaJornadaAulaAspiranteOpcionExamen(
                new Prueba(idPrueba),
                new Jornada(idJornada),
                idAula,
                new AspiranteOpcion(idAspiranteOpcion));
    }

    private PruebaJornadaAulaAspiranteOpcionPK padrePk() {
        return new PruebaJornadaAulaAspiranteOpcionPK(idPrueba, idJornada, idAula, idAspiranteOpcion);
    }

    private PruebaJornadaAulaAspiranteOpcionExamenPK examenPk() {
        return new PruebaJornadaAulaAspiranteOpcionExamenPK(idPrueba, idJornada, idAula, idAspiranteOpcion);
    }

    // ============================================================
    //  POST - crear
    // ============================================================

    @Test
    public void crearExitosoTest() {
        System.out.println("Ejecutando test: crearExitosoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();

        Mockito.when(mockPruebaOpcionDAO.buscarPorId(padrePk())).thenReturn(padreValido());
        Mockito.when(mockPruebaClaveDAO.buscarPorId(entity.getIdPruebaClave().getIdPruebaClave()))
            .thenReturn(new PruebaClave(entity.getIdPruebaClave().getIdPruebaClave()));

        Response res = cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entity, mockUriInfo);

        assertEquals(201, res.getStatus());
        assertNotNull(res.getLocation());
        assertEquals(idPrueba, entity.getIdPrueba().getIdPrueba());
        assertEquals(idJornada, entity.getIdJornada().getIdJornada());
        assertEquals(idAula, entity.getIdAula());
        assertEquals(idAspiranteOpcion, entity.getIdAspiranteOpcion().getIdAspiranteOpcion());
        Mockito.verify(mockDAO).crear(entity);
    }

    @Test
    public void crearParametrosNullTest() {
        System.out.println("Ejecutando test: crearParametrosNullTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        assertEquals(400, cut.crear(null, idJornada, idAula, idAspiranteOpcion, nuevaEntidadValida(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, null, idAula, idAspiranteOpcion, nuevaEntidadValida(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, idJornada, null, idAspiranteOpcion, nuevaEntidadValida(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, null, nuevaEntidadValida(), mockUriInfo).getStatus());
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, null, mockUriInfo).getStatus());

        PruebaJornadaAulaAspiranteOpcionExamen entitySinResultado = new PruebaJornadaAulaAspiranteOpcionExamen();
        entitySinResultado.setIdPruebaClave(new PruebaClave(UUID.randomUUID()));
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entitySinResultado, mockUriInfo).getStatus());

        
        PruebaJornadaAulaAspiranteOpcionExamen entitySinClave = new PruebaJornadaAulaAspiranteOpcionExamen();
        entitySinClave.setResultado(BigDecimal.TEN);
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entitySinClave, mockUriInfo).getStatus());

        PruebaJornadaAulaAspiranteOpcionExamen entityConClaveSinId = new PruebaJornadaAulaAspiranteOpcionExamen();
        entityConClaveSinId.setResultado(BigDecimal.TEN);
        entityConClaveSinId.setIdPruebaClave(new PruebaClave());
        assertEquals(400, cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entityConClaveSinId, mockUriInfo).getStatus());
    }

    @Test
    public void crearPadreNoEncontradoTest() {
        System.out.println("Ejecutando test: crearPadreNoEncontradoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();

        Mockito.when(mockPruebaOpcionDAO.buscarPorId(padrePk())).thenReturn(null);

        Response res = cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entity, mockUriInfo);
        assertEquals(404, res.getStatus());
    }

    @Test
    public void crearDuplicadoTest() {
        System.out.println("Ejecutando test: crearDuplicadoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();

        Mockito.when(mockPruebaOpcionDAO.buscarPorId(padrePk())).thenReturn(padreValido());
        Mockito.when(mockDAO.buscarPorId(examenPk()))
            .thenReturn(examenValido());

        Response res = cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entity, mockUriInfo);

        assertEquals(409, res.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).crear(Mockito.any());
    }

    @Test
    public void crearPruebaClaveNoEncontradaTest() {
        System.out.println("Ejecutando test: crearPruebaClaveNoEncontradaTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();

        Mockito.when(mockPruebaOpcionDAO.buscarPorId(padrePk())).thenReturn(padreValido());
        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(null);
        Mockito.when(mockPruebaClaveDAO.buscarPorId(entity.getIdPruebaClave().getIdPruebaClave())).thenReturn(null);

        Response res = cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entity, mockUriInfo);

        assertEquals(404, res.getStatus());
    }

    @Test
    public void crearExcepcionTest() {
        System.out.println("Ejecutando test: crearExcepcionTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();

        Mockito.when(mockPruebaOpcionDAO.buscarPorId(Mockito.any())).thenThrow(new RuntimeException("Error BD"));

        Response res = cut.crear(idPrueba, idJornada, idAula, idAspiranteOpcion, entity, mockUriInfo);
        assertEquals(500, res.getStatus());
    }

    // ============================================================
    //  GET - buscarPorPadre
    // ============================================================

    @Test
    public void buscarPorPadreExitosoTest() {
        System.out.println("Ejecutando test: buscarPorPadreExitosoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(examenPk()))
            .thenReturn(examenValido());

        Response res = cut.buscarPorPadre(idPrueba, idJornada, idAula, idAspiranteOpcion);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void buscarPorPadreNoEncontradoTest() {
        System.out.println("Ejecutando test: buscarPorPadreNoEncontradoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(null);

        Response res = cut.buscarPorPadre(idPrueba, idJornada, idAula, idAspiranteOpcion);
        assertEquals(404, res.getStatus());
    }

    @Test
    public void buscarPorPadreParametrosNullTest() {
        System.out.println("Ejecutando test: buscarPorPadreParametrosNullTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        assertEquals(400, cut.buscarPorPadre(null, idJornada, idAula, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.buscarPorPadre(idPrueba, null, idAula, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.buscarPorPadre(idPrueba, idJornada, null, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.buscarPorPadre(idPrueba, idJornada, idAula, null).getStatus());
    }

    @Test
    public void buscarPorPadreExcepcionTest() {
        System.out.println("Ejecutando test: buscarPorPadreExcepcionTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(Mockito.any()))
                .thenThrow(new RuntimeException("Error BD"));

        Response res = cut.buscarPorPadre(idPrueba, idJornada, idAula, idAspiranteOpcion);
        assertEquals(500, res.getStatus());
    }

    // ============================================================
    //  PUT - actualizar
    // ============================================================

    @Test
    public void actualizarExitosoConPruebaClaveEnBodyTest() {
        System.out.println("Ejecutando test: actualizarExitosoConPruebaClaveEnBodyTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        UUID idPruebaClave = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("8.5"));
        entity.setIdPruebaClave(new PruebaClave(idPruebaClave));

        PruebaJornadaAulaAspiranteOpcionExamen existente = examenValido();
        existente.setIdPruebaClave(new PruebaClave(UUID.randomUUID()));

        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(existente);
        Mockito.when(mockPruebaClaveDAO.buscarPorId(idPruebaClave)).thenReturn(new PruebaClave(idPruebaClave));

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, entity);

        assertEquals(200, res.getStatus());
        assertEquals(idPrueba, entity.getIdPrueba().getIdPrueba());
        assertEquals(idJornada, entity.getIdJornada().getIdJornada());
        assertEquals(idAula, entity.getIdAula());
        assertEquals(idAspiranteOpcion, entity.getIdAspiranteOpcion().getIdAspiranteOpcion());
        assertEquals(idPruebaClave, entity.getIdPruebaClave().getIdPruebaClave());
        Mockito.verify(mockDAO).actualizar(entity);
    }

    @Test
    public void actualizarExitosoSinPruebaClaveEnBodyTest() {
        System.out.println("Ejecutando test: actualizarExitosoSinPruebaClaveEnBodyTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("8.0"));

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, entity);

        assertEquals(400, res.getStatus());

        PruebaJornadaAulaAspiranteOpcionExamen entityConClaveSinId = new PruebaJornadaAulaAspiranteOpcionExamen();
        entityConClaveSinId.setResultado(new BigDecimal("8.0"));
        entityConClaveSinId.setIdPruebaClave(new PruebaClave());
        Response resConClaveSinId = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, entityConClaveSinId);

        assertEquals(400, resConClaveSinId.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarParametrosNullTest() {
        System.out.println("Ejecutando test: actualizarParametrosNullTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        assertEquals(400, cut.actualizar(null, idJornada, idAula, idAspiranteOpcion, nuevaEntidadValida()).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, null, idAula, idAspiranteOpcion, nuevaEntidadValida()).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, null, idAspiranteOpcion, nuevaEntidadValida()).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, idAula, null, nuevaEntidadValida()).getStatus());
        assertEquals(400, cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, null).getStatus());
    }

    @Test
    public void actualizarNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarNoEncontradoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(null);

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, nuevaEntidadValida());
        assertEquals(404, res.getStatus());
    }

    @Test
    public void actualizarPruebaClaveNoEncontradaTest() {
        System.out.println("Ejecutando test: actualizarPruebaClaveNoEncontradaTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = nuevaEntidadValida();
        PruebaJornadaAulaAspiranteOpcionExamen existente = examenValido();

        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(existente);
        Mockito.when(mockPruebaClaveDAO.buscarPorId(entity.getIdPruebaClave().getIdPruebaClave())).thenReturn(null);

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, entity);
        assertEquals(404, res.getStatus());
    }

    @Test
    public void actualizarExcepcionEnBuscarTest() {
        System.out.println("Ejecutando test: actualizarExcepcionEnBuscarTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(Mockito.any()))
                .thenThrow(new RuntimeException("Error BD"));

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, nuevaEntidadValida());
        assertEquals(500, res.getStatus());
    }

    @Test
    public void actualizarExcepcionEnActualizarTest() {
        System.out.println("Ejecutando test: actualizarExcepcionEnActualizarTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("6.5"));
        UUID idPruebaClave = UUID.randomUUID();
        entity.setIdPruebaClave(new PruebaClave(idPruebaClave));

        PruebaJornadaAulaAspiranteOpcionExamen existente = examenValido();
        existente.setIdPruebaClave(new PruebaClave(UUID.randomUUID()));

        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(existente);
        Mockito.when(mockPruebaClaveDAO.buscarPorId(idPruebaClave)).thenReturn(new PruebaClave(idPruebaClave));
        Mockito.doThrow(new RuntimeException("Error BD")).when(mockDAO).actualizar(entity);

        Response res = cut.actualizar(idPrueba, idJornada, idAula, idAspiranteOpcion, entity);
        assertEquals(500, res.getStatus());
    }

    // ============================================================
    //  DELETE - eliminar
    // ============================================================

    @Test
    public void eliminarExitosoTest() {
        System.out.println("Ejecutando test: eliminarExitosoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen existente = examenValido();
        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(existente);

        Response res = cut.eliminar(idPrueba, idJornada, idAula, idAspiranteOpcion);

        assertEquals(204, res.getStatus());
        Mockito.verify(mockDAO).eliminar(existente);
    }

    @Test
    public void eliminarParametrosNullTest() {
        System.out.println("Ejecutando test: eliminarParametrosNullTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        assertEquals(400, cut.eliminar(null, idJornada, idAula, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, null, idAula, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, idJornada, null, idAspiranteOpcion).getStatus());
        assertEquals(400, cut.eliminar(idPrueba, idJornada, idAula, null).getStatus());
    }

    @Test
    public void eliminarNoEncontradoTest() {
        System.out.println("Ejecutando test: eliminarNoEncontradoTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(examenPk())).thenReturn(null);

        Response res = cut.eliminar(idPrueba, idJornada, idAula, idAspiranteOpcion);
        assertEquals(404, res.getStatus());
    }

    @Test
    public void eliminarExcepcionTest() {
        System.out.println("Ejecutando test: eliminarExcepcionTest en PruebaJornadaAulaAspiranteOpcionExamenResource");
        Mockito.when(mockDAO.buscarPorId(Mockito.any()))
                .thenThrow(new RuntimeException("Error BD"));

        Response res = cut.eliminar(idPrueba, idJornada, idAula, idAspiranteOpcion);
        assertEquals(500, res.getStatus());
    }
}
