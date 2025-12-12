package com.uacm.mapeo.nominas.persistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominaResumenDTO {
    private Long id;
    private String reciboId;
    private Long empleadoId;
    private String empleadoNombre;
    private String periodo;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
    private LocalDate fechaPago;
    private String tipoNomina;
    private Integer diasTrabajados;
    private Integer horasExtras;
    private Double totalPercepciones;
    private Double totalDeducciones;
    private Double totalNeto;
    private String estatusNomina;
    private LocalDate fechaRegistro;
}
