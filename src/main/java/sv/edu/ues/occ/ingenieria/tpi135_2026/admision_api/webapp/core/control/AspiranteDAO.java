package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.io.Serializable;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Aspirante;

@Stateless
@LocalBean
public class AspiranteDAO extends DefaultDAO<Aspirante> implements Serializable {

    @PersistenceContext(unitName = "AdmisionPU")
    EntityManager em;

    public AspiranteDAO() {
        super(Aspirante.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Busca aspirantes por apellidos exactos.
     * @param apellidos apellidos del aspirante
     * @return lista de aspirantes encontrados
     * @throws IllegalArgumentException si el parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public List<Aspirante> buscarPorApellidos(String apellidos) {
        if (apellidos != null && !apellidos.isBlank()) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery("Aspirante.buscarPorApellidos", Aspirante.class)
                            .setParameter("apellidos", apellidos.trim())
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
     * Busca un aspirante por correo exacto.
     * @param correo correo del aspirante
     * @return aspirante encontrado o null si no se encuentra
     * @throws IllegalArgumentException si el parametro es invalido
     * @throws IllegalStateException si ocurre un error al consultar
     */
    public Aspirante buscarPorCorreo(String correo) {
        if (correo != null && !correo.isBlank()) {
            try {
                EntityManager entityManager = this.getEntityManager();
                if (entityManager != null) {
                    return entityManager.createNamedQuery("Aspirante.buscarPorCorreo", Aspirante.class)
                            .setParameter("correo", correo.trim())
                            .getSingleResult();
                }
                throw new NullPointerException("El repositorio es nulo");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalArgumentException("Parámetros inválidos");
    }
}