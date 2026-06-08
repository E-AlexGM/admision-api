package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

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
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;

public class AspiranteOpcionDAOTest {

    @Test
    public void constructorTest() {
        System.out.println("AspiranteOpcionDAOTest.constructorTest");
        AspiranteOpcionDAO cut = new AspiranteOpcionDAO();
        assertNotNull(cut);
        System.out.println("AspiranteOpcionDAOTest.constructorTest - finalizado");
    }

    @Test
    public void getEntityManagerTest() {
        System.out.println("AspiranteOpcionDAOTest.getEntityManagerTest");
        AspiranteOpcionDAO cut = new AspiranteOpcionDAO();
        assertNull(cut.getEntityManager());
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        cut.em = mockEM;
        assertEquals(mockEM, cut.getEntityManager());
        System.out.println("AspiranteOpcionDAOTest.getEntityManagerTest - finalizado");
    }

    @Test
    public void buscarPorAspiranteRangoTest() {
        System.out.println("AspiranteOpcionDAOTest.buscarPorAspiranteRangoTest");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcion opcion = new AspiranteOpcion();
        List<AspiranteOpcion> listaEsperada = Collections.singletonList(opcion);
        AspiranteOpcionDAO cut = new AspiranteOpcionDAO();

        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorAspiranteRango(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorAspiranteRango(idAspirante, -1, 10));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorAspiranteRango(idAspirante, 0, 0));

        assertThrows(IllegalStateException.class, () -> cut.buscarPorAspiranteRango(idAspirante, 0, 10));

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<AspiranteOpcion> mockQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.buscarPorAspiranteRango", AspiranteOpcion.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter(Mockito.anyString(), Mockito.any(UUID.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.setFirstResult(Mockito.anyInt())).thenReturn(mockQuery);
        Mockito.when(mockQuery.setMaxResults(Mockito.anyInt())).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(listaEsperada);

        cut.em = mockEM;
        List<AspiranteOpcion> resultadoExitoso = cut.buscarPorAspiranteRango(idAspirante, 0, 10);
        assertEquals(listaEsperada, resultadoExitoso);

        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.buscarPorAspiranteRango", AspiranteOpcion.class))
            .thenThrow(new RuntimeException("Error"));
        assertThrows(IllegalStateException.class, () -> cut.buscarPorAspiranteRango(idAspirante, 0, 10));
        System.out.println("AspiranteOpcionDAOTest.buscarPorAspiranteRangoTest - finalizado");
    }

    @Test
    public void contarPorAspiranteTest() {
        System.out.println("AspiranteOpcionDAOTest.contarPorAspiranteTest");
        UUID idAspirante = UUID.randomUUID();
        AspiranteOpcionDAO cut = new AspiranteOpcionDAO();

        assertThrows(IllegalArgumentException.class, () -> cut.contarPorAspirante(null));

        assertThrows(IllegalStateException.class, () -> cut.contarPorAspirante(idAspirante));

        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<Long> mockQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.contarPorAspirante", Long.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter(Mockito.anyString(), Mockito.any(UUID.class))).thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(5L);

        cut.em = mockEM;
        Long resultadoExitoso = cut.contarPorAspirante(idAspirante);
        assertEquals(5L, resultadoExitoso);

        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.contarPorAspirante", Long.class))
            .thenThrow(new RuntimeException("Error"));
        assertThrows(IllegalStateException.class, () -> cut.contarPorAspirante(idAspirante));
        System.out.println("AspiranteOpcionDAOTest.contarPorAspiranteTest - finalizado");
    }

    @Test
    public void buscarPorIdYAspiranteTest() {
        System.out.println("AspiranteOpcionDAOTest.buscarPorIdYAspiranteTest");
        UUID idAspirante = UUID.randomUUID();
        UUID idOpcion = UUID.randomUUID();
        AspiranteOpcionDAO cut = new AspiranteOpcionDAO();
        
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorIdYAspirante(null, UUID.randomUUID()));
        assertThrows(IllegalArgumentException.class, () -> cut.buscarPorIdYAspirante(UUID.randomUUID(), null));

        assertThrows(IllegalStateException.class, () -> cut.buscarPorIdYAspirante(idOpcion, idAspirante));

        AspiranteOpcion opcionEsperada = new AspiranteOpcion();
        EntityManager mockEM = Mockito.mock(EntityManager.class);
        TypedQuery<AspiranteOpcion> mockQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.buscarPorIdYAspirante", AspiranteOpcion.class)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter("idAspiranteOpcion", idOpcion)).thenReturn(mockQuery);
        Mockito.when(mockQuery.setParameter("idAspirante", idAspirante)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getSingleResult()).thenReturn(opcionEsperada);

        cut.em = mockEM;
        AspiranteOpcion resultadoExitoso = cut.buscarPorIdYAspirante(idOpcion, idAspirante);

        Mockito.when(mockEM.createNamedQuery("AspiranteOpcion.buscarPorIdYAspirante", AspiranteOpcion.class))
            .thenThrow(new RuntimeException("Error"));
        assertThrows(IllegalStateException.class, () -> cut.buscarPorIdYAspirante(idOpcion, idAspirante));
        System.out.println("AspiranteOpcionDAOTest.buscarPorIdYAspiranteTest - finalizado");
    }
}