package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.persistence.EntityManager;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AspiranteDAOIT extends AbstractIntengrationDAOTest{

    AspiranteDAO cut; // Class under test

    @BeforeEach
    public void setup() {
        cut = new AspiranteDAO();
        cut.em = em;
    }

    @Order(1)
    @Test
    public void testCrearExitoso(){
        System.out.println("Crear aspirante exitoso");
        Aspirante nuevo = new Aspirante(UUID.randomUUID());
        nuevo.setNombres("Aspirante 1");
        nuevo.setApellidos("Aspirante 1");
        nuevo.setCorreo("aspirante@gmail.com");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevo.setFechaCreacion(OffsetDateTime.now());

        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        Long resultado = cut.contar();
        assertEquals(1, resultado);
        System.out.println("Resultado: " + resultado);
    }

    @Order(2)
    @Test
    public void testCrearEntidadNula() {
        System.out.println("Crear Aspirante entidad null");
        // Dejamos que falle por la validación de la entidad nula
        assertThrows(IllegalArgumentException.class, () -> { cut.crear(null); });
    }

    @Order(3)
    @Test
    public void testCrearEmNulo() {
        System.out.println("Crear Aspirante Entity manager null");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> { cut.crear(new Aspirante(UUID.randomUUID())); });
    }

    @Order(4)
    @Test
    public void testCountExitoso() {
        System.out.println("Contar aspirante exitoso");
        Long resultado = cut.contar();
        assertEquals(resultado, 1);
        System.out.println("Resultado: " + resultado);
    }

    @Order(5)
    @Test
    public void testCountEmNulo() {
        System.out.println("Contar aspirante Entity manager null");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> cut.contar());
    }

    @Order(6)
    @Test
    public void testCountEmCerrado() {
        System.out.println("Contar aspirante Entity manager cerrado");
        EntityManager emCerrado = emf.createEntityManager();
        emCerrado.close();
        cut.em = emCerrado;
        assertThrows(IllegalStateException.class, () -> cut.contar());
    }

    @Order(7)
    @Test
    public void testBuscarPorIdExitoso() {
        System.out.println("BuscarPorId aspirante exitoso");
        UUID idBuscado = UUID.randomUUID();
        Aspirante nuevo = new Aspirante(idBuscado);
        nuevo.setNombres("Aspirante 1");
        nuevo.setApellidos("Aspirante 1");
        nuevo.setCorreo("aspirante2@gmail.com");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevo.setFechaCreacion(OffsetDateTime.now());

        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        Aspirante encontrado = cut.buscarPorId(idBuscado);
        assertNotNull(encontrado);
        assertEquals(idBuscado, encontrado.getIdAspirante());
        assertEquals(nuevo.getNombres(), encontrado.getNombres());
        assertEquals(nuevo.getApellidos(), encontrado.getApellidos());
        assertEquals(nuevo.getCorreo(), encontrado.getCorreo());
        assertEquals(nuevo.getFechaNacimiento(), encontrado.getFechaNacimiento());
        assertEquals(nuevo.getFechaCreacion(), encontrado.getFechaCreacion());
        System.out.println("Id: " + idBuscado);
    }

    @Order(8)
    @Test
    public void testBuscarPorIdNulo() {
        System.out.println("BuscarPorId aspirante null");
        assertThrows(IllegalArgumentException.class, () -> { cut.buscarPorId(null); });
    }

    @Order(9)
    @Test
    public void testBuscarPorIdEmNulo() {
        System.out.println("buscarPorId aspirante em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> { cut.buscarPorId(UUID.randomUUID()); });
    }

    @Order(10)
    @Test
    public void testEliminarNoExistente() {
        System.out.println("Eliminar Aspirante entidad no guardada");
        UUID idEliminado = UUID.randomUUID();
        Aspirante eliminado = new Aspirante(idEliminado);
        eliminado.setNombres("Aspirante 1");
        eliminado.setApellidos("Aspirante 1");
        eliminado.setCorreo("aspirante3@gmail.com");
        eliminado.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        eliminado.setFechaCreacion(OffsetDateTime.now());

        cut.em.getTransaction().begin();
        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(11)
    @Test
    public void testEliminarExitoso() {
        System.out.println("Eliminar Aspirante exitoso");
        UUID idEliminado = UUID.randomUUID();
        Aspirante eliminado = new Aspirante(idEliminado);
        eliminado.setNombres("Aspirante 1");
        eliminado.setApellidos("Aspirante 1");
        eliminado.setCorreo("aspirante4@gmail.com");
        eliminado.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        eliminado.setFechaCreacion(OffsetDateTime.now());

        // Guardamos
        cut.em.getTransaction().begin();
        cut.crear(eliminado);
        cut.em.getTransaction().commit();

        // eliminamos
        cut.em.getTransaction().begin();
        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(12)
    @Test
    public void testEliminarEntidadNula() {
        System.out.println("eliminar aspirante entidad nula");
        assertThrows(IllegalArgumentException.class, () -> { cut.eliminar(null); });
    }

    @Order(13)
    @Test
    public void testEliminarEmNulo() {
        System.out.println("eliminar aspirante em nulo");
        cut.em = null;
        Aspirante eliminado = new Aspirante(UUID.randomUUID());
        assertThrows(IllegalStateException.class, () -> { cut.eliminar(eliminado); });
    }

    @Order(14)
    @Test
    public void testActualizarExitoso(){
        System.out.println("actualizar aspirante exitoso");
        Aspirante nuevo = new Aspirante(UUID.randomUUID());
        nuevo.setNombres("Aspirante 1");
        nuevo.setApellidos("Aspirante 1");
        nuevo.setCorreo("aspirante5@gmail.com");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        nuevo.setFechaCreacion(OffsetDateTime.now());


        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        // Modificar la entidad
        nuevo.setNombres("Actualizado");
        cut.em.getTransaction().begin();
        Aspirante actualizado = cut.actualizar(nuevo);
        cut.em.getTransaction().commit();

        assertNotNull(actualizado);
        assertEquals(nuevo.getNombres(), actualizado.getNombres());
        System.out.println("Nombre actualizado: " + actualizado.getNombres());
        // fail("Esta prueba falló");
    }

    @Order(15)
    @Test
    public void testActualizarEntidadNula() {
        System.out.println("actualizar aspirante entidad nula");
        assertThrows(IllegalArgumentException.class, () -> { cut.actualizar(null); });
        // fail("Esta prueba falló");
    }

    @Order(16)
    @Test
    public void testActualizarEmNulo() {
        System.out.println("actualizar aspirante em nulo");
        // Hacemos la entity manager null
        cut.em = null;
        Aspirante actualizado = new Aspirante(UUID.randomUUID());

        assertThrows(IllegalStateException.class, () -> { cut.actualizar(actualizado); });
        // fail("Esta prueba falló");
    }

    @Order(17)
    @Test
    public void testBuscarPorRangoExtitoso(){
        System.out.println("buscarPorRango - extitoso");
        Long esperado = cut.contar();

        List<Aspirante> aspirantes = cut.buscarPorRango(0, 50);
        assertNotNull(aspirantes);
        assertEquals(esperado, aspirantes.size());
        // fail("Esta prueba falló");
    }

    @Order(18)
    @Test
    public void testBuscarPorRangoPametrosNoValidos() {
        System.out.println("buscarPorRango aspirante parametros no validos");
        assertThrows(IllegalArgumentException.class, () -> {cut.buscarPorRango(0, -1);});
        assertThrows(IllegalArgumentException.class, () -> {cut.buscarPorRango(-1, 50);});
        //fail("Esta prueba falló");
    }

    @Order(19)
    @Test
    public void testBuscarPorRangoEmNull() {
        System.out.println("buscarPorRango aspirante parametros no validos");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> {cut.buscarPorRango(0, 50);});
        //fail("Esta prueba falló");
    }

    @Order(20)
    @Test
    public void testBuscarPorApellidosExitoso() {
        System.out.println("buscarPorApellidos aspirante exitoso");
        String apellidos = "APELLIDO-BUSQUEDA-" + UUID.randomUUID().toString().substring(0, 8);

        Aspirante aspirante1 = new Aspirante(UUID.randomUUID());
        aspirante1.setNombres("Aspirante A");
        aspirante1.setApellidos(apellidos);
        aspirante1.setCorreo("aspirante.apellido.a." + UUID.randomUUID().toString().substring(0, 6) + "@gmail.com");
        aspirante1.setFechaNacimiento(LocalDate.of(2001, 5, 5));
        aspirante1.setFechaCreacion(OffsetDateTime.now());

        Aspirante aspirante2 = new Aspirante(UUID.randomUUID());
        aspirante2.setNombres("Aspirante B");
        aspirante2.setApellidos(apellidos);
        aspirante2.setCorreo("aspirante.apellido.b." + UUID.randomUUID().toString().substring(0, 6) + "@gmail.com");
        aspirante2.setFechaNacimiento(LocalDate.of(2002, 6, 6));
        aspirante2.setFechaCreacion(OffsetDateTime.now());

        cut.em.getTransaction().begin();
        cut.crear(aspirante1);
        cut.crear(aspirante2);
        cut.em.getTransaction().commit();

        List<Aspirante> encontrados = cut.buscarPorApellidos(apellidos);
        assertNotNull(encontrados);
        assertTrue(encontrados.size() >= 2);
        assertTrue(encontrados.stream().allMatch(a -> apellidos.equals(a.getApellidos())));
    }

    @Order(21)
    @Test
    public void testBuscarPorApellidosSinResultados() {
        System.out.println("buscarPorApellidos aspirante sin resultados");
        String apellidosInexistentes = "APELLIDO-INEXISTENTE-" + UUID.randomUUID().toString().substring(0, 8);

        List<Aspirante> encontrados = cut.buscarPorApellidos(apellidosInexistentes);
        assertNull(encontrados);
    }

    @Order(23)
    @Test
    public void testBuscarPorCorreoExitoso() {
        System.out.println("buscarPorCorreo aspirante exitoso");

        String correo = "correo-busqueda-" + UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";
        Aspirante aspirante = new Aspirante(UUID.randomUUID());
        aspirante.setNombres("Aspirante Correo");
        aspirante.setApellidos("Busqueda Correo");
        aspirante.setCorreo(correo);
        aspirante.setFechaNacimiento(LocalDate.of(2000, 7, 7));
        aspirante.setFechaCreacion(OffsetDateTime.now());

        cut.em.getTransaction().begin();
        cut.crear(aspirante);
        cut.em.getTransaction().commit();

        Aspirante encontrado = cut.buscarPorCorreo(correo);
        assertNotNull(encontrado);
        assertEquals(correo, encontrado.getCorreo());
        assertEquals(aspirante.getIdAspirante(), encontrado.getIdAspirante());
        assertEquals(aspirante.getNombres(), encontrado.getNombres());
        assertEquals(aspirante.getApellidos(), encontrado.getApellidos());
    }

    @Order(24)
    @Test
    public void testBuscarPorCorreoSinResultados() {
        System.out.println("buscarPorCorreo aspirante sin resultados");
        String correoInexistente = "correo-inexistente-" + UUID.randomUUID().toString().substring(0, 8) + "@gmail.com";

        Aspirante encontrado = cut.buscarPorCorreo(correoInexistente);
        assertNull(encontrado);
    }

    @Order(25)
    @Test
    public void testBuscarPorCorreoParametrosInvalidos() {
        System.out.println("buscarPorCorreo parametros invalidos");
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorCorreo(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorCorreo("   "));
    }

    @Order(26)
    @Test
    public void testBuscarPorCorreoEmNull() {
        System.out.println("buscarPorCorreo em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> cut.buscarPorCorreo("correo@test.com"));
    }

    @Order(27)
    @Test
    public void testBuscarPorApellidosParametrosInvalidos() {
        System.out.println("buscarPorApellidos aspirante parametros invalidos");
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorApellidos(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorApellidos("   "));
    }

    @Order(28)
    @Test
    public void testBuscarPorApellidosEmNull() {
        System.out.println("buscarPorApellidos aspirante em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> cut.buscarPorApellidos("Lopez"));
    }

}
