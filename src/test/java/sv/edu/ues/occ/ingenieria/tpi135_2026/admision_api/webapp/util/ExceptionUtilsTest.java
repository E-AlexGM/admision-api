package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExceptionUtilsTest {
  
    IllegalArgumentException illegalArgumentException;
    IllegalStateException illegalStateException;
    Throwable e;

    @BeforeEach
    public void setUp(){
        
        illegalArgumentException = new IllegalArgumentException();
        illegalStateException = new IllegalStateException(illegalArgumentException);
        e = new Throwable(illegalStateException);

    }

    /**
     * Prueba de Aceptacion
     */
    @Test
    void testGetRootCause() {
        Throwable expected = illegalArgumentException;
        Throwable result = ExceptionUtils.getRootCause(e);
        assertEquals(expected, result);
        assertNotEquals(illegalStateException, result);
    }

    @Test
    void testGetRootCauseIllegalArgument(){
        Throwable result = assertThrows(IllegalArgumentException.class, () -> ExceptionUtils.getRootCause(null));
        assertEquals("El argumento no puede ser nulo", result.getMessage());
    }
}
