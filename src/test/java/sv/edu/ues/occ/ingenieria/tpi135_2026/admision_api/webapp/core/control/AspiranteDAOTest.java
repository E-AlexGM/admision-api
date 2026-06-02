package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;

public class AspiranteDAOTest {

    @Test
    public void constructorTest() {
        System.out.println("AspiranteDAOTest.constructorTest");
        AspiranteDAO cut = new AspiranteDAO();
        assertNotNull(cut);
        System.out.println("AspiranteDAOTest.constructorTest - finalizado");
    }

    @Test
    public void getEntityManagerTest() {
        System.out.println("AspiranteDAOTest.getEntityManagerTest");
        AspiranteDAO cut = new AspiranteDAO();
        assertNull(cut.getEntityManager());
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;
        assertEquals(mockEM, cut.getEntityManager());
        System.out.println("AspiranteDAOTest.getEntityManagerTest - finalizado");
    }

    @Test
    public void buscarPorApellidosTest() {
        System.out.println("AspiranteDAOTest.buscarPorApellidosTest");
        AspiranteDAO cut = new AspiranteDAO();

        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorApellidos(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorApellidos("   "));
        assertThrows(IllegalStateException.class, () -> cut.buscarPorApellidos("Lopez"));

        Aspirante aspirante = new Aspirante();
        List<Aspirante> listaEsperada = Collections.singletonList(aspirante);

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Aspirante> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery("Aspirante.buscarPorApellidos", Aspirante.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter("apellidos", "Lopez")).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(listaEsperada);

        cut.em = mockEM;
        List<Aspirante> resultadoExitoso = cut.buscarPorApellidos("Lopez");
        assertEquals(listaEsperada, resultadoExitoso);

        Mockito.when(mockEM.createNamedQuery("Aspirante.buscarPorApellidos", Aspirante.class))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(IllegalStateException.class, () -> cut.buscarPorApellidos("Lopez"));
        System.out.println("AspiranteDAOTest.buscarPorApellidosTest - finalizado");
    }

    @Test
    public void buscarPorCorreoTest() {
        System.out.println("AspiranteDAOTest.buscarPorCorreoTest");
        AspiranteDAO cut = new AspiranteDAO();

        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorCorreo(null));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorCorreo("   "));
        assertThrows(IllegalStateException.class, () -> cut.buscarPorCorreo("correo@test.com"));

        UUID idAspirante = UUID.randomUUID();
        Aspirante aspirante = new Aspirante(idAspirante);
        aspirante.setNombres("Nombres");
        aspirante.setApellidos("Apellidos");
        aspirante.setCorreo("correo@test.com");
        aspirante.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        aspirante.setFechaCreacion(OffsetDateTime.now());

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Aspirante> mockQuery = Mockito.mock(TypedQuery.class);

        Mockito.when(mockEM.createNamedQuery("Aspirante.buscarPorCorreo", Aspirante.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter("correo", "correo@test.com")).thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(aspirante);

        cut.em = mockEM;
        Aspirante resultadoExitoso = cut.buscarPorCorreo("  correo@test.com  ");
        assertEquals(aspirante, resultadoExitoso);

        Mockito.when(mockEM.createNamedQuery("Aspirante.buscarPorCorreo", Aspirante.class))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(IllegalStateException.class, () -> cut.buscarPorCorreo("correo@test.com"));
        System.out.println("AspiranteDAOTest.buscarPorCorreoTest - finalizado");
    }
}