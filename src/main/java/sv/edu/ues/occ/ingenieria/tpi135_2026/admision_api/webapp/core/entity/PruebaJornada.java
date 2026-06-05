package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@IdClass(PruebaJornadaPK.class)
@Table(name = "prueba_jornada")
@NamedQueries({
    @NamedQuery(
        name = "PruebaJornada.findByJornadaFechaInicio",
        query = "SELECT pj FROM PruebaJornada pj WHERE pj.idJornada.fechaInicio <= :fechaInicio"
    )
})
public class PruebaJornada implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prueba", nullable = false)
    private Prueba idPrueba;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jornada", nullable = false)
    private Jornada idJornada;

    public PruebaJornada() {}

    public PruebaJornada(Prueba idPrueba, Jornada idJornada) {
        this.idPrueba = idPrueba;
        this.idJornada = idJornada;
    }

    
    public Prueba getIdPrueba() {
        return idPrueba;
    }

    public void setIdPrueba(Prueba idPrueba) {
        this.idPrueba = idPrueba;
    }

    
    public Jornada getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(Jornada idJornada) {
        this.idJornada = idJornada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idPrueba != null ? idPrueba.getIdPrueba() : null,
                idJornada != null ? idJornada.getIdJornada() : null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PruebaJornada other = (PruebaJornada) obj;
        return Objects.equals(idPrueba != null ? idPrueba.getIdPrueba() : null,
                other.idPrueba != null ? other.idPrueba.getIdPrueba() : null)
                && Objects.equals(idJornada != null ? idJornada.getIdJornada() : null,
                        other.idJornada != null ? other.idJornada.getIdJornada() : null);
    }

    @Override
    public String toString() {
        return "PruebaJornada [idPrueba=" + (idPrueba != null ? idPrueba.getIdPrueba() : null)
                + ", idJornada=" + (idJornada != null ? idJornada.getIdJornada() : null) + "]";
    }
}
