package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.persistence.EntityManager;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Area;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Distractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.DistractorArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Pregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveArea;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPregunta;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractor;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClaveAreaPreguntaDistractorPK;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PruebaClaveAreaPreguntaDistractorDAOIT extends AbstractIntengrationDAOTest {

    PruebaClaveAreaPreguntaDistractorDAO cut; // Class under test

    private static class ContextoCompleto {
        PruebaClave pruebaClave;
        Area area;
        Pregunta pregunta;
        Distractor distractor;
    }

    @BeforeEach
    public void setup() {
        // Reinstancia del DAO por prueba para evitar arrastre de estado.
        cut = new PruebaClaveAreaPreguntaDistractorDAO();
        cut.em = em;
    }

    private TipoPrueba crearTipoPrueba(UUID uniq) {
        TipoPrueba tipoPrueba = new TipoPrueba(UUID.randomUUID());
        tipoPrueba.setValor("Tipo " + uniq);
        em.persist(tipoPrueba);
        return tipoPrueba;
    }

    private Prueba crearPrueba(TipoPrueba tipoPrueba, UUID uniq) {
        Prueba prueba = new Prueba(UUID.randomUUID());
        prueba.setNombre("Prueba " + uniq);
        prueba.setPuntajeMaximo(new BigDecimal("10.00"));
        prueba.setNotaAprobacion(new BigDecimal("6.00"));
        prueba.setFechaCreacion(OffsetDateTime.now());
        prueba.setIdTipoPrueba(tipoPrueba);
        em.persist(prueba);
        return prueba;
    }

    private PruebaClave crearPruebaClave() {
        UUID uniq = UUID.randomUUID();
        TipoPrueba tipoPrueba = crearTipoPrueba(uniq);
        Prueba prueba = crearPrueba(tipoPrueba, uniq);

        PruebaClave pruebaClave = new PruebaClave(UUID.randomUUID());
        pruebaClave.setNombreClave("Clave " + uniq);
        pruebaClave.setIdPrueba(prueba);
        em.persist(pruebaClave);

        return pruebaClave;
    }

    private Area crearArea() {
        Area area = new Area(UUID.randomUUID());
        area.setNombre("Area " + UUID.randomUUID());
        em.persist(area);
        return area;
    }

    private Pregunta crearPregunta() {
        Pregunta pregunta = new Pregunta(UUID.randomUUID());
        pregunta.setValor("Pregunta " + UUID.randomUUID());
        em.persist(pregunta);
        return pregunta;
    }

    private Distractor crearDistractor() {
        Distractor distractor = new Distractor(UUID.randomUUID());
        distractor.setValor("Distractor " + UUID.randomUUID());
        em.persist(distractor);
        return distractor;
    }

    private void crearPreguntaArea(Pregunta pregunta, Area area) {
        PreguntaArea preguntaArea = new PreguntaArea(pregunta, area);
        em.persist(preguntaArea);
    }

    private void crearDistractorArea(Distractor distractor, Area area) {
        DistractorArea distractorArea = new DistractorArea(distractor, area);
        em.persist(distractorArea);
    }

    private void crearPruebaClaveArea(PruebaClave pruebaClave, Area area) {
        PruebaClaveArea pruebaClaveArea = new PruebaClaveArea(pruebaClave, area);
        pruebaClaveArea.setCantidad(1);
        pruebaClaveArea.setPorcentaje(new BigDecimal("50.00"));
        em.persist(pruebaClaveArea);
    }

    private void crearPruebaClaveAreaPregunta(PruebaClave pruebaClave, Area area, Pregunta pregunta) {
        PruebaClaveAreaPregunta pruebaClaveAreaPregunta = new PruebaClaveAreaPregunta(
                pruebaClave,
                area,
                pregunta);
        pruebaClaveAreaPregunta.setPorcentaje(new BigDecimal("50.00"));
        em.persist(pruebaClaveAreaPregunta);
    }

    private void crearPreguntaDistractor(Pregunta pregunta, Distractor distractor) {
        PreguntaDistractor preguntaDistractor = new PreguntaDistractor(pregunta, distractor);
        preguntaDistractor.setCorrecto(Boolean.FALSE);
        em.persist(preguntaDistractor);
    }

    private PruebaClaveAreaPreguntaDistractor nuevaEntidad(PruebaClave pruebaClave, Area area, Pregunta pregunta,
            Distractor distractor) {
        return new PruebaClaveAreaPreguntaDistractor(
                pruebaClave,
                area,
                pregunta,
                distractor);
    }

    private ContextoCompleto crearContextoCompleto() {
        // Cadena mínima de datos requerida por FK hasta llegar a prueba_clave_area_pregunta.
        PruebaClave pruebaClave = crearPruebaClave();
        Area area = crearArea();
        Pregunta pregunta = crearPregunta();
        Distractor distractor = crearDistractor();
        crearPreguntaArea(pregunta, area);
        crearDistractorArea(distractor, area);
        crearPruebaClaveArea(pruebaClave, area);
        crearPruebaClaveAreaPregunta(pruebaClave, area, pregunta);

        ContextoCompleto ctx = new ContextoCompleto();
        ctx.pruebaClave = pruebaClave;
        ctx.area = area;
        ctx.pregunta = pregunta;
        ctx.distractor = distractor;
        crearPreguntaDistractor(ctx.pregunta, ctx.distractor);
        return ctx;
    }

    private PruebaClaveAreaPreguntaDistractor crearEntidadValidaPersistida() {
        // Helper para reutilizar un registro completo y válido en pruebas CRUD.
        ContextoCompleto ctx = crearContextoCompleto();

        PruebaClaveAreaPreguntaDistractor entidad = nuevaEntidad(
            ctx.pruebaClave,
            ctx.area,
            ctx.pregunta,
            ctx.distractor);
        em.persist(entidad);
        return entidad;
    }

    @Order(1)
    @Test
    public void testCrearExitoso() {
        System.out.println("Crear prueba_clave_area_pregunta_distractor exitoso");

        // Todo ocurre en una sola transacción para dejar datos consistentes.
        cut.em.getTransaction().begin();
        ContextoCompleto ctx = crearContextoCompleto();

        PruebaClaveAreaPreguntaDistractor nuevo = nuevaEntidad(
            ctx.pruebaClave,
            ctx.area,
            ctx.pregunta,
            ctx.distractor);

        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        Long resultado = cut.contar();
        assertEquals(1, resultado);
        System.out.println("Resultado: " + resultado);
    }

    @Order(2)
    @Test
    public void testCrearEntidadNula() {
        System.out.println("Crear PruebaClaveAreaPreguntaDistractor entidad null");
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.crear(null);
        }));
    }

    @Order(3)
    @Test
    public void testCrearEmNulo() {
        System.out.println("Crear PruebaClaveAreaPreguntaDistractor Entity manager null");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.crear(new PruebaClaveAreaPreguntaDistractor());
        }));
    }

    @Order(4)
    @Test
    public void testCountExitoso() {
        System.out.println("Contar prueba_clave_area_pregunta_distractor exitoso");
        Long resultado = cut.contar();
        assertEquals(resultado, 1);
        System.out.println("Resultado: " + resultado);
    }

    @Order(5)
    @Test
    public void testCountEmNulo() {
        System.out.println("Contar prueba_clave_area_pregunta_distractor Entity manager null");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> cut.contar()));
    }

    @Order(6)
    @Test
    public void testCountEmCerrado() {
        System.out.println("Contar prueba_clave_area_pregunta_distractor Entity manager cerrado");
        EntityManager emCerrado = emf.createEntityManager();
        emCerrado.close();
        cut.em = emCerrado;
        assertNotNull(assertThrows(IllegalStateException.class, () -> cut.contar()));
    }

    @Order(7)
    @Test
    public void testBuscarPorIdExitoso() {
        System.out.println("BuscarPorId prueba_clave_area_pregunta_distractor exitoso");

        cut.em.getTransaction().begin();
        PruebaClaveAreaPreguntaDistractor nuevo = crearEntidadValidaPersistida();
        cut.em.getTransaction().commit();

        PruebaClaveAreaPreguntaDistractorPK idBuscado = new PruebaClaveAreaPreguntaDistractorPK(
            nuevo.getIdPruebaClave().getIdPruebaClave(),
            nuevo.getIdArea().getIdArea(),
            nuevo.getIdPregunta().getIdPregunta(),
            nuevo.getIdDistractor().getIdDistractor());

        PruebaClaveAreaPreguntaDistractor encontrado = cut.buscarPorId(idBuscado);
        assertNotNull(encontrado);
        assertEquals(idBuscado.getIdPruebaClave(), encontrado.getIdPruebaClave().getIdPruebaClave());
        assertEquals(idBuscado.getIdArea(), encontrado.getIdArea().getIdArea());
        assertEquals(idBuscado.getIdPregunta(), encontrado.getIdPregunta().getIdPregunta());
        assertEquals(idBuscado.getIdDistractor(), encontrado.getIdDistractor().getIdDistractor());
        System.out.println("IdDistractor: " + idBuscado.getIdDistractor());
    }

    @Order(8)
    @Test
    public void testBuscarPorIdNulo() {
        System.out.println("BuscarPorId prueba_clave_area_pregunta_distractor null");
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorId(null);
        }));
    }

    @Order(9)
    @Test
    public void testBuscarPorIdEmNulo() {
        System.out.println("buscarPorId prueba_clave_area_pregunta_distractor em nulo");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorId(new PruebaClaveAreaPreguntaDistractorPK(
                    UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
        }));
    }

    @Order(10)
    @Test
    public void testEliminarNoExistente() {
        System.out.println("Eliminar PruebaClaveAreaPreguntaDistractor entidad no guardada");

        cut.em.getTransaction().begin();
        ContextoCompleto ctx = crearContextoCompleto();

        PruebaClaveAreaPreguntaDistractor eliminado = nuevaEntidad(
            ctx.pruebaClave,
            ctx.area,
            ctx.pregunta,
            ctx.distractor);

        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        PruebaClaveAreaPreguntaDistractorPK idEliminado = new PruebaClaveAreaPreguntaDistractorPK(
            ctx.pruebaClave.getIdPruebaClave(),
            ctx.area.getIdArea(),
            ctx.pregunta.getIdPregunta(),
            ctx.distractor.getIdDistractor());

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(11)
    @Test
    public void testEliminarExitoso() {
        System.out.println("Eliminar PruebaClaveAreaPreguntaDistractor exitoso");

        cut.em.getTransaction().begin();
        PruebaClaveAreaPreguntaDistractor eliminado = crearEntidadValidaPersistida();
        cut.em.getTransaction().commit();

        PruebaClaveAreaPreguntaDistractorPK idEliminado = new PruebaClaveAreaPreguntaDistractorPK(
            eliminado.getIdPruebaClave().getIdPruebaClave(),
            eliminado.getIdArea().getIdArea(),
            eliminado.getIdPregunta().getIdPregunta(),
            eliminado.getIdDistractor().getIdDistractor());

        cut.em.getTransaction().begin();
        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(12)
    @Test
    public void testEliminarEntidadNula() {
        System.out.println("eliminar prueba_clave_area_pregunta_distractor entidad nula");
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.eliminar(null);
        }));
    }

    @Order(13)
    @Test
    public void testEliminarEmNulo() {
        System.out.println("eliminar prueba_clave_area_pregunta_distractor em nulo");
        cut.em = null;
        PruebaClaveAreaPreguntaDistractor eliminado = new PruebaClaveAreaPreguntaDistractor();
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.eliminar(eliminado);
        }));
    }

    @Order(14)
    @Test
    public void testActualizarExitoso() {
        System.out.println("actualizar prueba_clave_area_pregunta_distractor exitoso");

        cut.em.getTransaction().begin();
        PruebaClaveAreaPreguntaDistractor nuevo = crearEntidadValidaPersistida();
        cut.em.getTransaction().commit();

        cut.em.getTransaction().begin();
        PruebaClaveAreaPreguntaDistractor actualizado = cut.actualizar(nuevo);
        cut.em.getTransaction().commit();

        assertNotNull(actualizado);
        assertEquals(nuevo.getIdPruebaClave(), actualizado.getIdPruebaClave());
        assertEquals(nuevo.getIdArea(), actualizado.getIdArea());
        assertEquals(nuevo.getIdPregunta(), actualizado.getIdPregunta());
        assertEquals(nuevo.getIdDistractor(), actualizado.getIdDistractor());
        System.out.println("IdDistractor actualizado: " + actualizado.getIdDistractor());
    }

    @Order(15)
    @Test
    public void testActualizarEntidadNula() {
        System.out.println("actualizar prueba_clave_area_pregunta_distractor entidad nula");
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.actualizar(null);
        }));
    }

    @Order(16)
    @Test
    public void testActualizarEmNulo() {
        System.out.println("actualizar prueba_clave_area_pregunta_distractor em nulo");
        cut.em = null;
        PruebaClaveAreaPreguntaDistractor actualizado = new PruebaClaveAreaPreguntaDistractor();

        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.actualizar(actualizado);
        }));
    }

    @Order(17)
    @Test
    public void testBuscarPorRangoExtitoso() {
        System.out.println("buscarPorRango - extitoso");
        Long esperado = cut.contar();

        List<PruebaClaveAreaPreguntaDistractor> resultados = cut.buscarPorRango(0, 50);
        assertNotNull(resultados);
        assertEquals(esperado, resultados.size());
    }

    @Order(18)
    @Test
    public void testBuscarPorRangoPametrosNoValidos() {
        System.out.println("buscarPorRango prueba_clave_area_pregunta_distractor parametros no validos");
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorRango(0, -1);
        }));
        assertNotNull(assertThrows(IllegalArgumentException.class, () -> {
            cut.buscarPorRango(-1, 50);
        }));
    }

    @Order(19)
    @Test
    public void testBuscarPorRangoEmNull() {
        System.out.println("buscarPorRango prueba_clave_area_pregunta_distractor em nulo");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorRango(0, 50);
        }));
    }

    @Order(20)
    @Test
    public void testBuscarPorPadreRangoExitoso() {
        System.out.println("buscarPorPadreRango - exitoso");

        cut.em.getTransaction().begin();
        ContextoCompleto ctx = crearContextoCompleto();

        PruebaClaveAreaPreguntaDistractor registro1 = nuevaEntidad(
            ctx.pruebaClave, ctx.area, ctx.pregunta, ctx.distractor);
        cut.crear(registro1);

        Distractor distractor2 = crearDistractor();
        crearDistractorArea(distractor2, ctx.area);
        crearPreguntaDistractor(ctx.pregunta, distractor2);
        PruebaClaveAreaPreguntaDistractor registro2 = nuevaEntidad(
            ctx.pruebaClave, ctx.area, ctx.pregunta, distractor2);
        cut.crear(registro2);

        // Registro de otro padre para validar que la consulta filtre correctamente.
        ContextoCompleto otroCtx = crearContextoCompleto();
        PruebaClaveAreaPreguntaDistractor otroRegistro = nuevaEntidad(
            otroCtx.pruebaClave, otroCtx.area, otroCtx.pregunta, otroCtx.distractor);
        cut.crear(otroRegistro);

        cut.em.getTransaction().commit();

        List<PruebaClaveAreaPreguntaDistractor> resultados = cut.buscarPorPadreRango(
            ctx.pruebaClave.getIdPruebaClave(),
            ctx.area.getIdArea(),
            ctx.pregunta.getIdPregunta(),
            0,
            10);

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        System.out.println("Resultado buscarPorPadreRango: " + resultados.size());
    }

    @Order(21)
    @Test
    public void testBuscarPorPadreRangoEmNulo() {
        System.out.println("buscarPorPadreRango em nulo");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.buscarPorPadreRango(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, 10);
        }));
    }

    @Order(22)
    @Test
    public void testContarPorPadreExitoso() {
        System.out.println("contarPorPadre - exitoso");

        cut.em.getTransaction().begin();
        ContextoCompleto ctx = crearContextoCompleto();

        PruebaClaveAreaPreguntaDistractor registro1 = nuevaEntidad(ctx.pruebaClave, ctx.area, ctx.pregunta, ctx.distractor);
        cut.crear(registro1);

        Distractor distractor2 = crearDistractor();
        crearDistractorArea(distractor2, ctx.area);
        crearPreguntaDistractor(ctx.pregunta, distractor2);
        PruebaClaveAreaPreguntaDistractor registro2 = nuevaEntidad(
            ctx.pruebaClave, ctx.area, ctx.pregunta, distractor2);
        cut.crear(registro2);

        // Segundo conjunto para comprobar que el conteo sea por padre y no global.
        ContextoCompleto otroCtx = crearContextoCompleto();
        PruebaClaveAreaPreguntaDistractor otroRegistro = nuevaEntidad(
            otroCtx.pruebaClave, otroCtx.area, otroCtx.pregunta, otroCtx.distractor);
        cut.crear(otroRegistro);

        cut.em.getTransaction().commit();

        Long resultado = cut.contarPorPadre(
            ctx.pruebaClave.getIdPruebaClave(),
            ctx.area.getIdArea(),
            ctx.pregunta.getIdPregunta());
        assertEquals(2L, resultado);
        System.out.println("Resultado contarPorPadre: " + resultado);
    }

    @Order(23)
    @Test
    public void testContarPorPadreEmNulo() {
        System.out.println("contarPorPadre em nulo");
        cut.em = null;
        assertNotNull(assertThrows(IllegalStateException.class, () -> {
            cut.contarPorPadre(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        }));
    }

    @Order(24)
    @Test
    public void testBuscarPorPadreRangoParametrosInvalidos() {
        System.out.println("buscarPorPadreRango parametros no validos");

        IllegalArgumentException exNulo = assertThrows(IllegalArgumentException.class,
                () -> cut.buscarPorPadreRango(null, UUID.randomUUID(), UUID.randomUUID(), 0, 10));
        assertEquals("Parámetros inválidos", exNulo.getMessage());

        IllegalArgumentException exAreaNula = assertThrows(IllegalArgumentException.class,
            () -> cut.buscarPorPadreRango(UUID.randomUUID(), null, UUID.randomUUID(), 0, 10));
        assertEquals("Parámetros inválidos", exAreaNula.getMessage());

        IllegalArgumentException exPreguntaNula = assertThrows(IllegalArgumentException.class,
            () -> cut.buscarPorPadreRango(UUID.randomUUID(), UUID.randomUUID(), null, 0, 10));
        assertEquals("Parámetros inválidos", exPreguntaNula.getMessage());

        IllegalArgumentException exFirst = assertThrows(IllegalArgumentException.class,
            () -> cut.buscarPorPadreRango(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), -1, 10));
        assertEquals("Parámetros inválidos", exFirst.getMessage());

        IllegalArgumentException exRango = assertThrows(IllegalArgumentException.class,
                () -> cut.buscarPorPadreRango(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, -1));
        assertEquals("Parámetros inválidos", exRango.getMessage());

        IllegalArgumentException exMaxCero = assertThrows(IllegalArgumentException.class,
            () -> cut.buscarPorPadreRango(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, 0));
        assertEquals("Parámetros inválidos", exMaxCero.getMessage());
    }

    @Order(25)
    @Test
    public void testContarPorPadreParametrosInvalidos() {
        System.out.println("contarPorPadre parametros no validos");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cut.contarPorPadre(UUID.randomUUID(), null, UUID.randomUUID()));
        assertEquals("Parámetros inválidos", ex.getMessage());

        IllegalArgumentException exPruebaNula = assertThrows(IllegalArgumentException.class,
            () -> cut.contarPorPadre(null, UUID.randomUUID(), UUID.randomUUID()));
        assertEquals("Parámetros inválidos", exPruebaNula.getMessage());

        IllegalArgumentException exPreguntaNula = assertThrows(IllegalArgumentException.class,
            () -> cut.contarPorPadre(UUID.randomUUID(), UUID.randomUUID(), null));
        assertEquals("Parámetros inválidos", exPreguntaNula.getMessage());
    }
}
