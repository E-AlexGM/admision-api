package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenDefaultStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioPrimeraRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.IngresoUniversitarioSegundaRondaStrategy;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.PruebaJornadaAulaAspiranteOpcionExamenDAO;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.TipoPruebaEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

public class AspiranteExamenResourceTest {

    private PruebaJornadaAulaAspiranteOpcionExamenDAO mockDAO;
    private AspiranteExamenResource cut;

    private UUID idAspirante;
    private UUID idPrueba;

    PruebaDAO mockPD;
    PruebaJornadaAulaAspiranteOpcionExamenDAO mockPJAOED;
    ExamenDefaultStrategy mockStrategy;


    @BeforeEach
    public void setup() {
        // Crear los mocks primero
        mockDAO = Mockito.mock(PruebaJornadaAulaAspiranteOpcionExamenDAO.class);
        mockPD = Mockito.mock(PruebaDAO.class);
        mockPJAOED = Mockito.mock(PruebaJornadaAulaAspiranteOpcionExamenDAO.class);
        mockStrategy = Mockito.mock(ExamenDefaultStrategy.class);

        // Crear la instancia del cut
        cut = new AspiranteExamenResource();

        // Inyectar los mocks
        cut.pruebaJornadaAulaAspiranteOpcionExamenDAO = mockDAO;
        cut.pruebaDAO = mockPD;
        cut.estrategiasEstado = new HashMap<>();
        cut.examenDefaultStrategy = mockStrategy;

        // Inicializar las variables de prueba
        idAspirante = UUID.randomUUID();
        idPrueba = UUID.randomUUID();
    }

    @Test
    public void actualizarResultadoExitosoTest() {
        System.out.println("Ejecutando test: actualizarResultadoExitosoTest en AspiranteExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("8.75"));

        PruebaJornadaAulaAspiranteOpcionExamen existente = new PruebaJornadaAulaAspiranteOpcionExamen();
        existente.setResultado(new BigDecimal("5.00"));
        existente.setFechaResultado(null);

        Mockito.when(mockDAO.obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba)).thenReturn(existente);

        Response resultado = cut.actualizarResultado(idAspirante, idPrueba, entity);

        assertEquals(200, resultado.getStatus());
        assertEquals(0, existente.getResultado().compareTo(new BigDecimal("8.75")));
        assertNotNull(existente.getFechaResultado());
        assertEquals(existente, resultado.getEntity());
        Mockito.verify(mockDAO).actualizar(existente);
    }

