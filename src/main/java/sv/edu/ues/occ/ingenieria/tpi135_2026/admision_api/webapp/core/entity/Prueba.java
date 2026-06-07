package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "prueba")
@NamedQueries({
        @NamedQuery(name = "Prueba.findByIdAspirante",
                query = "SELECT ex.idPrueba FROM PruebaJornadaAulaAspiranteOpcionExamen ex WHERE ex.idAspiranteOpcion.idAspirante.idAspirante = :idAspirante"
        ),
        @NamedQuery(name = "Prueba.findActive",
            query = "SELECT p FROM Prueba p WHERE p.activo = :activo"
        )
})
public class Prueba implements Serializable{

    @Id
    @Column(name = "id_prueba")
    private UUID idPrueba;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "indicaciones")
    private String indicaciones;

    @Column(name = "puntaje_maximo", precision = 5, scale = 2, nullable = false)
    private BigDecimal puntajeMaximo;

    @Column(name = "nota_aprobacion", precision = 5, scale = 2, nullable = false)
    private BigDecimal notaAprobacion;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "fecha_creacion", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_prueba", nullable = false)
    private TipoPrueba idTipoPrueba;

    @Column(name = "activo", nullable = false)
    private Boolean activo;


    public Prueba(UUID idPrueba) {
        this.idPrueba = idPrueba;
    }

    public Prueba(){}

    public UUID getIdPrueba() {
        return idPrueba;
    }

    public void setIdPrueba(UUID idPrueba) {
        this.idPrueba = idPrueba;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public BigDecimal getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(BigDecimal puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public BigDecimal getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(BigDecimal notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    
    public TipoPrueba getIdTipoPrueba() {
        return idTipoPrueba;
    }

    

    public void setIdTipoPrueba(TipoPrueba idTipoPrueba) {
        this.idTipoPrueba = idTipoPrueba;
    }@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPrueba == null) ? 0 : idPrueba.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Prueba other = (Prueba) obj;
        if (idPrueba == null) {
            if (other.idPrueba != null)
                return false;
        } else if (!idPrueba.equals(other.idPrueba))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Prueba [idPrueba=" + idPrueba + ", nombre=" + nombre + ", indicaciones=" + indicaciones + 
               ", puntajeMaximo=" + puntajeMaximo + ", notaAprobacion=" + notaAprobacion + 
               ", duracion=" + duracion + ", fechaCreacion=" + fechaCreacion + "]";
    }
    
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
}
