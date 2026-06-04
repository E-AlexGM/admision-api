package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.PruebaJornadaAulaAspiranteOpcionExamen;

@Stateless
@LocalBean
public class PruebaJornadaAulaAspiranteOpcionExamenDAO
        extends DefaultDAO<PruebaJornadaAulaAspiranteOpcionExamen> implements Serializable {

    @PersistenceContext(unitName = "AdmisionPU")
    EntityManager em;

    public PruebaJornadaAulaAspiranteOpcionExamenDAO() {
        super(PruebaJornadaAulaAspiranteOpcionExamen.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Busca registros por llaves padre en un rango paginado.
     * @param idPrueba identificador de prueba
     * @param idJornada identificador de jornada
     * @param idAula identificador de aula
     * @param idAspiranteOpcion identificador de aspirante opcion
     * @param first indice inicial del rango
     * @param max cantidad maxima de registros
     * @return lista de registros encontrados
     * @throws IllegalArgumentException si algun parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public List<PruebaJornadaAulaAspiranteOpcionExamen> buscarPorPadreRango(
            UUID idPrueba, UUID idJornada, String idAula, UUID idAspiranteOpcion, int first, int max) {
        if (idPrueba != null && idJornada != null && idAula != null && idAspiranteOpcion != null && first >= 0 && max > 0) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery(
                            "PruebaJornadaAulaAspiranteOpcionExamen.buscarPorPadre",
                            PruebaJornadaAulaAspiranteOpcionExamen.class)
                        .setParameter("idPrueba", idPrueba)
                        .setParameter("idJornada", idJornada)
                        .setParameter("idAula", idAula)
                        .setParameter("idAspiranteOpcion", idAspiranteOpcion)
                            .setFirstResult(first)
                            .setMaxResults(max)
                            .getResultList();
                }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }

    /**
     * Cuenta registros por llaves padre.
     * @param idPrueba identificador de prueba
     * @param idJornada identificador de jornada
     * @param idAula identificador de aula
     * @param idAspiranteOpcion identificador de aspirante opcion
     * @return total de registros encontrados
     * @throws IllegalArgumentException si algun parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public Long contarPorPadre(UUID idPrueba, UUID idJornada, String idAula, UUID idAspiranteOpcion) {
        if (idPrueba != null && idJornada != null && idAula != null && idAspiranteOpcion != null) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery(
                            "PruebaJornadaAulaAspiranteOpcionExamen.contarPorPadre", Long.class)
                        .setParameter("idPrueba", idPrueba)
                        .setParameter("idJornada", idJornada)
                        .setParameter("idAula", idAula)
                        .setParameter("idAspiranteOpcion", idAspiranteOpcion)
                            .getSingleResult();
                }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }


    public PruebaJornadaAulaAspiranteOpcionExamen obtenerResultadoExamenPorAspiranteYPrueba(UUID idAspirante, UUID idPrueba) {
        if(idAspirante != null && idPrueba != null){
            if(em != null){
                return em.createNamedQuery("PruebaJornadaAulaAspiranteOpcionExamen.findByIdAspiranteAndIdPrueba", PruebaJornadaAulaAspiranteOpcionExamen.class)
                        .setParameter("idAspirante", idAspirante)
                        .setParameter("idPrueba", idPrueba)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
            }
           throw new IllegalStateException("El repositorio es nulo");
        }
        throw new IllegalArgumentException("idAspirante y idPrueba no pueden ser nulos");
    }

    public List<PruebaJornadaAulaAspiranteOpcionExamen> obtenerResultadoExamenPorCorreoAspirante(String correo) {
        if(correo != null && !correo.isBlank()){
            if(em != null){
                return em.createNamedQuery("PruebaJornadaAulaAspiranteOpcionExamen.findByCorreoAspirante", PruebaJornadaAulaAspiranteOpcionExamen.class)
                        .setParameter("correo", correo)
                        .getResultList();
            }
           throw new IllegalStateException("El repositorio es nulo");
        }
        throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
    }
}

