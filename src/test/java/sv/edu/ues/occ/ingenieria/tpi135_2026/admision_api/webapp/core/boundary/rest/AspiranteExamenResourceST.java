package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Jornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.JornadaAula;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaClave;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornada;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcion;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.TipoPrueba;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AspiranteExamenResourceST extends AbstractIntegrationTest{

    private final String RESOURCE_NAME_ASPIRANTE = "aspirante";
    private final String RESOURCE_NAME_TIPO_PRUEBA = "tipo_prueba";
    private final String RESORUCE_NAME_PRUEBA = "prueba";
    private final String  RESOURCE_NAME_JORNADA = "jornada";
    private final String RESOURCE_NAME_AULA = "aula";
    private final String RESOURCE_NAME_ASPIRANTE_OPCION = "aspirante_opcion";
    private final String RESORCE_NAME_OPCION = "opcion";
    private final String RESOURCE_NAME_EXAMEN = "examen";
    private final String RESOURCE_NAME_CLAVE = "clave";

    TipoPrueba tipoPrueba = new TipoPrueba();
    UUID idTipoPrueba;
    Prueba prueba = new Prueba();
    UUID idPrueba;
    Jornada jornada = new Jornada();
    UUID idJornada;
    JornadaAula jornadaAula = new JornadaAula();
    String idAula = UUID.randomUUID().toString();
    Aspirante aspirante =  new Aspirante();
    UUID idAspirante;
    AspiranteOpcion aspiranteOpcion = new AspiranteOpcion();
    UUID idAspiranteOpcion;
    String idOpcion = UUID.randomUUID().toString();
    PruebaJornada pruebaJornada =  new PruebaJornada();
    PruebaJornadaAulaAspiranteOpcion pruebaJornadaAulaAspiranteOpcion = new PruebaJornadaAulaAspiranteOpcion();
    PruebaJornadaAulaAspiranteOpcionExamen pruebaJornadaAulaAspiranteOpcionExamen;
    PruebaClave pruebaClave = new PruebaClave();
    UUID idPruebaClave;

    @BeforeEach
    public void setUp() {
        tipoPrueba.setValor("INGRESO_UNIVERSITARIO_PRIMERA_RONDA");
        tipoPrueba.setActivo(true);

        prueba.setIndicaciones("Indicaciones");
        prueba.setDuracion(120);
        prueba.setPuntajeMaximo(new BigDecimal(100));
        prueba.setFechaCreacion(OffsetDateTime.now());
        prueba.setNombre("NUEVO_INGRESO_2026");
        prueba.setNotaAprobacion(new BigDecimal(50));

        jornada.setFechaInicio(OffsetDateTime.now().plusDays(1));
        jornada.setFechaFin(OffsetDateTime.now().plusDays(2));
        jornada.setNombre("JORNADA_VESPERTINA_2026");

        jornadaAula.setIdAula(idAula);
        jornadaAula.setIdJornada(jornada);

        aspirante.setNombres("Jose");
        aspirante.setDocumentoIdentidad("111111-1");
        aspirante.setCorreo("jose@email.com");
        aspirante.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        aspirante.setApellidos("López");
        aspirante.setFechaCreacion(OffsetDateTime.now());

        aspiranteOpcion.setPrioridad(1);
        aspiranteOpcion.setFechaCreacion(OffsetDateTime.now());
        aspiranteOpcion.setIdAspirante(aspirante);
        aspiranteOpcion.setIdOpcion(idOpcion);

        pruebaClave.setNombreClave("PRIMERA_CLAVE");

    }

    public void crearContextoExamen(){
        Response respuestaTipoPrueba = target.path(RESOURCE_NAME_TIPO_PRUEBA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(tipoPrueba));
        idTipoPrueba = UUID.fromString(respuestaTipoPrueba.getLocation().toString().split(RESOURCE_NAME_TIPO_PRUEBA + "/")[1]);
        tipoPrueba.setIdTipoPrueba(idTipoPrueba);
        prueba.setIdTipoPrueba(tipoPrueba);
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaTipoPrueba.getStatus());

        Response respuestaPrueba = target.path(RESORUCE_NAME_PRUEBA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(prueba));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPrueba.getStatus());
        idPrueba = UUID.fromString(respuestaPrueba.getLocation().toString().split(RESORUCE_NAME_PRUEBA + "/")[1]);
        prueba.setIdPrueba(idPrueba);

        pruebaClave.setIdPrueba(prueba);
        Response respuestaPruebaClave = target.path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .path(RESOURCE_NAME_CLAVE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pruebaClave));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPruebaClave.getStatus());
        idPruebaClave = UUID.fromString(respuestaPruebaClave.getLocation().toString().split(RESOURCE_NAME_CLAVE + "/")[1]);
        pruebaClave.setIdPruebaClave(idPruebaClave);

        Response respuestaJornada = target.path(RESOURCE_NAME_JORNADA)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jornada));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaJornada.getStatus());
        idJornada = UUID.fromString(respuestaJornada.getLocation().toString().split(RESOURCE_NAME_JORNADA + "/")[1]);
        jornada.setIdJornada(idJornada);

        jornadaAula.setIdJornada(jornada);
        Response respuestaJornadaAula = target.path(RESOURCE_NAME_JORNADA)
                .path(idJornada.toString())
                .path(RESOURCE_NAME_AULA)
                .path(idAula)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jornadaAula));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaJornadaAula.getStatus());

        Response respuestaAspirante = target.path(RESOURCE_NAME_ASPIRANTE)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(aspirante));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaAspirante.getStatus());
        idAspirante = UUID.fromString(respuestaAspirante.getLocation().toString().split(RESOURCE_NAME_ASPIRANTE + "/")[1]);


        Response respuestaAspiranteOpcion = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(idAspirante.toString())
                .path(RESORCE_NAME_OPCION)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(aspiranteOpcion));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaAspiranteOpcion.getStatus());
        idAspiranteOpcion = UUID.fromString(respuestaAspiranteOpcion.getLocation().toString().split(RESORCE_NAME_OPCION + "/")[1]);
        aspiranteOpcion.setIdAspiranteOpcion(idAspiranteOpcion);

        pruebaJornada.setIdJornada(jornada);
        pruebaJornada.setIdPrueba(prueba);
        Response respuestaPruebaJornada = target.path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .path(RESOURCE_NAME_JORNADA)
                .path(idJornada.toString())
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pruebaJornada));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPruebaJornada.getStatus());

        pruebaJornadaAulaAspiranteOpcion.setIdAula(idAula);
        pruebaJornadaAulaAspiranteOpcion.setIdPrueba(prueba);
        pruebaJornadaAulaAspiranteOpcion.setActivo(true);
        pruebaJornadaAulaAspiranteOpcion.setIdJornada(jornada);
        pruebaJornadaAulaAspiranteOpcion.setFecha(OffsetDateTime.now());
        pruebaJornadaAulaAspiranteOpcion.setIdAspiranteOpcion(aspiranteOpcion);

        Response respuestaPruebaJornadaAulaAspiranteOpcion = target.path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .path(RESOURCE_NAME_JORNADA)
                .path(idJornada.toString())
                .path(RESOURCE_NAME_AULA)
                .path(idAula)
                .path(RESOURCE_NAME_ASPIRANTE_OPCION)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pruebaJornadaAulaAspiranteOpcion));
        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPruebaJornadaAulaAspiranteOpcion.getStatus());


        pruebaJornadaAulaAspiranteOpcionExamen = new PruebaJornadaAulaAspiranteOpcionExamen(prueba, jornada, idAula, aspiranteOpcion );
        pruebaJornadaAulaAspiranteOpcionExamen.setIdPruebaClave(pruebaClave);
        pruebaJornadaAulaAspiranteOpcionExamen.setFechaResultado(OffsetDateTime.now());
        pruebaJornadaAulaAspiranteOpcionExamen.setResultado(new BigDecimal(70));

        Response respuestaPruebaJornadaAulaAspiranteOpcionExamen = target.path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .path(RESOURCE_NAME_JORNADA)
                .path(idJornada.toString())
                .path(RESOURCE_NAME_AULA)
                .path(idAula)
                .path(RESOURCE_NAME_ASPIRANTE_OPCION)
                .path(idAspiranteOpcion.toString())
                .path(RESOURCE_NAME_EXAMEN)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pruebaJornadaAulaAspiranteOpcionExamen));

        assertEquals(Response.Status.CREATED.getStatusCode(), respuestaPruebaJornadaAulaAspiranteOpcionExamen.getStatus());

    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Order(1)
    @Test
    public void buscarResultadoExamen(){
        crearContextoExamen();
        //("{id_aspirante}/prueba/{id_prueba}")
        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(idAspirante.toString())
                .path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(ExamenResultadosEnum.SELECCIONADO, respuesta.readEntity(ExamenResultadosEnum.class));
    }

    @Order(2)
    @Test
    public void listarPruebasPorAspirante(){
        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(idAspirante.toString())
                .path(RESORUCE_NAME_PRUEBA)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
    }

    @Order(3)
    @Test
    public void actualizarResultadoExamen() {
        PruebaJornadaAulaAspiranteOpcionExamen examenActualizado = new PruebaJornadaAulaAspiranteOpcionExamen();
        examenActualizado.setResultado(new BigDecimal(40));
        Response respuesta = target.path(RESOURCE_NAME_ASPIRANTE)
                .path(idAspirante.toString())
                .path(RESORUCE_NAME_PRUEBA)
                .path(idPrueba.toString())
                .path(RESOURCE_NAME_EXAMEN)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(examenActualizado));
        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        assertEquals(new BigDecimal(40), respuesta.readEntity(PruebaJornadaAulaAspiranteOpcionExamen.class).getResultado());
    }
}
