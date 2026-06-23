/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

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

// Examen TPI

/**
 *
 * @author everg
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AreaDAOIT extends AbstractIntengrationDAOTest{

    AreaDAO cut;

    @BeforeEach // una antes de cada prueba
    public void antesdecadaprueba(){
        cut = new AreaDAO();
        cut.em = em;
    }

    @Order(1)
    @Test
    public void testCountExitoso() {
        System.out.println("count - exitoso");
        Long resultado = cut.contar();
        assertEquals(resultado, 0);
        System.out.println("Resultado: " + resultado);
    }

    @Order(2)
    @Test
    public void testCountEmNulo() {
        System.out.println("count - em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> cut.contar());
    }

    @Order(3)
    @Test
    public void testCountEmCerrado() {
        System.out.println("count - em cerrado");
        EntityManager emCerrado = emf.createEntityManager();
        emCerrado.close();
        cut.em = emCerrado;
        assertThrows(IllegalStateException.class, () -> cut.contar());
    }

    @Order(4)
    @Test
    public void testCrearExitoso() {
        System.out.println("crear - exitoso");
        Area nuevo = new Area(UUID.randomUUID());
        nuevo.setNombre("registro prueba 1");

        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        Long resultado = cut.contar();
        assertEquals(1, resultado); // Esperamos 1 porque el test anterior verificó que había 0
        System.out.println("Resultado: " + resultado);
    }

    @Order(5)
    @Test
    public void testCrearEntidadNula() {
        System.out.println("crear - entidad nula");
        // Dejamos que falle por la validación de la entidad nula
        assertThrows(IllegalArgumentException.class, () -> { cut.crear(null); });
    }

    @Order(6)
    @Test
    public void testCrearEmNulo() {
        System.out.println("crear - em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> { cut.crear(new Area(UUID.randomUUID())); });
    }

    @Order(7)
    @Test
    public void testBuscarPorIdExitoso() {
        System.out.println("buscarPorId - exitoso");
        UUID idBuscado = UUID.randomUUID();
        Area nuevo = new Area(idBuscado);
        nuevo.setNombre("Area a buscar");

        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        Area encontrado = cut.buscarPorId(idBuscado);
        assertNotNull(encontrado);
        assertEquals(idBuscado, encontrado.getIdArea());
        assertEquals(nuevo.getNombre(), encontrado.getNombre());
        System.out.println("Id: " + idBuscado);
    }

    @Order(8)
    @Test
    public void testBuscarPorIdNulo() {
        System.out.println("buscarPorId - id nulo");
        assertThrows(IllegalArgumentException.class, () -> { cut.buscarPorId(null); });
    }

    @Order(9)
    @Test
    public void testBuscarPorIdEmNulo() {
        System.out.println("buscarPorId - em nulo");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> { cut.buscarPorId(UUID.randomUUID()); });
    }


    @Order(10)
    @Test
    public void testEliminarNoExistente() {
        System.out.println("eliminar - entidad no guardada");
        UUID idEliminado = UUID.randomUUID();
        Area eliminado = new Area(idEliminado);
        eliminado.setNombre("Area a eliminar");

        cut.em.getTransaction().begin();
        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(11)
    @Test
    public void testEliminarExitoso() {
        System.out.println("eliminar - exitoso");
        UUID idEliminado = UUID.randomUUID();
        Area eliminado = new Area(idEliminado);
        eliminado.setNombre("Area a eliminar");

        // Guardamos
        cut.em.getTransaction().begin();
        cut.crear(eliminado);
        cut.em.getTransaction().commit();

        // Eliminamos
        cut.em.getTransaction().begin();
        cut.eliminar(eliminado);
        cut.em.getTransaction().commit();

        assertNull(cut.buscarPorId(idEliminado));
    }

    @Order(12)
    @Test
    public void testEliminarEntidadNula() {
        System.out.println("eliminar - entidad nula");
        assertThrows(IllegalArgumentException.class, () -> { cut.eliminar(null); });
    }

    @Order(13)
    @Test
    public void testEliminarEmNulo() {
        System.out.println("eliminar - em nulo");
        cut.em = null;
        Area eliminado = new Area(UUID.randomUUID());
        assertThrows(IllegalStateException.class, () -> { cut.eliminar(eliminado); });
    }

    @Order(14)
    @Test
    public void testActualizarExitoso(){
        System.out.println("actualizar - exitoso");
        Area nuevo = new Area(UUID.randomUUID());
        nuevo.setNombre("registro antes de actualizar");

        cut.em.getTransaction().begin();
        cut.crear(nuevo);
        cut.em.getTransaction().commit();

        // Modificar la entidad
        nuevo.setNombre("Actualizado");
        cut.em.getTransaction().begin();
        Area actualizado = cut.actualizar(nuevo);
        cut.em.getTransaction().commit();

        assertNotNull(actualizado);
        assertEquals(nuevo.getNombre(), actualizado.getNombre());
        System.out.println("Nombre actualizado: " + actualizado.getNombre());
        // fail("Esta prueba falló");
    }

    @Order(15)
    @Test
    public void testActualizarEntidadNula() {
        System.out.println("actualizar - entidad nula");
        assertThrows(IllegalArgumentException.class, () -> { cut.actualizar(null); });
        // fail("Esta prueba falló");
    }

    @Order(16)
    @Test
    public void testActualizarEmNulo() {
        System.out.println("actualizar - em nulo");
        // Hacemos la entity manager null
        cut.em = null;
        Area actualizado = new Area(UUID.randomUUID());

        assertThrows(IllegalStateException.class, () -> { cut.actualizar(actualizado); });
        // fail("Esta prueba falló");
    }

    @Order(17)
    @Test
    public void testBuscarPorRangoExtitoso(){
        System.out.println("buscarPorRango - extitoso");
        Long esperado = cut.contar();

        List<Area> areas = cut.buscarPorRango(0, 50);
        assertNotNull(areas);
        assertEquals(esperado, areas.size());
        // fail("Esta prueba falló");
    }

    @Order(18)
    @Test
    public void testBuscarPorRangoPametrosNoValidos() {
        System.out.println("buscarPorRango - parametros no validos");
        assertThrows(IllegalArgumentException.class, () -> {cut.buscarPorRango(0, -1);});
        assertThrows(IllegalArgumentException.class, () -> {cut.buscarPorRango(-1, 50);});
        //fail("Esta prueba falló");
    }

    @Order(19)
    @Test
    public void testBuscarPorRangoEmNull() {
        System.out.println("buscarPorRango - parametros no validos");
        cut.em = null;
        assertThrows(IllegalStateException.class, () -> {cut.buscarPorRango(0, 50);});
        //fail("Esta prueba falló");
    }
}
