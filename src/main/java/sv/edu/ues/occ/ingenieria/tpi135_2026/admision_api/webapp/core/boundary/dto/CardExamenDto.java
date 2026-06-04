package sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.boundary.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import sv.edu.ues.occ.ingenieria.tpi135_2026.admision_api.webapp.core.control.ExamenResultadosEnum;

public record CardExamenDto(
    String nombrePrueba,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    OffsetDateTime fechaRealizacion,
    ExamenResultadosEnum resultado
) {

}
