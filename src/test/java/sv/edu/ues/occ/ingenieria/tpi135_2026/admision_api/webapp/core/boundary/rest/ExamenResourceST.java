package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.bdd.AbstractBDD;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExamenResourceST extends AbstractBDD{

    private final String RESOURCE_NAME_EXAMEN = "examen";

    @BeforeEach
    public void setup(){
        initClient();
    }

    

    @Order(1)
    @Test
    public void testListarResultadosExamenPorCorreo() {
        Response respuesta = target.path(RESOURCE_NAME_EXAMEN)
            .queryParam("correo", "maria.lopez@email.com")
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }   


}
