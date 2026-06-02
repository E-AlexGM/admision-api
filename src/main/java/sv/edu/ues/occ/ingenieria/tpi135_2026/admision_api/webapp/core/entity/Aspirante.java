package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@NamedQueries({
        @NamedQuery(
                name = "Aspirante.buscarPorApellidos",
                query = "SELECT a FROM Aspirante a WHERE a.apellidos = :apellidos"
        ),
        @NamedQuery(
                name = "Aspirante.buscarPorCorreo",
                query = "SELECT a FROM Aspirante a WHERE a.correo = :correo"
        )
})
@Entity
@Table(name = "aspirante")
public class Aspirante implements Serializable {

    @Id
    @Column(name = "id_aspirante")
    private UUID idAspirante;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Column(name = "documento_identidad")
    private String documentoIdentidad;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "fecha_creacion", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime fechaCreacion;

    public Aspirante(UUID idAspirante) {
        this.idAspirante = idAspirante;
    }

    public Aspirante(){}

    public UUID getIdAspirante() {
        return idAspirante;
    }

    public void setIdAspirante(UUID idAspirante) {
        this.idAspirante = idAspirante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idAspirante == null) ? 0 : idAspirante.hashCode());
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
        Aspirante other = (Aspirante) obj;
        if (idAspirante == null) {
            if (other.idAspirante != null)
                return false;
        } else if (!idAspirante.equals(other.idAspirante))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Aspirante [idAspirante=" + idAspirante + ", nombres=" + nombres + ", apellidos=" + apellidos + 
               ", fechaNacimiento=" + fechaNacimiento + ", documentoIdentidad=" + documentoIdentidad + 
               ", correo=" + correo + ", fechaCreacion=" + fechaCreacion + "]";
    }
}
