package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control;

import jakarta.enterprise.context.ApplicationScoped;
import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.dto.AulaDto;

@ApplicationScoped
public class AulaClientMock {


    /**
     * Mockea el acceso a un dto de aula, mockea el microservicio
     * @param idAula el id relacionado a la entidad que buscamos
     * @return dto de aula con datos quemados
     */
    public AulaDto findById(String idAula){
    
        if(idAula == null) throw new IllegalArgumentException("El iiAula no puede ser nulo");
        
        switch (idAula) {
            case "A01": return new AulaDto("A01","María Cervántes", "Final 25 Avenida Norte Mártires 30 de Julio, Ciudad Universitaria, San Salvador, El Salvador.");
            case "A02": return new AulaDto("A02", "Laboratorio Cómputo", "Colonia Universitaria, Avenida Fray Felipe de Jesús Moraga Sur, justo al sur del Estadio Óscar Quiteño, en el departamento y municipio de Santa Ana, El Salvador");
            case "A03": return new AulaDto("A03","Búnker","Colonia Universitaria, Avenida Fray Felipe de Jesús Moraga Sur, justo al sur del Estadio Óscar Quiteño, en el departamento y municipio de Santa Ana, El Salvador" );
            default: return null;            
        }
    }
    
}


