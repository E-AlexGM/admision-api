package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.AspiranteOpcion;

@Stateless
@LocalBean
public class AspiranteOpcionDAO extends DefaultDAO<AspiranteOpcion> implements Serializable {

    @PersistenceContext(unitName = "AdmisionPU")
    EntityManager em;

    public AspiranteOpcionDAO() {
        super(AspiranteOpcion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Busca registros de AspiranteOpcion por aspirante en un rango paginado.
     * @param idAspirante identificador del aspirante
     * @param first indice inicial del rango
     * @param max cantidad maxima de registros
     * @return lista de registros encontrados
     * @throws IllegalArgumentException si algun parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public List<AspiranteOpcion> buscarPorAspiranteRango(UUID idAspirante, int first, int max) {
        if (idAspirante != null && first >= 0 && max > 0) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery("AspiranteOpcion.buscarPorAspiranteRango", AspiranteOpcion.class)
                        .setParameter("idAspirante", idAspirante)
                        .setFirstResult(first)
                        .setMaxResults(max)
                        .getResultList();
                }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }

    /**
     * Cuenta registros de AspiranteOpcion por aspirante.
     * @param idAspirante identificador del aspirante
     * @return total de registros encontrados
     * @throws IllegalArgumentException si el parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public Long contarPorAspirante(UUID idAspirante) {
        if (idAspirante != null) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery("AspiranteOpcion.contarPorAspirante", Long.class)
                        .setParameter("idAspirante", idAspirante)
                        .getSingleResult();
                }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }

    /**
     * Busca un registro de AspiranteOpcion por id de opcion y aspirante.
     * @param idAspiranteOpcion identificador de aspirante opcion
     * @param idAspirante identificador del aspirante
     * @return registro encontrado
     * @throws IllegalArgumentException si algun parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public AspiranteOpcion buscarPorIdYAspirante(UUID idAspiranteOpcion, UUID idAspirante) {
        if (idAspiranteOpcion != null && idAspirante != null) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    AspiranteOpcion resultados = entityManager.createNamedQuery("AspiranteOpcion.buscarPorIdYAspirante", AspiranteOpcion.class)
                        .setParameter("idAspiranteOpcion", idAspiranteOpcion)
                        .setParameter("idAspirante", idAspirante)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                    return resultados;
                 }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }
}