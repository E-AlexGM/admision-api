package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity.Prueba;

@Stateless
@LocalBean
public class PruebaDAO extends DefaultDAO<Prueba> implements Serializable {

    @PersistenceContext(unitName = "AdmisionPU")
    EntityManager em;

    public PruebaDAO() {
        super(Prueba.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
     
    public List<Prueba> findByIdAspirante(UUID idAspirante) {
        if(idAspirante == null){
            throw new IllegalArgumentException("El id del aspirante no puede ser nulo");
        }
        try {
            return em.createNamedQuery("Prueba.findByIdAspirante", Prueba.class)
                    .setParameter("idAspirante", idAspirante)
                    .getResultList();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }
    
    
    public List<Prueba> findActive(Boolean active){

        if(active == null) throw new IllegalArgumentException("El estado no puede ser nulo");
        
        try{
            return em.createNamedQuery("Prueba.findActive", Prueba.class)
                    .setParameter("activo", active)
                    .getResultList();
        }catch(Exception e){
            throw new IllegalStateException(e);
        }
    }
    

}