    @Test
    public void actualizarResultadoParametrosInvalidosTest() {
        System.out.println("Ejecutando test: actualizarResultadoParametrosInvalidosTest en AspiranteExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen conResultado = new PruebaJornadaAulaAspiranteOpcionExamen();
        conResultado.setResultado(new BigDecimal("7.25"));

        PruebaJornadaAulaAspiranteOpcionExamen sinResultado = new PruebaJornadaAulaAspiranteOpcionExamen();

        assertEquals(400, cut.actualizarResultado(null, idPrueba, conResultado).getStatus());
        assertEquals(400, cut.actualizarResultado(idAspirante, null, conResultado).getStatus());
        assertEquals(400, cut.actualizarResultado(idAspirante, idPrueba, null).getStatus());
        assertEquals(400, cut.actualizarResultado(idAspirante, idPrueba, sinResultado).getStatus());

        Mockito.verifyNoInteractions(mockDAO);
    }

    @Test
    public void actualizarResultadoNoEncontradoTest() {
        System.out.println("Ejecutando test: actualizarResultadoNoEncontradoTest en AspiranteExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("6.00"));

        Mockito.when(mockDAO.obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba)).thenReturn(null);

        Response resultado = cut.actualizarResultado(idAspirante, idPrueba, entity);

        assertEquals(404, resultado.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarResultadoExcepcionAlBuscarTest() {
        System.out.println("Ejecutando test: actualizarResultadoExcepcionAlBuscarTest en AspiranteExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("8.00"));

        Mockito.when(mockDAO.obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba))
                .thenThrow(new IllegalStateException("Error al buscar"));

        Response resultado = cut.actualizarResultado(idAspirante, idPrueba, entity);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAO, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    public void actualizarResultadoExcepcionAlActualizarTest() {
        System.out.println("Ejecutando test: actualizarResultadoExcepcionAlActualizarTest en AspiranteExamenResource");
        PruebaJornadaAulaAspiranteOpcionExamen entity = new PruebaJornadaAulaAspiranteOpcionExamen();
        entity.setResultado(new BigDecimal("9.00"));

        PruebaJornadaAulaAspiranteOpcionExamen existente = new PruebaJornadaAulaAspiranteOpcionExamen();
        existente.setResultado(new BigDecimal("4.00"));
        existente.setFechaResultado(OffsetDateTime.now().minusDays(1));

        Mockito.when(mockDAO.obtenerResultadoExamenPorAspiranteYPrueba(idAspirante, idPrueba)).thenReturn(existente);
        Mockito.doThrow(new IllegalArgumentException("Error al actualizar")).when(mockDAO).actualizar(existente);

        Response resultado = cut.actualizarResultado(idAspirante, idPrueba, entity);

        assertEquals(500, resultado.getStatus());
        Mockito.verify(mockDAO).actualizar(existente);
    }

    @Test
    public void listarResultadoExamenExitosoTest() {
        UUID idA = UUID.randomUUID();
        UUID idP = UUID.randomUUID();
        Prueba p = new Prueba();
        p.setIdPrueba(idP);
        p.setIdTipoPrueba(new TipoPrueba(UUID.randomUUID()));
        PruebaJornadaAulaAspiranteOpcionExamen ex = new PruebaJornadaAulaAspiranteOpcionExamen();
        ex.setIdPrueba(p);

        Mockito.when(cut.pruebaJornadaAulaAspiranteOpcionExamenDAO.obtenerResultadoExamenPorAspiranteYPrueba(idA, idP)).thenReturn(ex);
        Mockito.when(cut.examenDefaultStrategy.obtenerEstado(Mockito.any(), Mockito.any())).thenReturn(ExamenResultadosEnum.APROBADO);

        Response res = cut.buscarResultadoExamen(idA, idP);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void listarResultadoExamenNullTest() {
        Response res = cut.buscarResultadoExamen(null, null);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void listarResultadoExamenIdPruebaNullTest() {
        Response res = cut.buscarResultadoExamen(UUID.randomUUID(), null);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void listarResultadoExamenNoEncontradoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(cut.pruebaJornadaAulaAspiranteOpcionExamenDAO.obtenerResultadoExamenPorAspiranteYPrueba(id, id)).thenReturn(null);
        Response res = cut.buscarResultadoExamen(id, id);
        assertEquals(404, res.getStatus());
    }


    @Test
    public void listarResultadoExamenSinPruebaEnResultadoTest() {
        UUID id = UUID.randomUUID();
        PruebaJornadaAulaAspiranteOpcionExamen ex = new PruebaJornadaAulaAspiranteOpcionExamen();
        ex.setIdPrueba(null);

        Mockito.when(cut.pruebaJornadaAulaAspiranteOpcionExamenDAO.obtenerResultadoExamenPorAspiranteYPrueba(id, id)).thenReturn(ex);

        Response res = cut.buscarResultadoExamen(id, id);
        assertEquals(404, res.getStatus());
    }

    @Test
    public void listarResultadoExamenErrorTest() {
        Mockito.when(cut.pruebaJornadaAulaAspiranteOpcionExamenDAO.obtenerResultadoExamenPorAspiranteYPrueba(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException());
        Response res = cut.buscarResultadoExamen(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(500, res.getStatus());
    }

    @Test
    public void listarPruebasPorAspiranteExitosoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(cut.pruebaDAO.findByIdAspirante(id)).thenReturn(List.of(new Prueba()));
        Response res = cut.listarPruebasPorAspirante(id);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void listarPruebasPorAspiranteNullTest() {
        Response res = cut.listarPruebasPorAspirante(null);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void listarPruebasPorAspiranteVacioTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(cut.pruebaDAO.findByIdAspirante(id)).thenReturn(Collections.emptyList());
        Response res = cut.listarPruebasPorAspirante(id);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void listarPruebasPorAspiranteErrorTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(cut.pruebaDAO.findByIdAspirante(id)).thenThrow(new RuntimeException());
        Response res = cut.listarPruebasPorAspirante(id);
        assertEquals(500, res.getStatus());
    }

    @Test
    public void listarPruebasPorAspiranteNullResultadoTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(cut.pruebaDAO.findByIdAspirante(id)).thenReturn(null);
        Response res = cut.listarPruebasPorAspirante(id);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void inicializarTest() {
        cut.ingresoUniversitarioPrimeraRondaStrategy = Mockito.mock(IngresoUniversitarioPrimeraRondaStrategy.class);
        cut.ingresoUniversitarioSegundaRondaStrategy = Mockito.mock(IngresoUniversitarioSegundaRondaStrategy.class);

        cut.inicializar();

        assertNotNull(cut.estrategiasEstado);
        assertEquals(2, cut.estrategiasEstado.size());
        assertEquals(cut.ingresoUniversitarioPrimeraRondaStrategy,
                cut.estrategiasEstado.get(TipoPruebaEnum.INGRESO_UNIVERSITARIO_PRIMERA_RONDA.name()));
        assertEquals(cut.ingresoUniversitarioSegundaRondaStrategy,
                cut.estrategiasEstado.get(TipoPruebaEnum.INGRESO_UNIVERSITARIO_SEGUNDA_RONDA.name()));
    }



}
