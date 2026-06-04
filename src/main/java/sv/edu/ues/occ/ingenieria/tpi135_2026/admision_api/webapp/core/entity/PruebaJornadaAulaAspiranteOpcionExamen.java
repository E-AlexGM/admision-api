package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@NamedQueries({
    @NamedQuery(
        name = "PruebaJornadaAulaAspiranteOpcionExamen.buscarPorPadre",
        query = "SELECT p FROM PruebaJornadaAulaAspiranteOpcionExamen p WHERE p.idPrueba.idPrueba = :idPrueba AND p.idJornada.idJornada = :idJornada AND p.idAula = :idAula AND p.idAspiranteOpcion.idAspiranteOpcion = :idAspiranteOpcion ORDER BY p.fechaResultado"
    ),
    @NamedQuery(
        name = "PruebaJornadaAulaAspiranteOpcionExamen.contarPorPadre",
        query = "SELECT COUNT(p) FROM PruebaJornadaAulaAspiranteOpcionExamen p WHERE p.idPrueba.idPrueba = :idPrueba AND p.idJornada.idJornada = :idJornada AND p.idAula = :idAula AND p.idAspiranteOpcion.idAspiranteOpcion = :idAspiranteOpcion"
    ),
    @NamedQuery(
                name = "PruebaJornadaAulaAspiranteOpcionExamen.findByIdAspiranteAndIdPrueba",
                query = "SELECT p FROM PruebaJornadaAulaAspiranteOpcionExamen p WHERE p.idAspiranteOpcion.idAspirante.idAspirante = :idAspirante AND p.idPrueba.idPrueba = :idPrueba"
    ),
     @NamedQuery(
                name = "PruebaJornadaAulaAspiranteOpcionExamen.findByCorreoAspirante",
                query = "SELECT p FROM PruebaJornadaAulaAspiranteOpcionExamen p WHERE p.idAspiranteOpcion.idAspirante.correo = :correo"
    )
})
@Entity
@IdClass(PruebaJornadaAulaAspiranteOpcionExamenPK.class)
@Table(name = "prueba_jornada_aula_aspirante_opcion_examen")
public class PruebaJornadaAulaAspiranteOpcionExamen implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prueba", nullable = false)
    private Prueba idPrueba;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jornada", nullable = false)
    private Jornada idJornada;

    @Id
    @Column(name = "id_aula", nullable = false, length = 100)
    private String idAula;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aspirante_opcion", nullable = false)
    private AspiranteOpcion idAspiranteOpcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prueba_clave", nullable = false)
    private PruebaClave idPruebaClave;

    @Column(name = "resultado", precision = 5, scale = 2, nullable = false)
    private BigDecimal resultado;

    @Column(name = "fecha_resultado")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime fechaResultado;

    public PruebaJornadaAulaAspiranteOpcionExamen() {}

    public PruebaJornadaAulaAspiranteOpcionExamen(Prueba idPrueba, Jornada idJornada, String idAula, AspiranteOpcion idAspiranteOpcion) {
        this.idPrueba = idPrueba;
        this.idJornada = idJornada;
        this.idAula = idAula;
        this.idAspiranteOpcion = idAspiranteOpcion;
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

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public AspiranteOpcion getIdAspiranteOpcion() {
        return idAspiranteOpcion;
    }

    public void setIdAspiranteOpcion(AspiranteOpcion idAspiranteOpcion) {
        this.idAspiranteOpcion = idAspiranteOpcion;
    }

    public PruebaClave getIdPruebaClave() {
        return idPruebaClave;
    }

    public void setIdPruebaClave(PruebaClave idPruebaClave) {
        this.idPruebaClave = idPruebaClave;
    }

    public BigDecimal getResultado() {
        return resultado;
    }

    public void setResultado(BigDecimal resultado) {
        this.resultado = resultado;
    }

    public OffsetDateTime getFechaResultado() {
        return fechaResultado;
    }

    public void setFechaResultado(OffsetDateTime fechaResultado) {
        this.fechaResultado = fechaResultado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idPrueba != null ? idPrueba.getIdPrueba() : null,
                idJornada != null ? idJornada.getIdJornada() : null,
                idAula,
                idAspiranteOpcion != null ? idAspiranteOpcion.getIdAspiranteOpcion() : null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PruebaJornadaAulaAspiranteOpcionExamen other = (PruebaJornadaAulaAspiranteOpcionExamen) obj;
        return Objects.equals(idPrueba != null ? idPrueba.getIdPrueba() : null,
                other.idPrueba != null ? other.idPrueba.getIdPrueba() : null)
                && Objects.equals(idJornada != null ? idJornada.getIdJornada() : null,
                        other.idJornada != null ? other.idJornada.getIdJornada() : null)
                && Objects.equals(idAula, other.idAula)
                && Objects.equals(idAspiranteOpcion != null ? idAspiranteOpcion.getIdAspiranteOpcion() : null,
                        other.idAspiranteOpcion != null ? other.idAspiranteOpcion.getIdAspiranteOpcion() : null);
    }

    @Override
    public String toString() {
        return "PruebaJornadaAulaAspiranteOpcionExamen [idPrueba="
            + (idPrueba != null ? idPrueba.getIdPrueba() : null)
            + ", idJornada=" + (idJornada != null ? idJornada.getIdJornada() : null)
            + ", idAula=" + idAula + ", idAspiranteOpcion="
            + (idAspiranteOpcion != null ? idAspiranteOpcion.getIdAspiranteOpcion() : null) + ", resultado=" + resultado
                + ", fechaResultado=" + fechaResultado + "]";
    }
}
